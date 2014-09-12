package format

trait Mapper[T] {
  def mapTo(x: T): T
}
