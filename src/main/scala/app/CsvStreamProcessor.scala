package app

import java.io.File
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

object CsvStreamProcessor {

  val validator: Validator = Validation.buildDefaultValidatorFactory.getValidator

  def parse[T](file: File, f: Array[String] => T): Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = {
    val containerSeq: IndexedSeq[(T, Int)] = io.linesR(new FileInputStream(file))
      .map(t => {
      val split: Array[String] = t.split(",")
      println(f(split))

      f(split)
    })
      .zipWithIndex
      .runLog
      .run

    val validationSeq: Seq[(Int, Set[ConstraintViolation[T]])] = containerSeq
      .map(t => (t._2, validator.validate(t._1).toSet))

    validationSeq match {
      case x: Seq[(Int, ConstraintViolation[T])] if x.filter(p => !p._2.isEmpty).isEmpty => Left(containerSeq.map(_._1))
      case _ => Right(validationSeq)
    }

  }



}

