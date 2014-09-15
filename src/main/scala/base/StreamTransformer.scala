package base

import core._
import scalaz.stream.{Sink, Process}
import scalaz.concurrent.Task
import scala.util.Try

trait StreamTransformer {

  def transformT[T](reader: ReaderLike, f: StringArray => T): Process[Task, (Try[T], Int)] = {
    def mapToT: StringArray => Task[Try[T]] = t => Task(Try{f(t)})

    val result: Process[Task, StringArray] = Process.emitAll(reader.readAll())

    val channel = Process.constant(mapToT)

    (result through channel).zipWithIndex.map(t => (t._1, t._2 + 1))
  }

  def iterateT[T](reader: ReaderLike, f: StringArray => T) = {
//    val iterator = Iterator[(Try[T], Int)]

//    def mapToT: StringArray => Task[Try[T]] = t => Task(Try{f(t)})
//
//    val result: Process[Task, StringArray] = Process.emit(reader.readLine()).repeat
//
//    def write(t: (Try[T], Int)): Task[Unit] = Task delay { Iterator.continually(t)}
//
//    val sink: Sink[Task, (Try[T], Int)] = Process.constant(write _)
//
//    val channel = Process.constant(mapToT)
//
//    (result through channel).zipWithIndex.map(t => (t._1, t._2 + 1)).to(sink)

  }

}
