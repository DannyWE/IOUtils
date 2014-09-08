package app

import java.io.File
import com.google.common.base.Function
import core._
import conversion.Result
import javax.validation.ConstraintViolation
import conversion.ConverterUtils._
import app.StreamProcessor._
import scalaz.stream.Process
import scalaz.concurrent.Task
import base.Strategy


class CsvIOService {

  def parseCsvFile[T](file: File, f: Function[StringArray, T]): Result[T] = {
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = appendValidation(StreamProcessor.transform(file, f))
    Result(processedResult)
  }

  def parseCsvFile[T](file: File, f: Function[StringArray, T], ops: ProcessF[T] ): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(StreamProcessor.transform(file, f))
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = appendValidation(operatedProcess)
    Result(processedResult)
  }

  def parseCsvFileWithStrategy[T](file: File, f: Function[StringArray, T]): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = StreamProcessor.transform(file, f)
    val validate: Seq[(Int, Set[ConstraintViolation[T]])] = StreamProcessor.streamValidate(operatedProcess)
//    if(validate.nonEmpty) Result[T](Right(validate))
//    else {

    println(validate.size)
      val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = appendValidation(operatedProcess)
      Result(processedResult)
//    }
  }
}
