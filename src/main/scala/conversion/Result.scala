package conversion

import javax.validation.ConstraintViolation
import java.util.{List => JList}
import scala.collection.breakOut
import scala.collection.JavaConversions._
import core.EitherResult
import java.util


case class Result[T](either: EitherResult[T]) {

 lazy val isSuccessful: Boolean = either.isLeft
 lazy val isFailed: Boolean = either.isRight
 lazy val getResult: JList[T] = either.left.get
 lazy val getFailureResult: util.Map[Integer, util.Set[ConstraintViolation[T]]] = {
   mapAsJavaMap((either.right.get.map(identity)(breakOut): Map[Int, Set[ConstraintViolation[T]]])
     .filter(t => t._2.nonEmpty).map(t => (new Integer(t._1), setAsJavaSet(t._2))))
 }

}
