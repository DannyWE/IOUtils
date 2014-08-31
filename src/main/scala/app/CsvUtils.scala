package app

import java.io.File
import core.Consumer
import csv.CsvIO
import core.ConsumerOperation._
import javax.validation.{Validator, Validation, ValidatorFactory, ConstraintViolation}
import scala.collection.mutable
import java.util

//import java.util.{Set => JSet}
import scala.collection.JavaConversions._


object CsvUtils {

  val validator: Validator = Validation.buildDefaultValidatorFactory.getValidator

  def parse[T](file: File, f: Array[String] => T, condition: Consumer[Array[String],Unit]): Either[Seq[T], Set[ConstraintViolation[T]]] = {

    val voSeq: Seq[T] = CsvIO.enumerateFile(file, mapWithFilter(f, condition)).evaluate.run

    val validationSeq: Set[ConstraintViolation[T]] = voSeq
      .map(validator.validate(_).toSet)
      .reduceLeft[Set[ConstraintViolation[T]]](_ ++ _)

    validationSeq match {
      case x: Set[ConstraintViolation[T]] if x.isEmpty => Left(voSeq)
      case _ => Right(validationSeq)
    }

  }

  def parse[T](file: File, f: Array[String] => T): Seq[T] = {

    CsvIO.enumerateFile(file, map(f)).evaluate.run

  }
}


