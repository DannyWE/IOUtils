package base

import java.io.{Reader, File}
import core._
import conversion.Result
import javax.validation.ConstraintViolation
import scalaz.stream.Process
import scalaz.concurrent.Task

trait ReaderService {

  def readerCreationModule: ReaderCreationModule
  def processModule: ProcessModule

  type ReaderCreationModule = ReaderCreationModuleLike
  
  trait ReaderCreationModuleLike {
    def getReader(file: File): ReaderLike
    def getReader(reader: Reader): ReaderLike
  }
  
  type ProcessModule = ProcessModuleLike

  trait ProcessModuleLike {
    def validate[T](process: Process[Task, (T, Int)], buffer: Int): EitherResult[T]
    def transform[T](reader: ReaderLike, f: StringArray => T): Process[Task, (T, Int)]
    def validateAll[T](process: Process[Task, (T, Int)]): EitherResult[T]
  }

  def parse[T](file: File, f: StringArray => T): Result[T] = {
    val processedResult: EitherResult[T] = processModule.validateAll(processModule.transform(readerCreationModule.getReader(file), f))
    Result(processedResult)
  }

  def parse[T](reader: Reader, f: StringArray => T): Result[T] = {
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = processModule
      .validateAll(processModule.transform(readerCreationModule.getReader(reader), f))
    Result(processedResult)
  }

  def parse[T](file: File, f: StringArray => T, ops: ProcessF[T] ): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(processModule.transform(readerCreationModule.getReader(file), f))
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = processModule.validateAll(operatedProcess)
    Result(processedResult)
  }

  def parse[T](reader: Reader, f: StringArray => T, ops: ProcessF[T] ): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(processModule.transform(readerCreationModule.getReader(reader), f))
    val processedResult: Either[Seq[T], Seq[(Int, Set[ConstraintViolation[T]])]] = processModule.validateAll(operatedProcess)
    Result(processedResult)
  }

  def parse[T](file: File, f: StringArray => T, buffer: Int): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = processModule.transform(readerCreationModule.getReader(file), f)
    Result(processModule.validate(operatedProcess, buffer))
  }

  def parse[T](reader: Reader, f: StringArray => T, buffer: Int = 1): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = processModule.transform(readerCreationModule.getReader(reader), f)
    Result(processModule.validate(operatedProcess, buffer))
  }

  def parse[T](file: File, f: StringArray => T, ops: ProcessF[T], buffer: Int): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(processModule.transform(readerCreationModule.getReader(file), f))
    Result(processModule.validate(operatedProcess, buffer))
  }

  def parse[T](reader: Reader, f: StringArray => T, ops: ProcessF[T], buffer: Int = 1): Result[T] = {
    val operatedProcess: Process[Task, (T, Int)] = ops(processModule.transform(readerCreationModule.getReader(reader), f))
    Result(processModule.validate(operatedProcess, buffer))
  }
}
