package controllers

import javax.inject.*
import play.api.libs.json.Json
import play.api.mvc.*
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc.{Action, AnyContent}
import play.api.mvc.{Request, Result}

class LoginController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def login(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Handle the login logic here
    Ok(Json.obj("status" -> "success", "message" -> "Logged in successfully"))
  }

  def logout(): Action[AnyContent] = Action.apply { implicit request: Request[AnyContent] =>
    // Handle the logout logic here
    Ok(Json.obj("status" -> "success", "message" -> "Logged out successfully"))
  }

}
