package controllers

import scala.concurrent.Future
import scala.io.Source

import config.ApplicationConfig
import javax.inject._
import javax.inject.Inject
import org.apache.pekko.stream.Materializer
import play.api._
import play.api.mvc._
import play.mvc.Action

class TemplateRendererController @Inject() (
    val controllerComponents: MessagesControllerComponents,
    config: ApplicationConfig
) extends BaseController {

  def serveMustacheTemplate() = Action.async { implicit request =>

    val tpl = if (config.environment == "dev") {
      Source
        .fromInputStream(
          getClass.getResourceAsStream("/govuk-template.mustache.html")
        )
        .mkString
        .replaceAll(
          """href="/contact""",
          """href="http://localhost:9250/contact"""
        )
        .replaceAll(
          """href="/template""",
          """href="http://localhost:9310/template"""
        )
        .replaceAll(
          """src="/template""",
          """src="http://localhost:9310/template"""
        )
        .replaceAll(
          """href="/assets""",
          """href="http://localhost:9032/assets"""
        )
        .replaceAll("""src="/assets""", """src="http://localhost:9032/assets""")
        .replaceAll(
          """href="/personal-account""",
          """href="http://localhost:9232/personal-account"""
        )
        .replaceAll("""href="/track""", """href="http://localhost:9100/track""")
        .replaceAll(
          """href="/business-account""",
          """href="http://localhost:9020/business-account"""
        )
        .replaceAll(
          """href="/trusted-helpers""",
          """href="http://localhost:9231/trusted-helpers"""
        )
        .replaceAll(
          """href="/contact""",
          """href="http://localhost:9250/contact"""
        )
    } else {
      Source
        .fromInputStream(
          getClass.getResourceAsStream("/template.mustache.html")
        )
        .mkString
    }

    Future.successful(Ok(tpl).as("text/html"))
  }

}
