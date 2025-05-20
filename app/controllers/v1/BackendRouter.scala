package controllers.v1

import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.routing.sird.*
import play.api.routing.SimpleRouter
import router.Routes

class BackendRouter @Inject() () extends SimpleRouter {

  override def routes: PartialFunction[play.api.mvc.RequestHeader, play.api.mvc.Handler] = {
    case GET(p"/backend")        => getBackend
    case POST(p"/backend")       => postBackend
    case DELETE(p"/backend/$id") => deleteBackend(id)
  }

  def getBackend: Action[AnyContent]                = ???
  def postBackend: Action[AnyContent]               = ???
  def deleteBackend(id: String): Action[AnyContent] = ???

}
