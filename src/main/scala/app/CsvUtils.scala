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

  def parse[T](file: File, f: Array[String] => T, condition: Consumer[Array[String],Unit]): Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = {

    val voSeq: Seq[(Int, T)] = CsvIO.enumerateFile(file, mapWithFilter(f, condition)).evaluate.run

    val validationSeq: Seq[(Int, Set[ConstraintViolation[T]])] = voSeq
      .map(t => (t._1, validator.validate(t._2).toSet))

    validationSeq match {
      case x: Seq[(Int, ConstraintViolation[T])] if x.filter(p => ! p._2.isEmpty).isEmpty => Left(voSeq.map(_._2))
      case _ => Right(validationSeq)
    }

  }

  def parse[T](file: File, f: Array[String] => T): Seq[(Int, T)] = {

    CsvIO.enumerateFile(file, map(f)).evaluate.run

  }
}


