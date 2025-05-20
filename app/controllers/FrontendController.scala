package controllers

import javax.inject._
import play.api._
import play.api.mvc._

final class FrontendController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("Hello, world!")
  }

}
