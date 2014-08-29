import java.io.{FileReader, BufferedReader, File}
import java.util.Date

import app.CsvUtils
import org.scalatest.FunSuite

class CsvIntegrationTest extends FunSuite {



  test("get csv content correctly") {

    val file = new File(getClass.getResource("ApprovedInverter_Short.csv").getFile)

//    val lines = scala.io.Source.fromFile(file).mkString
//    println(lines)

    val containers = CsvUtils.parse[Container](file)

    assert(containers.head.manufacturer == "Ablerex Electronics Co Ltd")
    assert(containers.head.series == "EnerSolis")
    assert(containers.head.modelNumber == "ES2200")
    assert(containers.head.acPower == "2000")
//    assert(containers.head.approvedDate == "")
//    assert(containers.head.expiredDate == "")


  }

}

case class Container(manufacturer: String,
                     series: String,
                     modelNumber: String,
                     acPower: String,
                     approvedDate: Date,
                     expiredDate: Date
                      )
