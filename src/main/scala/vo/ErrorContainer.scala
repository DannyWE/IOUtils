package vo

import javax.validation.ConstraintViolation
import java.util
import format.{DefaultFieldMapper, Mapper}
import scala.collection.JavaConversions._

case class ErrorContainer[T](errorLine: (Int, Set[ConstraintViolation[T]])) {

  val getLineNumber: Int = errorLine._1
  val getConstraintViolation: util.Set[ConstraintViolation[T]] = errorLine._2.toSet[ConstraintViolation[T]]

  def getCustomizedErrorMessage(mapper: Mapper[String]): String = {
    val lineNumber: Int = errorLine._1


  }

  private def getFormattedErrorForEachLine(mapper: Mapper[String])


}
