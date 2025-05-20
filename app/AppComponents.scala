
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

//import com.gu.googleauth.AuthAction
import controllers._
import filters.HstsFilter
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.actor.ClassicActorSystemProvider
import org.apache.pekko.actor.CoordinatedShutdown
import org.apache.pekko.stream.Materializer
import play.api.{BuiltInComponentsFromContext, Logger}
import play.api.http.DefaultHttpErrorHandler
import play.api.http.FileMimeTypes
import play.api.http.HttpConfiguration
import play.api.http.HttpErrorHandler
import play.api.http.HttpRequestHandler
import play.api.i18n.I18nComponents
import play.api.i18n.MessagesApi
import play.api.inject.ApplicationLifecycle
import play.api.libs.crypto.CSRFTokenSigner
import play.api.libs.crypto.CookieSigner
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.libs.ws.StandaloneWSClient
import play.api.libs.ws.WSClient
import play.api.libs.Files.TemporaryFileCreator
import play.api.mvc.{AnyContent, RequestHeader, Result}
import play.api.mvc.request.RequestFactory
import play.api.mvc.BodyParser
import play.api.mvc.ControllerComponents
import play.api.mvc.EssentialFilter
import play.api.mvc.PlayBodyParsers
import play.api.mvc.Results.InternalServerError
import play.api.routing.Router
import play.api.Application
//import persistence.ScheduleRepository
import play.api.ApplicationLoader.Context
import play.api.Configuration
import play.api.Environment
import play.api.Logging
import play.core.server.ssl.noCATrustManager
import play.core.server.ssl.DefaultSSLEngineProvider
import play.core.server.ssl.ServerSSLEngine
//import schedule.DeployScheduler
import play.core.server.PekkoHttpServerProvider
import play.core.server.ServerConfig
import play.filters.components.AllowedHostsComponents
import play.filters.components.CSPComponents
import play.filters.components.CSPReportComponents
import play.filters.components.HttpFiltersComponents
import play.filters.components.IPFilterComponents
import play.filters.components.NoHttpFiltersComponents
import play.filters.components.RedirectHttpsComponents
import play.filters.cors.CORSActionBuilder
import play.filters.cors.CORSComponents
import play.filters.cors.CORSConfig
import play.filters.cors.CORSConfigProvider
import play.filters.cors.CORSFilter
import play.filters.cors.CORSModule
import play.filters.csp.CSPModule
import play.filters.csrf.CSRF.ErrorHandler
import play.filters.csrf.CSRF.TokenProvider
import play.filters.csrf.CSRFAddToken
import play.filters.csrf.CSRFCheck
import play.filters.csrf.CSRFComponents
import play.filters.csrf.CSRFConfig
import play.filters.csrf.CSRFFilter
import play.filters.gzip.GzipFilter
import play.filters.gzip.GzipFilterComponents
import play.filters.gzip.GzipFilterConfig
import play.filters.headers.SecurityHeadersComponents
import play.filters.headers.SecurityHeadersConfig
import play.filters.headers.SecurityHeadersConfigProvider
import play.filters.headers.SecurityHeadersFilter
import play.filters.headers.SecurityHeadersModule
import play.filters.hosts.AllowedHostsComponents
import play.filters.hosts.AllowedHostsConfig
import play.filters.hosts.AllowedHostsConfigProvider
import play.filters.hosts.AllowedHostsFilter
import play.filters.hosts.AllowedHostsModule
import play.filters.https.RedirectHttpsComponents
import play.filters.https.RedirectHttpsConfiguration
import play.filters.https.RedirectHttpsConfigurationProvider
import play.filters.https.RedirectHttpsFilter
import play.filters.https.RedirectHttpsModule
import play.filters.ip.IPFilter
import play.filters.ip.IPFilterComponents
import play.filters.ip.IPFilterConfig
import play.filters.ip.IPFilterConfigProvider
import play.filters.ip.IPFilterModule
import play.filters.HttpFiltersComponents
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient
import router.Routes

class AppComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with AhcWSComponents
    with I18nComponents
    with CSRFComponents
    with GzipFilterComponents
    with AssetsComponents
    with Logging {

  override lazy val asyncHttpClient: AsyncHttpClient = ???

  override lazy val standaloneWSClient: StandaloneWSClient = ???

  implicit val implicitMessagesApi: MessagesApi = messagesApi
  // implicit val implicitWsClient: WSClient = wsClient

//   val availableDeploymentTypes = Seq(
//     ElasticSearch, S3, AutoScaling, Fastly, CloudFormation, Lambda, AmiCloudFormationParameter, SelfDeploy
//   )
//   val prismLookup = new PrismLookup(wsClient, conf.Configuration.lookup.prismUrl, conf.Configuration.lookup.timeoutSeconds.seconds)
//   val deploymentEngine = new DeploymentEngine(prismLookup, availableDeploymentTypes, conf.Configuration.deprecation.pauseSeconds)
//   val buildPoller = new CIBuildPoller(executionContext)
//   val builds = new Builds(buildPoller)
//   val targetResolver = new TargetResolver(buildPoller, availableDeploymentTypes)
//   val deployments = new Deployments(deploymentEngine, builds)
//   val continuousDeployment = new ContinuousDeployment(buildPoller, deployments)
//   val previewCoordinator = new PreviewCoordinator(prismLookup, availableDeploymentTypes)

//   val authAction = new AuthAction[AnyContent](
//     conf.Configuration.auth.googleAuthConfig, routes.Login.loginAction(), controllerComponents.parsers.default)(executionContext)

  override lazy val httpFilters: Seq[EssentialFilter] = Seq(
    csrfFilter,
    gzipFilter,
    new HstsFilter()(executionContext)
  ) // TODO (this would require an upgrade of the management-play lib) ++ PlayRequestMetrics.asFilters

//   val deployScheduler = new DeployScheduler(deployments)
//   log.info("Starting deployment scheduler")
//   deployScheduler.start()
//   applicationLifecycle.addStopHook { () =>
//     log.info("Shutting down deployment scheduler")
//     Future.successful(deployScheduler.shutdown())
//   }
//   deployScheduler.initialise(ScheduleRepository.getScheduleList())

//   val applicationController = new Application(prismLookup, availableDeploymentTypes, authAction, controllerComponents, assets)(environment, wsClient, executionContext)
//   val deployController = new DeployController(deployments, prismLookup, availableDeploymentTypes, builds, authAction, controllerComponents)
//   val apiController = new Api(deployments, availableDeploymentTypes, authAction, controllerComponents)
//   val continuousDeployController = new ContinuousDeployController(prismLookup, authAction, controllerComponents)
//   val previewController = new PreviewController(previewCoordinator, authAction, controllerComponents)(wsClient, executionContext)
//   val hooksController = new HooksController(prismLookup, authAction, controllerComponents)
//   val restrictionsController = new Restrictions(authAction, controllerComponents)
//   val scheduleController = new ScheduleController(authAction, controllerComponents, prismLookup, deployScheduler)
//   val targetController = new TargetController(deployments, authAction, controllerComponents)
//   val loginController = new Login(deployments, controllerComponents, authAction)
//   val testingController = new Testing(prismLookup, authAction, controllerComponents)

  val homeController = new HomeController(controllerComponents)
//   override lazy val httpErrorHandler = new DefaultHttpErrorHandler(environment, configuration, sourceMapper, Some(router)) {
//     override def onServerError(request: RequestHeader, t: Throwable): Future[Result] = {
//       Logger.error("Error whilst trying to serve request", t)
//       val reportException = if (t.getCause != null) t.getCause else t
//       Future.successful(InternalServerError(views.html.errorPage(reportException)))
//     }
//   }

  val authController = new AuthController(controllerComponents, wsClient)

  override def router: Router                             = ???
  override def applicationLifecycle: ApplicationLifecycle = ???

  override def configuration: Configuration = ???

  // override def csrfTokenSigner: CSRFTokenSigner = ???

  override def environment: Environment = ???

  // override def fileMimeTypes: FileMimeTypes = ???

  override lazy val playBodyParsers: PlayBodyParsers = ???

  override lazy val messagesApi: MessagesApi = ???

  override protected val logger: Logger = ???

  override lazy val gzipFilterConfig: GzipFilterConfig = ???

  override lazy val gzipFilter: GzipFilter = ???

  override lazy val fileMimeTypes: FileMimeTypes = ???

  override lazy val defaultBodyParser: BodyParser[AnyContent] = ???

  override lazy val httpConfiguration: HttpConfiguration = ???

  override lazy val csrfTokenSigner: CSRFTokenSigner = ???

  override protected def parse: PlayBodyParsers = ???

  override lazy val csrfTokenProvider: TokenProvider = ???

  override lazy val csrfFilter: CSRFFilter = ???

  override lazy val csrfErrorHandler: ErrorHandler = ???

  override lazy val csrfConfig: CSRFConfig = ???

  override lazy val csrfCheck: CSRFCheck = ???

  override lazy val csrfAddToken: CSRFAddToken = ???

  override lazy val cookieSigner: CookieSigner = ???

  override lazy val coordinatedShutdown: CoordinatedShutdown = ???

  override lazy val controllerComponents: ControllerComponents = ???

  override lazy val classicActorSystemProvider: ClassicActorSystemProvider = ???

  override lazy val httpErrorHandler: HttpErrorHandler = ???

  override lazy val tempFileCreator: TemporaryFileCreator = ???

  override lazy val httpRequestHandler: HttpRequestHandler = ???

  override lazy val requestFactory: RequestFactory = ???

  // override lazy val assetsMetadata: AssetsMetadata = ???

  // override def assetsFinder: AssetsFinder =  ???

  // override lazy val assetsConfiguration: AssetsConfiguration = ???

  // override lazy val assets: Assets = ???

  override lazy val application: Application = ???

  override lazy val actorSystem: ActorSystem = ???

}
