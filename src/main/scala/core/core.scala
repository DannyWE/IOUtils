import javax.validation.ConstraintViolation
import scala.util.Try
import scalaz.concurrent.Task
import scalaz.stream.Process
import vo.LineConstraintViolation

package object core {

  type StringArray = Array[String]

  type ProcessF[T] = Process[Task, (Try[T], Int)] => Process[Task, (Try[T], Int)]

  type EitherResult[T] = Either[Seq[T], Seq[LineConstraintViolation]]

}
