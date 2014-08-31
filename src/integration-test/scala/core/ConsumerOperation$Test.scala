package core

import org.scalatest.FunSuite
import java.io.{Reader, FileReader, File, BufferedReader}
import csv.CsvIO
import file.BufferedReaderIO
import core.Consumer
import com.sun.beans.decoder.ValueObject

class ConsumerOperation$Test extends FunSuite {



  test("should iteratee List") {

    import ConsumerOperation._
    def enumerate[E,A]: (List[E], Consumer[E,A]) => Consumer[E,A] = {
      case (Nil, i) => i
      case (_, i@Done(_, _)) => i
      case (x :: xs, Cont(k)) => enumerate(xs, k(Element(x)))
    }


    def combination[E]: Consumer[E, Option[E]] = for {
      e <- drop(3)
      t <- head
    } yield t



    val result = enumerate(List(1,2,3,4), combination[Int]).run

    println(result.get)

  }


  test("file reader") {
    import ConsumerOperation._


//    def enumReader[A](r: BufferedReader, it: Consumer[String, A]): IO[Consumer[String, A]] = {
//      def loop: Consumer[String, A] => IO[Consumer[String, A]] = {
//        case i @ Done(_, _) => IO { i }
//        case i @ Cont(k) => for {
//          s <- IO { r.readLine }
//          a <- if (s == null) IO { i } else loop(k(Element(s)))
//        } yield a
//      }
//      loop(it)
//    }
//
//    def bufferFile(f: File) = IO {
//      new BufferedReader(new FileReader(f))
//    }
//
//    def closeReader(r: Reader) = IO {
//      r.close()
//    }
//
//    def bracket[A,B,C](ioInit: IO[A], body: A => IO[B], ioEnd: A => IO[C]): IO[B] =
//      for { a <- ioInit
//            c <- body(a)
//            _ <- ioEnd(a) }
//      yield c
//
//    def enumFile[A](f: File, i: Consumer[String, A]): IO[Consumer[String, A]] =
//      bracket(bufferFile(f), enumReader(_:BufferedReader, i), closeReader(_:BufferedReader))


    val result = BufferedReaderIO.enumerateFile(new File("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv"), head)

    println(result.evaluate)

  }


  test("csv reader") {
    import ConsumerOperation._
    case class Container(manufacturer: String = "123",
                         series: String = "123",
                         modelNumber: String = "123",
                         acPower: String = "",
                         approvedDate: String,
                         expiredDate: String
                          )

    def f(x: StringArray): Container = Container(x(0), x(1), x(2), x(3), x(4), x(5))

    def combine[E, T](y: Consumer[E, Seq[T]], x: Consumer[E,Unit]): Consumer[E, Seq[T]] = {
      for {
        e <- x
        t <- y
      } yield t
    }


    val result = CsvIO.enumerateFile(new File(
      "/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Short.csv"),
      combine(map(f), drop(1)))


    println(result.evaluate.run)

  }

  test("javax validation") {


  }


}

