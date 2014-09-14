package base

import scala.util.{Success, Failure, Try}
import vo.{ColumnConstraintViolation, MissingColumnViolation, LineConstraintViolation}
import javax.validation.{ConstraintViolation, Validation, Validator}
import scala.collection.JavaConversions._

class StreamValidator(validator: Validator) {



  def validate[T](line: (Try[T], Int)): LineConstraintViolation = {
    line._1 match {
      case Failure(t: java.lang.ArrayIndexOutOfBoundsException) =>
        MissingColumnViolation(line._2)
      case Success(t) =>
        ColumnConstraintViolation[T](line._2, validator.validate(t).toSet)
    }
  }
}
