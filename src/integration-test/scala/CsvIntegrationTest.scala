import core._
import java.io.{FileReader, BufferedReader, File}
import java.util.Date

import app.{CsvStreamProcessor, Container, CsvUtils}
import javax.validation.ConstraintViolation
import org.hibernate.validator.constraints.NotEmpty
import org.scalatest.FunSuite
import core.ConsumerOperation.drop

class CsvIntegrationTest extends FunSuite {



  test("get csv content correctly") {

    implicit def stringToDate(x: String): Date = new Date(x)

    val file = new File(getClass.getResource("ApprovedInverter_Short.csv").getFile)

//    val lines = scala.io.Source.fromFile(file).mkString
//    println(lines)

    def f(x: StringArray): Container = Container(x(0), x(1), x(2), x(3), x(4), x(5))

    val containers = CsvUtils.parse(file, f, drop(2)).left.get

    assert(containers.head.manufacturer == "Ablerex Electronics Co Ltd")
    assert(containers.head.series == "EnerSolis")
    assert(containers.head.modelNumber == "ES3300")
    assert(containers.head.acPower == "3000")

    println(containers.size)
    containers.foreach(println)
//    assert(containers.head.approvedDate == "")
//    assert(containers.head.expiredDate == "")


    val header = CsvUtils.parse(file, f)

    assert(header.head._2.manufacturer == "Manufacturer")
    assert(header.head._1 == 1)



  }


  test("should populate all the validation") {

    val file = new File(getClass.getResource("ApprovedInverter_invalid.csv").getFile)

    def f(x: StringArray): ValueObject = new ValueObject(x(0), x(1), x(2), x(3), x(4), x(5))

    val containers = CsvUtils.parse(file, f, drop(3)).right.get

    println(containers.head._1)
    println(containers(1)._1)
    println(containers(1)._2.head.getPropertyPath.toString)

    
    val row: (Int, Set[ConstraintViolation[ValueObject]]) = containers(3)

    assert(row._1 == 7)
    assert(row._2.size == 1)
    assert(row._2.head.getPropertyPath.toString == "expiredDate")
    assert(row._2.head.getMessage == "may not be empty")

  }

  test("whatever") {

    def f(x: StringArray): Container = Container(x(0), x(1), x(2), x(3), x(4), x(5))

    def fValue(x: StringArray): ValueObject = new ValueObject(x(0), x(1), x(2), x(3), x(4), x(5))

    //    val validResult = CsvStreamProcessor.parse(new File("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv"), f)
    //
    //    println(validResult.left.get.size)

    val invalidResult = CsvStreamProcessor.parse(new File("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_invalid.csv"), fValue)

    println(invalidResult.right.get)

  }

}

case class Container(manufacturer: String,
                     series: String,
                     modelNumber: String,
                     acPower: String,
                     approvedDate: String,
                     expiredDate: String
                      )

//import javax.validation.constraints.NotNull
//
//class ValueObject3(  @NotEmpty manufacturer: String,
//                     @NotEmpty series: String,
//                     @NotEmpty modelNumber: String,
//                     @NotEmpty acPower: String,
//                     @NotEmpty approvedDate: String,
//                     @NotEmpty expiredDate: String
//                      ) {
//
//  def getManufacturer = manufacturer
//  def getSeries = series
//  def getModelNumber = modelNumber
//  def getAcPower = acPower
//  def getApprovedDate = approvedDate
//  def getExpiredDate = expiredDate
//
//
//}