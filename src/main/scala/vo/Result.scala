package vo

import java.util
import java.util.{List => JList}
import javax.validation.ConstraintViolation

import core.EitherResult

import scala.collection.JavaConversions._
import scala.collection.breakOut


case class Result[T](either: EitherResult[T]) {

 lazy val isSuccessful: Boolean = either.isLeft
 lazy val isFailed: Boolean = either.isRight
 lazy val getResult: JList[T] = either.left.get
 lazy val getFailureResultAsMap: util.Map[Integer, util.Set[ConstraintViolation[T]]] = {
    mapAsJavaMap((either.right.get.map(identity)(breakOut): Map[Int, Set[ConstraintViolation[T]]])
      .filter(t => t._2.nonEmpty).map(t => (new Integer(t._1), setAsJavaSet(t._2))))
 }
 lazy val getFailureResult: JList[ErrorContainer[T]] = {
   either.right.get.filter(t => t._2.nonEmpty).map(ErrorContainer(_)).toList
  }

 lazy val getCustomizedErrorMessage: String = {
       ""
 }

}
