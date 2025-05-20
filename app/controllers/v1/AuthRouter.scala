package controllers.v1

import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Security.Authenticated
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc.Security.AuthenticatedRequest
import play.api.mvc.Session
import play.api.routing.sird.*
import play.api.routing.SimpleRouter
import router.Routes

class AuthRouter @Inject() () extends SimpleRouter {

  override def routes: PartialFunction[play.api.mvc.RequestHeader, play.api.mvc.Handler] = {
    case GET(p"/auth")        => getAuth
    case POST(p"/auth")       => postAuth
    case DELETE(p"/auth/$id") => deleteAuth(id)
  }

  def getAuth: Action[AnyContent]                = ???
  def postAuth: Action[AnyContent]               = ???
  def deleteAuth(id: String): Action[AnyContent] = ???

}
