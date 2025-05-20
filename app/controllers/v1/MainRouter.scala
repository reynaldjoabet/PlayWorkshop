package controllers.v1

import javax.inject.Inject
import javax.inject.Singleton
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.routing.sird.*
import play.api.routing.Router
import play.api.routing.SimpleRouter
import router.Routes
import router.RoutesPrefix

@Singleton
class MainRouter @Inject() (
    // uptime: UptimeController,
    authRouter: AuthRouter,
    backendRouter: BackendRouter,
    jobsRouter: JobsRouter,
    uiRouter: UiRouter,
    messageRouter: MessageRouter
) extends SimpleRouter {

  private lazy val mainRoutes: Router.Routes = {
    case GET(p"/uptime")    => ??? // uptime.uptime
    case GET(p"/buildinfo") => ??? // uptime.buildInfo
  }

  override lazy val routes: Router.Routes = {
    mainRoutes
      .orElse(messageRouter.withPrefix("/ws").routes)
      .orElse(uiRouter.withPrefix("/api/tools").routes)
      .orElse(jobsRouter.withPrefix("/api/jobs").routes)
      .orElse(backendRouter.withPrefix("/api/backend").routes)
      .orElse(authRouter.withPrefix("/api/auth").routes)
  }

}
