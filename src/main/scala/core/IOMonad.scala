package core

sealed trait IOMonad[A] {
  def getLineNumber: Int
  def evaluate: A

  def flatMap[B](f: A => IOMonad[B]): IOMonad[B] = {
    f(evaluate)
  }

  def map[B](f: A => B): IOMonad[B]= {
    IOMonad (f(evaluate), getLineNumber)
  }
}

object IOMonad {
  def apply[A](a: => A, lineNumber: Int = -1): IOMonad[A] = new IOMonad[A] {
    def evaluate = a
    def getLineNumber = lineNumber
  }
}