package builder

import core.ProcessF
import scala.util.Try

class StreamOperationBuilder[T] {

  def drop(x: Int): ProcessF[T] = { process =>
    process.drop(x)
  }

  def take(x: Int): ProcessF[T] = { process =>
    process.take(x)
  }

  def takeWhile(f: ((Try[T], Int)) => Boolean): ProcessF[T] = { process =>
    process.takeWhile(f)
  }

}
