package controllers.v1

import com.google.inject.Inject
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.routing.sird.*
import play.api.routing.Router
import play.api.routing.SimpleRouter
import router.Routes

class UiRouter @Inject() () extends SimpleRouter {

  override def routes: Router.Routes = ???
}
