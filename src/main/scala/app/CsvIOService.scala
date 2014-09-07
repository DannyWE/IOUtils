package app

import conversion.{ConverterUtils, Result}
import java.io.File
import com.google.common.base.Function
import core._
import conversion.Result
import javax.validation.ConstraintViolation
import conversion.ConverterUtils._
import builder.StreamOperationBuilder
import app.StreamProcessor._
import scalaz.stream.Process
import scalaz.concurrent.Task


class CsvIOService {

  def parseCsvFile[T](file: File, f: Function[StringArray, T]): Result[T] = {
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = appendValidation(StreamProcessor.transform(file, f))
    Result(processedResult)
  }

  def parseCsvFile[T](file: File, f: Function[StringArray, T], opBuilder: (Process[Task, (T, Int)] => Process[Task, (T, Int)]) * ): Result[T] = {
    val process: Process[Task, (T, Int)] = StreamProcessor.transform(file, f)
    val operatedResult: Process[Task, (T, Int)] = opBuilder.foldLeft(process)((acc, ele) => ele(acc))
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = appendValidation(operatedResult)
    Result(processedResult)
  }
}
