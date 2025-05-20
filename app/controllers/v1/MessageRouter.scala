package controllers.v1

import controllers.Assets
import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.routing.sird.*
import play.api.routing.Router
//import router.Routes
import play.api.routing.SimpleRouter
//import jakarta.inject.Inject

class MessageRouter @Inject() (assets: Assets) extends SimpleRouter {

  override def routes: Router.Routes = {
    case GET(p"/messages")        => getMessages
    case POST(p"/messages")       => postMessage
    case DELETE(p"/messages/$id") => deleteMessage(id)
  }

  def getMessages: Action[AnyContent]               = ???
  def postMessage: Action[AnyContent]               = ???
  def deleteMessage(id: String): Action[AnyContent] = ???

  val router = Router.from { case GET(p"/assets/$file*") =>
    assets.versioned(path = "/public", file = file)
  }

}
