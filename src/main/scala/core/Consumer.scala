package core

trait Consumer[E, A] {

  def run: A = this match {
    case Done(x, _) => x
    case Cont(f) => f(EOF).run
    case Error(f) => f(EOF).run
  }

  def flatMap[B](f: A => Consumer[E,B]): Consumer[E, B] = this match {
    case Done(x, e) => f(x) match {
      case Done(y, _) => Done(y, e)
      case Cont(k) => k(e)
      case Error(k) => k(e)
    }
    case Cont(k) => Cont(e => k(e) flatMap f)
    case Error(k) => Error(e => k(e) flatMap f)
  }

  def map[B](f: A => B): Consumer[E, B] = this match {
    case Done(x, e) => Done(f(x), e)
    case Cont(k) => Cont(e => k(e) map f)
    case Error(k) => Error(e => k(e) map f)
  }

}

case class Done[E,A](a: A, e: Input[E]) extends Consumer[E,A]

case class Cont[E,A](f: Input[E] => Consumer[E,A]) extends Consumer[E,A]

case class Error[E, A](f: Input[E] => Consumer[E, A]) extends Consumer[E, A]


