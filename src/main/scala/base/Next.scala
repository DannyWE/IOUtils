package base

class Next[T](x: T) {

 @throws[Exception] def get: T = x
 @throws[Exception] def shouldStop: Boolean = false

}
