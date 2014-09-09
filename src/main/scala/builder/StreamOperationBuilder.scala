package builder

import core.ProcessF

class StreamOperationBuilder[T] {

  def drop(x: Int): ProcessF[T] = { process =>
    process.drop(x)
  }

  def take(x: Int): ProcessF[T] = { process =>
    process.take(x)
  }

  def takeWhile(f: ((T, Int)) => Boolean): ProcessF[T] = { process =>
    process.takeWhile(f)
  }

}
