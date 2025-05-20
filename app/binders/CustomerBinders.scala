package binders

import scala.util.{Failure, Success, Try}

import models.UniqueIdentifier
import play.api.mvc.{PathBindable, QueryStringBindable}

object CustomBinders {

  implicit def pathBindableIdentifier: PathBindable[UniqueIdentifier] =
    new PathBindable[UniqueIdentifier] {

      def bind(key: String, value: String) =
        Try(UniqueIdentifier(value)) match {
          case Success(v) => Right(v)
          case Failure(e: IllegalArgumentException) =>
            Left(s"Badly formatted UniqueIdentifier $value")
          case Failure(e) => throw e
        }

      def unbind(key: String, value: UniqueIdentifier) = value.toString

    }

  implicit def queryBindableIdentifier(implicit
      stringBinder: QueryStringBindable[String]
  ): QueryStringBindable[UniqueIdentifier] =
    new QueryStringBindable[UniqueIdentifier] {

      def bind(key: String, params: Map[String, Seq[String]]) =
        for {
          uuid <- stringBinder.bind(key, params)
        } yield {
          uuid match {
            case Right(value) =>
              Try(UniqueIdentifier(value)) match {
                case Success(v) => Right(v)
                case Failure(e: IllegalArgumentException) =>
                  Left(s"Badly formatted UniqueIdentifier $value")
                case Failure(e) => throw e
              }
            case _ => Left("Bad uuid")
          }
        }

      def unbind(key: String, value: UniqueIdentifier) = stringBinder.unbind(key, value.toString())

    }

}
