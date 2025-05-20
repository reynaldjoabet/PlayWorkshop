package controllers.v1

import com.google.inject.Inject
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.routing.sird.*
import play.api.routing.SimpleRouter
import router.Routes

class JobsRouter @Inject() () extends SimpleRouter {

  override def routes: PartialFunction[play.api.mvc.RequestHeader, play.api.mvc.Handler] = {
    case GET(p"/jobs")        => getJobs
    case POST(p"/jobs")       => postJob
    case DELETE(p"/jobs/$id") => deleteJob(id)
  }

  def getJobs: Action[AnyContent]               = ???
  def postJob: Action[AnyContent]               = ???
  def deleteJob(id: String): Action[AnyContent] = ???

}
