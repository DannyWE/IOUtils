import javax.validation.ConstraintViolation
import scalaz.concurrent.Task
import scalaz.stream.Process

package object core {

  type StringArray = Array[String]

  type ProcessF[T] = Process[Task, (T, Int)] => Process[Task, (T, Int)]

  type EitherResult[T] = Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]]

}
