package core

sealed trait IOMonad[A] {
  def evaluate: A

  def flatMap[B](f: A => IOMonad[B]): IOMonad[B] = {
    f(evaluate)
  }

  def map[B](f: A => B): IOMonad[B]= {
    IOMonad (f(evaluate))
  }
}

object IOMonad {
  def apply[A](a: => A): IOMonad[A] = new IOMonad[A] {
    def evaluate = a
  }
}