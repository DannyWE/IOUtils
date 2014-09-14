package vo

import javax.validation.ConstraintViolation
import java.util.{List => JList}
import format.{SortedColumn, DefaultFieldMapper, Mapper}
import scala.collection.JavaConversions._

case class LineErrorConverter(errorLine: LineConstraintViolation) {

  val getLineNumber: Int = errorLine.getLineNumber
//  val getViolation: util.Set[ConstraintViolation[T]] = {
//    val set: Option[Set[ConstraintViolation[T]]] = errorLine.getConstraintViolationSet
//    set match {
//      case Some(result) => result.toSet[ConstraintViolation[T]]
//      case _ => Set[ConstraintViolation[T]]().toSet[ConstraintViolation[T]]
//    }
//  }

  def getViolations[T](mapper: Mapper[String, String]): JList[SimplifiedErrorContainer] = {
    errorLine match {
      case t: MissingColumnViolation => Seq(new SimplifiedErrorContainer(t.getLineNumber, t.getMessage))
      case t: ColumnConstraintViolation[T] => t.getConstraintViolationSet.map(w => SimplifiedErrorContainer(t.getLineNumber, mapper.mapTo(w.getPropertyPath.toString), w.getMessage)).toList
    }

//
//    if (errorLine.lineError != null) {
//      Seq(new SimplifiedErrorContainer(errorLine.getLineNumber, errorLine.lineError))
//    } else {
//      val set: Option[Set[ConstraintViolation[T]]] = errorLine.getConstraintViolationSet
//      set match {
//        case Some(result) => result.map(t => SimplifiedErrorContainer(errorLine.lineNumber, mapper.mapTo(t.getPropertyPath.toString), t.getMessage)).toList
//        case _ => Seq()
//      }
//    }
  }


  def getSortedViolations[T](mapper: Mapper[String, SortedColumn]): JList[SimplifiedErrorContainer] = {
    errorLine match {
      case t: MissingColumnViolation => Seq(new SimplifiedErrorContainer(t.getLineNumber, t.getMessage))
      case t: ColumnConstraintViolation[T] => t.getConstraintViolationSet.toList
        .sortBy(w => mapper.mapTo(w.getPropertyPath.toString).columnNumber)
        .map(w => SimplifiedErrorContainer(t.getLineNumber, mapper.mapTo(w.getPropertyPath.toString).columnName, w.getMessage))
    }


//    if (errorLine.lineError != null) {
//      Seq(new SimplifiedErrorContainer(errorLine.getLineNumber, errorLine.lineError))
//    } else {
//      val set: Option[Set[ConstraintViolation[T]]] = errorLine.getConstraintViolationSet
//      set match {
//        case Some(result) => result.toList
//          .sortBy(t => mapper.mapTo(t.getPropertyPath.toString).columnNumber)
//          .map(t => SimplifiedErrorContainer(errorLine.getLineNumber, mapper.mapTo(t.getPropertyPath.toString).columnName, t.getMessage))
//        case _ => Seq()
//      }
//    }
  }

  def getViolations: JList[SimplifiedErrorContainer] = {
    getViolations(DefaultFieldMapper)
  }
}
