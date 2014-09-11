package base

import core.StringArray
import au.com.bytecode.opencsv.CSVReader
import scala.collection.JavaConversions._

abstract class CsvStreamTransformer(csvReader: CSVReader) extends StreamTransformer {

//  class CsvReader extends Reader {
//    override def getReader(): ReaderLike = ???
//
//    override def close(): Unit = csvReader.close()
//
//    override def readAll(): Seq[StringArray] = csvReader.readAll()
//  }

}
