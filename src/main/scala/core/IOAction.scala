package core

import java.io.File
import scala.annotation.tailrec


trait IOReader {

  def close(): Unit
  def readLine(): StringArray

}

trait IOAction[T] {

  private def openReader(f: File): IOMonad[T] = IOMonad(getReader(f))

  private def closeReader(r: T): IOMonad[Unit] = IOMonad(getAdapter(r).close())

  private def contentReader[A](r: T, it: Consumer[StringArray, A]): IOMonad[Consumer[StringArray, A]] = {
    def loop(lineNumber: Int): Consumer[StringArray, A] => IOMonad[Consumer[StringArray, A]] = {
      case i @ Done(_, _) => IOMonad(i)
      case i @ Cont(k) => for {
        s <- IOMonad(getAdapter(r).readLine())
        a <- if (s == null) IOMonad(i) else loop(lineNumber + 1)(k(Element(s, lineNumber)))
      } yield a
    }
    loop(1)(it)
  }

  private def combine[A,B,C](ioInit: IOMonad[A], ioEnd: A => IOMonad[B], body: A => IOMonad[C]): IOMonad[C] =
    for { a <- ioInit
          c <- body(a)
          _ <- ioEnd(a) }
    yield c

  def getReader(x: File): T

  def getAdapter(t: T): IOReader
  
  def enumerateFile[A](f: File, i: Consumer[StringArray, A]): IOMonad[Consumer[StringArray, A]] =
    combine(openReader(f), closeReader(_:T), contentReader(_:T, i))
}

