package vo

import java.util
import java.util.{List => JList}
import javax.validation.ConstraintViolation

import core.EitherResult

import scala.collection.JavaConversions._
import scala.collection.breakOut
import format._
import vo.LineErrorConverter


case class Result[T](either: EitherResult[T]) {

 lazy val isSuccessful: Boolean = either.isLeft
 lazy val isFailed: Boolean = either.isRight
 lazy val getResult: JList[T] = either.left.get
// lazy val getFailureResultAsMap: util.Map[Integer, util.Set[ConstraintViolation[T]]] = {
//    mapAsJavaMap((either.right.get.map(identity)(breakOut): Map[Int, Set[ConstraintViolation[T]]])
//      .filter(t => t._2.nonEmpty).map(t => (new Integer(t._1), setAsJavaSet(t._2))))
// }
 lazy val getFailureResult: JList[LineErrorConverter] = {
   either.right.get.filter(t => t.hasError).map(LineErrorConverter).toList
  }

  def getSimplifiedFailureResult(mapper: Mapper[String, String]): JList[SimplifiedErrorContainer] = {
    val allErrors: Seq[SimplifiedErrorContainer] = either.right.get
      .filter(_.hasError)
      .map(LineErrorConverter(_)
      .getViolations(mapper))
      .flatten

    val partition: (Seq[SimplifiedErrorContainer], Seq[SimplifiedErrorContainer]) = allErrors.partition(_.columnName == null)
    (partition._2.sortBy(_.columnName) ++ partition._1).toList
  }

  def getSortedSimplifiedFailureResult(mapper: Mapper[String, SortedColumn]): JList[SimplifiedErrorContainer] = {
    either.right.get
      .filter(_.hasError)
      .map(LineErrorConverter(_)
      .getSortedViolations(mapper))
      .flatten
      .toList
  }

  def getFormattedErrorMessage(formatter: ErrorLineFormatter = DefaultErrorLineFormatter, split: String = "\n"): String = {
    val result: Seq[SimplifiedErrorContainer] = getSimplifiedFailureResult(DefaultFieldMapper)
    result.sortBy(_.lineNumber).map(t => formatter.format(t.lineNumber, t.columnName, t.errorMessage)).mkString(split)
  }

  def getSortedFormattedErrorMessage(mapper: Mapper[String, SortedColumn], formatter: ErrorLineFormatter = DefaultErrorLineFormatter, split: String = "\n"): String = {
    val result: Seq[SimplifiedErrorContainer] = getSortedSimplifiedFailureResult(mapper)
    result.sortBy(_.lineNumber).map(t => formatter.format(t.lineNumber, t.columnName, t.errorMessage)).mkString(split)
  }

  //for java

  def getSimplifiedFailureResult: JList[SimplifiedErrorContainer] = {
    getSimplifiedFailureResult(DefaultFieldMapper)
  }

  def getFormattedErrorMessage: String = {
    getFormattedErrorMessage(DefaultErrorLineFormatter, "\n")
  }

  def getFormattedErrorMessage(formatter: ErrorLineFormatter): String = {
    getFormattedErrorMessage(formatter, "\n")
  }

  def getSortedFormattedErrorMessage(mapper: Mapper[String, SortedColumn], split: String): String = {
    getSortedFormattedErrorMessage(mapper, DefaultErrorLineFormatter, split)
  }

  def getSortedFormattedErrorMessage(mapper: Mapper[String, SortedColumn]): String = {
    getSortedFormattedErrorMessage(mapper, DefaultErrorLineFormatter, "\n")
  }
}
