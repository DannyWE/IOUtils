package base

trait Iterator[T] {



  @throws[Exception] def next: Next[T]
  @throws[Exception] def close(): Unit

}
