package controllers.v1

import javax.inject.{Inject, Singleton}
import javax.inject.Provider
import play.api.http.HttpConfiguration
import play.api.inject
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationLoader
import play.api.inject.guice.GuiceableModule
import play.api.mvc.DefaultActionBuilder
import play.api.mvc.Results
import play.api.routing.sird.*
import play.api.routing.Router
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.ApplicationLoader

class ScalaSimpleRouter @Inject() (val Action: DefaultActionBuilder) extends SimpleRouter {

  override def routes: Routes = { case GET(p"/") =>
    Action {
      Results.Ok
    }
  }

}

@Singleton
class ScalaRoutesProvider @Inject() (
    playSimpleRouter: ScalaSimpleRouter,
    httpConfig: HttpConfiguration
) extends Provider[Router] {
  lazy val get = playSimpleRouter.withPrefix(httpConfig.context)
}

class ScalaGuiceAppLoader extends GuiceApplicationLoader {

  override protected def overrides(context: ApplicationLoader.Context): Seq[GuiceableModule] =
    super.overrides(context) :+ (bind[Router].toProvider[ScalaRoutesProvider]: GuiceableModule)

}
