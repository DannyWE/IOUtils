package app
import scalaz.stream._
import scalaz.concurrent.Task
import java.io.{InputStream, FileReader, FileOutputStream, FileInputStream}
import scalaz._
import Scalaz._
import core._
import app.Container
import au.com.bytecode.opencsv.CSVReader


object StreamUtils {



  def parse() = {

    def f(x: StringArray): Container = Container(x(0), x(1), x(2), x(3), x(4), x(5))
//
//    io.linesR(new FileInputStream("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv"))
//      .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
//      .map(t => f(t.split(","))).runLog
//      .run


    val reader: CSVReader = new CSVReader(new FileReader("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv"))
//    Process.unfold(Array[String]())(s => {
//      val acc =s ++ reader.readNext()
//      Some(acc, acc)
//    })
////      .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
//      .map(t => f(t)).runLog


//    Process(reader.readNext()).repeat.map(t => f(t)).runLog

//    Process.fol


  }


  def main(args: Array[String]) {
//    println(StreamUtils.parse())
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

