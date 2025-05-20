package controllers.v1

import com.google.inject.Inject
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents

class LineageController @Inject() (
    cc: ControllerComponents
) extends AbstractController(cc) {

  def table(name: String, bw: Option[String], fw: Option[String]) = Action.apply {
    implicit request: play.api.mvc.Request[play.api.mvc.AnyContent] =>
      Ok(s"Table name: $name, bw: ${bw.getOrElse("None")}, fw: ${fw.getOrElse("None")}")
  }

}
