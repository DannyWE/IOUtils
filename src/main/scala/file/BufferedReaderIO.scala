package file

import core._
import java.io.{FileReader, File, BufferedReader}

object BufferedReaderIO extends IOAction[BufferedReader] {
  override def getReader(file: File): BufferedReader = new BufferedReader(new FileReader(file))

  override def getAdapter(x: BufferedReader) = new IOReader {
    override def readLine(): StringArray = x.readLine().split(" ")

    override def close(): Unit = x.close()
  }
}
