package base

trait Iterator[T] {



//  def hasNext: Boolean
  @throws[Exception] def next: T
  @throws[Exception] def close(): Unit

}
