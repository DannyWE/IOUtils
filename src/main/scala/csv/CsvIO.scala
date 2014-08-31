package csv

import java.io.{FileReader, File, BufferedReader}
import core._
import au.com.bytecode.opencsv.CSVReader

object CsvIO extends IOAction[CSVReader]{
  override def getReader(file: File): CSVReader = new CSVReader(new FileReader(file))

  override def getAdapter(x: CSVReader) = new IOReader {
    override def readLine(): StringArray = x.readNext()

    override def close(): Unit = x.close()
  }
}
