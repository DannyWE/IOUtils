package appJava

import java.io.{Reader, File}
import com.google.common.base.Function
import core._
import conversion.ConverterUtils._
import vo.Result
import app.CsvReaderService


class CsvService extends CsvReaderService {
  def parse[T](file: File, f: Function[StringArray, T]): Result[T] = super.parse(file, f)
  def parse[T](reader: Reader, f: Function[StringArray, T]): Result[T] =  super.parse(reader, f)
  def parse[T](file: File, f: Function[StringArray, T], ops: ProcessF[T] ): Result[T] = super.parse(file, f, ops)
  def parse[T](reader: Reader, f: Function[StringArray, T], ops: ProcessF[T] ): Result[T] = super.parse(reader, f, ops)
  def parse[T](file: File, f: Function[StringArray, T], buffer: Int): Result[T] = super.parse(file, f, buffer)
  def parse[T](reader: Reader, f: Function[StringArray, T], buffer: Int): Result[T] = super.parse(reader, f, buffer)
  def parse[T](file: File, f: Function[StringArray, T], ops: ProcessF[T], buffer: Int): Result[T] = super.parse(file, f, ops, buffer)
  def parse[T](reader: Reader, f: Function[StringArray, T], ops: ProcessF[T], buffer: Int): Result[T] = super.parse(reader, f, ops, buffer)
}
