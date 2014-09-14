package format

trait Mapper[T, W] {
  def mapTo(x: T): W
}
