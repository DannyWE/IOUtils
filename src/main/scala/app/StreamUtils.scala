package app
import scalaz.stream._
import scalaz.concurrent.Task
import java.io._
import scalaz._
import Scalaz._
import core._
import app.Container
import au.com.bytecode.opencsv.CSVReader
import scala.collection.JavaConversions._


object StreamUtils {



  def parse() = {

//    def f(x: StringArray): Container = Container(x(0), x(1), x(2), x(3), x(4), x(5))
    def f(x: StringArray): Container = Container(x(0))
//
//    io.linesR(new FileInputStream("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv"))
//      .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
//      .map(t => f(t.split(","))).runLog
//      .run


    var fileName = "C:\\workspace\\git\\IOUtils\\src\\integration-test\\resources\\ApprovedInverter_Short.csv"
    fileName = "C:\\workspace\\spike\\dummy.txt"
    val reader: CSVReader = new CSVReader(new FileReader(fileName))

    val bufferedReader = new BufferedReader(new FileReader(fileName))
//    Process.unfold(Array[String]())(s => {
//      val acc =s ++ reader.readNext()
//      Some(acc, acc)
//    })
////      .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
//      .map(t => f(t)).runLog


//    Process(reader.readNext()).repeat.map(t => f(t)).runLog

//    Process.fol


    val mapToContainer: StringArray => Task[Container] = x => Task({
      println(f(x))
      f(x)
    })
    val lengthChannel = Process.constant(mapToContainer)
    val source: Process[Task, String] = Process.repeatEval(Task({
      val t = bufferedReader.readLine()
      println(t)
      t
    })).takeThrough(t => t != null)
//      .map(t => f(t))
//    val lengths = source through lengthChannel
    source.run.run


//    io.linesR(fileName)
//      .map(t => f(t.split(separator)))
//      .zipWithIndex.map(t => (t._1, t._2 + 1))
//      .intersperse("\n")
//      .pipe(text.utf8Encode)
//      .to(io.fileChunkW("ApprovedInverter_invalid_copy.csv"))
//      .run.run


  }


  def main(args: Array[String]) {
//    println(StreamUtils.parse())
//    println(StreamUtils.parse().size)

//    val iterator = List(1,2,3).iterator
//
//    def next(): Int = {
//      println("...")
//      if (iterator.hasNext)
//      iterator.next()
//      else throw new Exception()
//
//    }
//
//    def nextTask: Task[Int] = {
//      if (iterator.hasNext) {
//        val next = iterator.next()
//        println(next)
//        Task{next}
//      }
//      else Task.fail(new Exception)
//    }
//
    def multiply: Int => Task[Int] = x => {
      Thread.sleep(3000)
      println("processing" + x)
      Task(x * 3)
    }
//
    val multiplyChannel = Process.constant(multiply)
//
////    val source: Process[Task, Int] = Process.iterate(0)(t => iterator.next()).takeThrough(t => iterator.hasNext)
//    val source: Process[Task, Int] = Process.repeatEval(Task{iterator.next()}).takeWhile(t => iterator.hasNext)
//
//    val run = (source through multiplyChannel).runLog.run
//
//    println(run)

        val source: Process[Task, Int] = Process.emitAll(List(1,2,3))

        val run = (source through multiplyChannel).runLog.run

        println(run)

  }


}
case class Container(manufacturer: String,
                     series: String = "",
                     modelNumber: String = "",
                     acPower: String = "",
                     approvedDate: String = "",
                     expiredDate: String = ""
                      )

object transform {


  def take(x: Array[String]): Container = Container(x(0), x(1), x(2), x(3), x(4), x(5))
}

