package base

import core._

trait ReaderLike {
//  def getReader(): ReaderLike
  @throws[Exception] def readAll(): Seq[StringArray]
  @throws[Exception] def close(): Unit
}

