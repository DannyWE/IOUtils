package app

import java.io.{Reader, File}
import com.google.common.base.Function
import scala.Predef.Function
import scala.Function
import core._
import conversion.Result
import javax.validation.{Validation, ConstraintViolation}
import base._
import scalaz.stream.Process
import scalaz.concurrent.Task
import csv.{CsvReaderAdaptor, CsvReaderCreator}
import au.com.bytecode.opencsv.CSVReader

class CsvReaderService extends ReaderService with StreamTransformer {

  val streamValidator: StreamValidator = new StreamValidator(Validation.buildDefaultValidatorFactory.getValidator)

  class CsvProcessModule extends ProcessModuleLike {
    override def validateAll[T](process: Process[Task, (T, Int)]): EitherResult[T] = streamValidator.validateAll(process)

    override def transform[T](reader: ReaderLike, f: (StringArray) => T): Process[Task, (T, Int)] = transformT(reader, f)

    override def validate[T](process: Process[Task, (T, Int)], buffer: Int): EitherResult[T] = streamValidator.validate(process, buffer)
  }

  class CsvReaderCreatorModule extends ReaderCreationModuleLike {
    override def getReader(reader: Reader): ReaderLike = CsvReaderAdaptor(reader)

    override def getReader(file: File): ReaderLike = CsvReaderAdaptor(file)
  }


  override def readerCreationModule: ReaderCreationModuleLike = new CsvReaderCreatorModule

  override def processModule: ProcessModuleLike = new CsvProcessModule
}
