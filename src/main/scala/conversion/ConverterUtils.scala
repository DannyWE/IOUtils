package conversion

import com.google.common.base.Function

object ConverterUtils {


  implicit def convertGuavaToFunction1[T](f: Function[Array[String], T]): Array[String] => T = {
    val fFromScala: Array[String] => T = { array => f.apply(array)}
    fFromScala
  }


}
