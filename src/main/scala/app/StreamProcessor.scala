package app

import java.io.File
import au.com.bytecode.opencsv.CSVReader
import javax.validation.{Validation, Validator, ConstraintViolation}
import scalaz.stream._
import scalaz.concurrent.Task
import java.io.{FileReader, FileInputStream}
import core._
import scala.collection.JavaConversions._
import java.util

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

  def transform[T](file: File, f: StringArray => T): Process[Task, (T, Int)] = {
    val reader: CSVReader = new CSVReader(new FileReader(file))

    def mapToT: StringArray => Task[T] = t => Task(f(t))

    val result: Process[Task, StringArray] = Process.emitAll(reader.readAll())

    val channel = Process.constant(mapToT)

    (result through channel).zipWithIndex.map(t => (t._1, t._2 + 1))

  }
  

  def streamValidate[T](process: Process[Task, (T, Int)],
                         buffer: Int = 1): Seq[(Int, Set[ConstraintViolation[T]])] = {
    val errorRows: Process[Task, (Int, Set[ConstraintViolation[T]])] = process
      .map(t => (t._2, validator.validate(t._1).toSet))
      .filter(t => t._2.nonEmpty)
      .take(buffer)

    errorRows.runLog.run
  }

  def appendValidation[T](process: Process[Task, (T, Int)]): Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = {
    val voSeq = process.runLog.run

    checkValidation(voSeq)
  }


  private def checkValidation[T](seq: Seq[(T, Int)]): Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = {
    val validationSeq: Seq[(Int, Set[ConstraintViolation[T]])] = seq.map(t => (t._2, validator.validate(t._1).toSet))

    validationSeq match {
      case x: Seq[(Int, ConstraintViolation[T])] if x.filter(p => p._2.nonEmpty).isEmpty => Left(seq.map(_._1))
      case _ => Right(validationSeq)
    }
  }

}

