package base

import core._
import scalaz.stream.Process
import scalaz.concurrent.Task

trait StreamTransformer {

  def transformT[T](reader: ReaderLike, f: StringArray => T): Process[Task, (T, Int)] = {
    def mapToT: StringArray => Task[T] = t => Task(f(t))

    val result: Process[Task, StringArray] = Process.emitAll(reader.readAll())

    val channel = Process.constant(mapToT)

    (result through channel).zipWithIndex.map(t => (t._1, t._2 + 1))
  }

}
