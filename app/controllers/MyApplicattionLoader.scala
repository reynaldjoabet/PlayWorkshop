package controllers

import play.api._
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.core.server._

class MyApplicationLoader extends ApplicationLoader {

  def load(context: ApplicationLoader.Context): Application = {
    new BuiltInComponentsFromContext(context) {
      // Define a router (e.g., generated Routes)

      override def httpFilters: Seq[EssentialFilter] = Seq.empty

      override def router: Router = Router.empty

    }.application
  }

}
