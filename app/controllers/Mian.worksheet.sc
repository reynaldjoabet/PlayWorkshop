import java.io.File

import scala.concurrent.Future

import play.api.mvc.BodyParsers

//file should be fingerprinted
val file = new File("main-3313")

file.getParent()

file.getName()

file.getName().takeWhile(_ != '-') // this gives the file

trait EssentialAction extends (Int => String) {}

object EssentialAction {
  def apply(f: Int => String): Int => String = f(_)
}

import javax.inject.Inject
import org.apache.pekko.stream.scaladsl._
import org.apache.pekko.util.ByteString
import play.api.libs.streams._
import play.api.mvc.BodyParser

//val Action = inject[DefaultActionBuilder]

implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

val csv: BodyParser[Seq[Seq[String]]] = BodyParser { req =>
  // A flow that splits the stream into CSV lines
  val sink: Sink[ByteString, Future[Seq[Seq[String]]]] = Flow[ByteString]
    // We split by the new line character, allowing a maximum of 1000 characters per line
    .via(Framing.delimiter(ByteString("\n"), 1000, allowTruncation = true))
    // Turn each line to a String and split it by commas
    .map(_.utf8String.trim.split(",").toSeq)
    // Now we fold it into a list
    .toMat(Sink.fold(Seq.empty[Seq[String]])(_ :+ _))(Keep.right)

  // Convert the body to a Right either
  Accumulator(sink).map(Right.apply)
}

BodyParsers.Default
