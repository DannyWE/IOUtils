package base

import java.io._
import javax.validation.{ConstraintViolation, Validation, Validator}

import au.com.bytecode.opencsv.{CSVWriter, CSVReader}
import core._

import scala.collection.JavaConversions._
import scalaz.concurrent.Task
import scalaz.stream._

object StreamProcessor {

  val validator: Validator = Validation.buildDefaultValidatorFactory.getValidator

  def parse[T](file: File, separator: String, f: Array[String] => T): Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = {
    val voSeq: IndexedSeq[(T, Int)] = io.linesR(new FileInputStream(file))
      .map(t => f(t.split(separator)))
      .zipWithIndex.map(t => (t._1, t._2 + 1))
      .runLog
      .run

    checkValidation(voSeq)
  }

  def getCsvReader(file: File): CSVReader = new CSVReader(new FileReader(file))

  def getCsvReader(reader: Reader): CSVReader = new CSVReader(reader)

  def transform[T](file: File, f: StringArray => T): Process[Task, (T, Int)] = {
    val reader: CSVReader = getCsvReader(file)

    transformToT(reader, f)
  }

  def transform[T](reader: Reader, f: StringArray => T): Process[Task, (T, Int)] = {
    val csvReader: CSVReader = getCsvReader(reader)

    transformToT(csvReader, f)
  }

  def validateWithBuffer[T](process: Process[Task, (T, Int)], buffer: Int = 1): EitherResult[T] = {
    val errorRows: Process[Task, (Int, Set[ConstraintViolation[T]])] = process
      .map(t => (t._2, validator.validate(t._1).toSet))
      .filter(t => t._2.nonEmpty)
      .take(buffer)

    val validate: Seq[(Int, Set[ConstraintViolation[T]])] = errorRows.runLog.run

    if(validate.nonEmpty) {
      Right(validate)
    }
    else {
      val voSeq = process.runLog.run
      Left(voSeq.map(_._1))
    }
  }

  def validateAll[T](process: Process[Task, (T, Int)]): EitherResult[T] = {
    val voSeq = process.runLog.run

    checkValidation(voSeq)
  }


  def write[T](it: Iterator[T], f: T => StringArray, writer: java.io.Writer): Unit = {
    val csvWriter: CSVWriter = new CSVWriter(writer)
    //takeThrough(t => t == null).map(f).map(t => csvWriter.writeNext(t)).run
    def mapToT: T => Task[StringArray] = t => Task(f(t))

    val result: Process[Task, T] = Process.repeatEval(Task{it.next}).takeWhile(t => t != null)

    val channel = Process.constant(mapToT)

    (result through channel).map(t => csvWriter.writeNext(t)).run.run

    csvWriter.close()

  }

  private def transformToT[T](csvReader: CSVReader, f: StringArray => T): Process[Task, (T, Int)] = {
    def mapToT: StringArray => Task[T] = t => Task(f(t))

    val result: Process[Task, StringArray] = Process.emitAll(csvReader.readAll())

    val channel = Process.constant(mapToT)

    (result through channel).zipWithIndex.map(t => (t._1, t._2 + 1))
  }

  private def checkValidation[T](seq: Seq[(T, Int)]): EitherResult[T] = {
    val validationSeq: Seq[(Int, Set[ConstraintViolation[T]])] = seq.map(t => (t._2, validator.validate(t._1).toSet))

    validationSeq match {
      case x: Seq[(Int, ConstraintViolation[T])] if x.filter(p => p._2.nonEmpty).isEmpty => Left(seq.map(_._1))
      case _ => Right(validationSeq)
    }
  }

}

