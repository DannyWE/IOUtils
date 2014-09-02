package app
import scalaz.stream._
import scalaz.concurrent.Task
import java.io.{FileOutputStream, FileInputStream}
import scalaz._
import Scalaz._


object StreamUtils {



  def parse() = {


    io.linesR(new FileInputStream("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv"))
      .intersperse("\n")
      .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
      .map(t => {
//      println(t)
      val container = transform.take(t.split(','))
      println(container)
      container
    })
//      .pipe(text.utf8Encode)
//      .to(io.chunkW(new FileOutputStream("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_invalid_copy.csv")))
      .run
      .run



//    val converter: Task[Unit] =
//      io.linesR("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv")
//        .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
//        .intersperse("\n")
//        .pipe(text.utf8Encode)
//        .to(io.chunkW(new FileOutputStream("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_invalid_copy.csv")))
//        .run
//
//
//    val u: Unit = converter.run

//    io.linesR("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv")
//    .once.runLast.run
  }


  def main(args: Array[String]) {
    println(StreamUtils.parse())
  }


}
case class Container(manufacturer: String,
                     series: String,
                     modelNumber: String,
                     acPower: String,
                     approvedDate: String,
                     expiredDate: String
                      )

object transform {


  def take(x: Array[String]): Container = Container(x(0), x(1), x(2), x(3), x(4), x(5))
}