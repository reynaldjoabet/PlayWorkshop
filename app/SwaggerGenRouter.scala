package controllers

import controllers.javascript.ReverseAssets
import controllers.javascript.ReverseAuthController
import controllers.javascript.ReverseHomeController
import controllers.javascript.ReverseUIController
import controllers.Default
import controllers.ReverseAuthController
import javax.inject.{Inject, Provider, Singleton}
import play.api.http.HttpConfiguration
import play.api.routing.{Router, SimpleRouter}
import play.api.routing.sird.{GET, _}
import play.api.routing.JavaScriptReverseRouter
import play.api.routing.Router.Routes

//private class SwaggerGenRouter @Inject() (controller: controllers.ApiHelpController) extends SimpleRouter {
private class SwaggerGenRouter @Inject() (controller: controllers.Default) extends SimpleRouter {

  override def routes: Routes = {
    case GET(p"/dynamic_swagger.json")      => controller.redirect("") // controller.getResources
    case GET(p"/docs/dynamic_swagger.json") => controller.todo         // controller.getResources
  }

}

class AppRouter @Inject() (swaggerGenRouter1: SwaggerGenRouter, prodRouter: router.Routes)
    extends SimpleRouter {
  // Composes both routers with spaRouter having precedence.
  override def routes: Routes = swaggerGenRouter1.routes.orElse(prodRouter.routes)
}

@Singleton
class SwaggerGenRoutesProvider @Inject() (
    val swaggerGenRouter: AppRouter,
    val httpConfig: HttpConfiguration
) extends Provider[Router] {
  override def get: Router = swaggerGenRouter.withPrefix(httpConfig.context)
}
