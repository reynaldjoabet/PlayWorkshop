package handlers

import scala.concurrent._

import javax.inject._
import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc._
import play.api.routing.Router
//import play.http.DefaultHttpErrorHandler //java api

@Singleton
class CustomErrorHandler @Inject() (
    val env: Environment,
    val config: Configuration,
    val sourceMapper: OptionalSourceMapper,
    val router: Provider[Router]
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onClientError(
      request: RequestHeader,
      statusCode: Int,
      message: String
  ): Future[Result] = {
    Future.successful(
      Results.Status(statusCode)(
        views.html.error(s"Client Error $statusCode", message)
      )
    )
  }

  override def onServerError(
      request: RequestHeader,
      exception: Throwable
  ): Future[Result] = {
    Future.successful(
      Results.InternalServerError(
        views.html.error("Server Error", exception.getMessage)
      )
    )
  }

}
