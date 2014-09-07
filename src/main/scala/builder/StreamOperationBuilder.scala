package builder

import scalaz.stream.Process
import scalaz.concurrent.Task

object StreamOperationBuilder {


  def drop[T](x: Int): Process[Task, (T, Int)] => Process[Task, (T, Int)] = { process =>
    process.drop(x)
  }

  def take[T](x: Int): Process[Task, (T, Int)] => Process[Task, (T, Int)] = { process =>
    process.take(x)
  }


}
