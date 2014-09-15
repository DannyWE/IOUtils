package app

import java.io.{Writer, Reader, OutputStream}

import com.google.common.base.Function
import core._
import base.{StreamProcessor, StreamIterator}
import conversion.ConverterUtils._
import scalaz.stream.Process
import scalaz.concurrent.Task

class CsvWriterService {

  def writeTOStream[T](it: StreamIterator[T],f: Function[T, StringArray], writer: Writer): Unit = {

    StreamProcessor.write(it, f, writer)
  }


}
