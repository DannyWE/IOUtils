package app

import java.io.File
import au.com.bytecode.opencsv.CSVReader
import core.Consumer
import javax.validation.{Validation, Validator, ConstraintViolation}
import csv.CsvIO
import core.ConsumerOperation._
import scalaz.stream._
import scalaz.concurrent.Task
import java.io.{InputStream, FileReader, FileOutputStream, FileInputStream}
import scalaz._
import Scalaz._
import core._
import app.Container
import scala.collection.JavaConversions._

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

  def parseCsvFile[T](file: File, f: StringArray => T): Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = {
    val reader: CSVReader = new CSVReader(new FileReader(file))

    def mapToT: StringArray => Task[T] = t => Task(f(t))

    val result: Process[Task, StringArray] = Process.emitAll(reader.readAll())

    val channel = Process.constant(mapToT)

    val voSeq = (result through channel).zipWithIndex.map(t => (t._1, t._2 + 1)).runLog.run

    checkValidation(voSeq)
  }


  private def checkValidation[T](seq: Seq[(T, Int)]): Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = {
    val validationSeq: Seq[(Int, Set[ConstraintViolation[T]])] = seq.map(t => (t._2, validator.validate(t._1).toSet))

    validationSeq match {
      case x: Seq[(Int, ConstraintViolation[T])] if x.filter(p => p._2.nonEmpty).isEmpty => Left(seq.map(_._1))
      case _ => Right(validationSeq)
    }
  }

  def main(args: Array[String]) = {

    def f(x: StringArray): Container = Container(x(0), x(1), x(2), x(3), x(4), x(5))

    var fileName = "C:\\workspace\\git\\IOUtils\\src\\integration-test\\resources\\ApprovedInverter_Long.csv"

    val result = StreamProcessor.parseCsvFile(new File(fileName), f)

    println(result.left.get.size)

  }

}

