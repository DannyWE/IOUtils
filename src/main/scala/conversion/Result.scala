package conversion

import javax.validation.ConstraintViolation
import java.util.{List => JList}
import scala.collection.breakOut
import scala.collection.JavaConversions._


class Result[T](either: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]]) {

 lazy val isSuccessful: Boolean = either.isLeft
 lazy val isFailed: Boolean = either.isLeft
 lazy val getResult: JList[T] = either.left.get
 lazy val getFailureResult: Map[Int, Set[ConstraintViolation[T]]] = either.right.get.map(identity)(breakOut)

}


object Result {

  def apply[T](either: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]]) = {
    new Result(either: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]])
  }

}