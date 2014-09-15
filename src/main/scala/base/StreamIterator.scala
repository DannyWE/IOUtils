package base

trait StreamIterator[T] {



  @throws[Exception] def next: Next[T]
  @throws[Exception] def close(): Unit

}
