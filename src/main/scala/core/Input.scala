package core

sealed trait Input[+E]

case class Element[E](e: E, lineNumber: Int) extends Input[E]
case object Empty extends Input[Nothing]
case object EOF extends Input[Nothing]
