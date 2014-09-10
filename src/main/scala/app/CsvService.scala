package app

import java.io.{Reader, File}
import base.StreamProcessor
import com.google.common.base.Function
import core._
import conversion.Result
import javax.validation.ConstraintViolation
import conversion.ConverterUtils._
import StreamProcessor._
import scalaz.stream.Process
import scalaz.concurrent.Task


class CsvService {

  def parse[T](file: File, f: Function[StringArray, T]): Result[T] = {
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = validateAll(StreamProcessor.transform(file, f))
    Result(processedResult)
  }

  def parse[T](reader: Reader, f: Function[StringArray, T]): Result[T] = {
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = validateAll(StreamProcessor.transform(reader, f))
    Result(processedResult)
  }

  def parse[T](file: File, f: Function[StringArray, T], ops: ProcessF[T] ): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(StreamProcessor.transform(file, f))
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = validateAll(operatedProcess)
    Result(processedResult)
  }

  def parse[T](reader: Reader, f: Function[StringArray, T], ops: ProcessF[T] ): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(StreamProcessor.transform(reader, f))
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = validateAll(operatedProcess)
    Result(processedResult)
  }

  def parseWithLimitedValidation[T](file: File, f: Function[StringArray, T], buffer: Int): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = StreamProcessor.transform(file, f)
    Result(StreamProcessor.validateWithBuffer(operatedProcess, buffer))
  }

  def parseWithLimitedValidation[T](reader: Reader, f: Function[StringArray, T], buffer: Int = 1): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = StreamProcessor.transform(reader, f)
    Result(StreamProcessor.validateWithBuffer(operatedProcess, buffer))
  }

  def parseWithLimitedValidation[T](file: File, f: Function[StringArray, T], ops: ProcessF[T], buffer: Int): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(StreamProcessor.transform(file, f))
    Result(StreamProcessor.validateWithBuffer(operatedProcess, buffer))
  }

  def parseWithLimitedValidation[T](reader: Reader, f: Function[StringArray, T], ops: ProcessF[T], buffer: Int = 1): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(StreamProcessor.transform(reader, f))
    Result(StreamProcessor.validateWithBuffer(operatedProcess, buffer))
  }

}
