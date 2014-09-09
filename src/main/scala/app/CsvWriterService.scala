package app

import java.io.{Reader, OutputStream}

import com.google.common.base.Function
import core._

class CsvWriterService {

  def writeTOStream[T](reader: Reader, f: Function[T, StringArray], stream: OutputStream): Unit = {

  }


}
