package base

import scalaz.stream.Process
import scalaz.concurrent.Task
import core._
import javax.validation.ConstraintViolation
import javax.validation.{ConstraintViolation, Validation, Validator}
import scala.collection.JavaConversions._


class StreamValidator(validator: Validator) {

  def validateAll[T](process: Process[Task, (T, Int)]): EitherResult[T] = {
    val voSeq = process.runLog.run
    val validationSeq: Seq[(Int, Set[ConstraintViolation[T]])] = voSeq.map(t => (t._2, validator.validate(t._1).toSet))

    validationSeq match {
      case x: Seq[(Int, ConstraintViolation[T])] if x.filter(p => p._2.nonEmpty).isEmpty => Left(voSeq.map(_._1))
      case _ => Right(validationSeq)
    }
  }

  def validate[T](process: Process[Task, (T, Int)], buffer: Int = 1): EitherResult[T] = {
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

}
