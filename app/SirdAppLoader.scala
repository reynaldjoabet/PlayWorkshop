import play.api.routing.sird._
import play.api.routing.Router
import play.api.ApplicationLoader
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.Mode
import play.api.NoHttpFiltersComponents

class SirdAppLoader extends ApplicationLoader {

  def load(context: Context) =
    // new SirdComponents(context).application
    ???

}

class SirdComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with NoHttpFiltersComponents
    with play.api.mvc.Results {

  lazy val router = Router.from { case GET(p"/hello/$to") =>
    Action {
      Ok(s"Hello $to")
    }
  }

}
