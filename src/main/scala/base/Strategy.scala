package base

sealed trait Strategy {

}

case object FailDirectly extends Strategy
case class Buffer(n: Int) extends Strategy