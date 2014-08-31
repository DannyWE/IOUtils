package core

object ConsumerOperation {


  def drop[E,A](n: Int): Consumer[E,Unit] = {
    def step: Input[E] => Consumer[E,Unit] = {
      case Element(x, _) => drop(n - 1)
      case Empty => Cont(step)
      case EOF => Done((), EOF)
    }
    if (n == 0) Done((), Empty) else Cont(step)
  }

  def head[E]: Consumer[E, Option[E]] = {
    def step: Input[E] => Consumer[E, Option[E]] = {
      case Element(x, _) => Done(Some(x), Empty)
      case Empty => Cont(step)
      case EOF => Done(None, EOF)
    }
    Cont(step)
  }

  def map[E, T](f: E => T): Consumer[E, Seq[(Int, T)]] = {
    def step(t: Seq[(Int, T)]): Input[E] => Consumer[E, Seq[(Int, T)]] = {
      case Element(x, v) => Cont(step(t :+ (v, f(x))))
      case Empty => Cont(step(t))
      case EOF => Done(t, EOF)
    }

    Cont(step(Seq()))
  }

  def mapWithFilter[E, T](f: E => T, precondition: Consumer[E,Unit]): Consumer[E, Seq[(Int, T)]] = {
    for {
      e <- precondition
      t <- map(f)
    } yield t
  }

}

