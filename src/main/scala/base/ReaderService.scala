package base

import java.io.{Reader, File}
import core._
import javax.validation.ConstraintViolation
import vo.Result

import scalaz.stream.Process
import scalaz.concurrent.Task
import scala.util.Try

trait ReaderService {

  def readerCreationModule: ReaderCreationModule
  def processModule: ProcessModule

  type ReaderCreationModule <: ReaderCreationModuleLike
  
  trait ReaderCreationModuleLike { this: ReaderCreationModule =>
    def getReader(file: File): ReaderLike
    def getReader(reader: Reader): ReaderLike
  }
  
  type ProcessModule <: ProcessModuleLike

  trait ProcessModuleLike { this: ProcessModule =>
    def validate[T](process: Process[Task, (Try[T], Int)], buffer: Int): EitherResult[T]
    def transform[T](reader: ReaderLike, f: StringArray => T): Process[Task, (Try[T], Int)]
    def validateAll[T](process: Process[Task, (Try[T], Int)]): EitherResult[T]
  }

  def parse[T](file: File, f: StringArray => T): Result[T] = {
    val readerLike: ReaderLike = readerCreationModule.getReader(file)
    val processedResult: EitherResult[T] = processModule.validateAll(processModule.transform(readerLike, f))
    readerLike.close()
    Result(processedResult)
  }

  def parse[T](reader: Reader, f: StringArray => T): Result[T] = {
    val readerLike: ReaderLike = readerCreationModule.getReader(reader)
    val processedResult: EitherResult[T] = processModule.validateAll(processModule.transform(readerLike, f))
    readerLike.close()
    Result(processedResult)
  }

  def parse[T](file: File, f: StringArray => T, ops: ProcessF[T] ): Result[T] = {
    val readerLike: ReaderLike = readerCreationModule.getReader(file)
    val operatedProcess: Process[Task, (Try[T], Int)] = ops(processModule.transform(readerLike, f))
    val processedResult: EitherResult[T] = processModule.validateAll(operatedProcess)
    readerLike.close()
    Result(processedResult)
  }

  def parse[T](reader: Reader, f: StringArray => T, ops: ProcessF[T] ): Result[T] = {
    val readerLike: ReaderLike = readerCreationModule.getReader(reader)
    val operatedProcess: Process[Task, (Try[T], Int)] = ops(processModule.transform(readerLike, f))
    val processedResult: EitherResult[T] = processModule.validateAll(operatedProcess)
    readerLike.close()
    Result(processedResult)
  }

  def parse[T](file: File, f: StringArray => T, buffer: Int): Result[T] = {
    val readerLike: ReaderLike = readerCreationModule.getReader(file)
    val operatedProcess: Process[Task, (Try[T], Int)] = processModule.transform(readerLike, f)
    readerLike.close()
    Result(processModule.validate(operatedProcess, buffer))
  }

  def parse[T](reader: Reader, f: StringArray => T, buffer: Int = 1): Result[T] = {
    val readerLike: ReaderLike = readerCreationModule.getReader(reader)
    val operatedProcess: Process[Task, (Try[T], Int)] = processModule.transform(readerLike, f)
    readerLike.close()
    Result(processModule.validate(operatedProcess, buffer))
  }

  def parse[T](file: File, f: StringArray => T, ops: ProcessF[T], buffer: Int): Result[T] = {
    val readerLike: ReaderLike = readerCreationModule.getReader(file)
    val operatedProcess: Process[Task, (Try[T], Int)] = ops(processModule.transform(readerLike, f))
    readerLike.close()
    Result(processModule.validate(operatedProcess, buffer))
  }

  def parse[T](reader: Reader, f: StringArray => T, ops: ProcessF[T], buffer: Int = 1): Result[T] = {
    val readerLike: ReaderLike = readerCreationModule.getReader(reader)
    val operatedProcess: Process[Task, (Try[T], Int)] = ops(processModule.transform(readerLike, f))
    readerLike.close()
    Result(processModule.validate(operatedProcess, buffer))
  }
}
