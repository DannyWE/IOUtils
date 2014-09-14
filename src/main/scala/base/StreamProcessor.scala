package base

import java.io._
import javax.validation.{ConstraintViolation, Validation, Validator}

import au.com.bytecode.opencsv.{CSVWriter, CSVReader}
import core._

import scala.collection.JavaConversions._
import scalaz.concurrent.Task
import scalaz.stream._

object StreamProcessor {

//  val streamValidator: StreamValidationCollector = new StreamValidationCollector(Validation.buildDefaultValidatorFactory.getValidator)


//  def getCsvReader(file: File): CSVReader = new CSVReader(new FileReader(file))
//
//  def getCsvReader(reader: Reader): CSVReader = new CSVReader(reader)

//  def transform[T](file: File, f: StringArray => T): Process[Task, (T, Int)] = {
//    val reader: CSVReader = getCsvReader(file)
//
//    transformToT(reader, f)
//  }
//
//  def transform[T](reader: Reader, f: StringArray => T): Process[Task, (T, Int)] = {
//    val csvReader: CSVReader = getCsvReader(reader)
//
//    transformToT(csvReader, f)
//  }


  def write[T](it: Iterator[T], f: T => StringArray, writer: java.io.Writer): Unit = {
    val csvWriter: CSVWriter = new CSVWriter(writer)
    //takeThrough(t => t == null).map(f).map(t => csvWriter.writeNext(t)).run
    def mapToT: Next[T] => Task[StringArray] = t => Task(f(t.get))

    val result: Process[Task, Next[T]] = Process.repeatEval(Task{it.next}).takeWhile(t => !t.shouldStop)

    val channel = Process.constant(mapToT)

    (result through channel).map(t => csvWriter.writeNext(t)).run.run

    csvWriter.close()

  }

//  private def transformToT[T](csvReader: CSVReader, f: StringArray => T): Process[Task, (T, Int)] = {
//    def mapToT: StringArray => Task[T] = t => Task(f(t))
//
//    val result: Process[Task, StringArray] = Process.emitAll(csvReader.readAll())
//
//    val channel = Process.constant(mapToT)
//
//    (result through channel).zipWithIndex.map(t => (t._1, t._2 + 1))
//  }

}

