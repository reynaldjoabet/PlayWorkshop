# PlayWorkshop

routes file in config produces the following

```scala
// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.sbt.routes.JavascriptBuild
import _root_.calculatenifrontend.controllers.Binders._

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:7
  HomeController_0: controllers.HomeController,
  // @LINE:10
  Assets_1: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

  @javax.inject.Inject()
  def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:7
    HomeController_0: controllers.HomeController,
    // @LINE:10
    Assets_1: controllers.Assets
  ) = this(errorHandler, HomeController_0, Assets_1, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_0, Assets_1, prefix)
  }

  private val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    Nil
  ).foldLeft(Seq.empty[(String, String, String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String, String, String)]
    case l => s ++ l.asInstanceOf[List[(String, String, String)]]
  }}


  // @LINE:7
  private lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_0.index(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:10
  private lazy val controllers_Assets_versioned1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""", encodeable=false)))
  )
  private lazy val controllers_Assets_versioned1_invoker = createInvoker(
    Assets_1.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:7
    case controllers_HomeController_index0_route(params@_) =>
      call { 
        controllers_HomeController_index0_invoker.call(HomeController_0.index())
      }
  
    // @LINE:10
    case controllers_Assets_versioned1_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned1_invoker.call(Assets_1.versioned(path, file))
      }
  }
}
```

You might want clean URL separation:

- `/api/hello` for API calls

- `/assets/stylesheets/main.css` for frontend

- `/` for home page

In `conf/routes`, you can use `prefix groups`:

```sh
# Homepage
GET     /                         controllers.HomeController.index

# API Routes
->      /api                      api.Routes

# Static Assets
GET     /assets/*file              controllers.Assets.versioned(path="/public", file: Asset)

```

and then in `conf/api.routes`, create another route file:

```routes

# API sub-routes (conf/api.routes)

GET     /hello                    controllers.api.ApiController.hello

```


```sh

Browser requests --> /assets/stylesheets/main.css
                           |
                           v
routes -> controllers.Assets.versioned("stylesheets/main.css")
                           |
                           v
AssetsBuilder finds file in /public/stylesheets/main.css
                           |
                           v
Returns file content with proper HTTP headers (Cache-Control, ETag, etc.)
```


Open the file `~/.sbt/1.0/global.sbt` using your editor

```bash
mkdir -p ~/.sbt/1.0
vi ~/.sbt/1.0/global.sbt

//Metals requires the semanticdb compiler plugin

Global / semanticdbEnabled := true


Global / semanticdbVersion := "4.13.4"
```


A class is generated for each route file
located at target/scala-3.3.3/routes/main/router/Routes.scala for `routes` file in `conf` folder


```code 
# Routes
# This file defines all application routes (Higher priority routes first)
# https://www.playframework.com/documentation/latest/ScalaRouting
# ~~~~

###################################################################################################
# point /api/v2 to v2.routes file in same directory. This is a soft link to the routes file
# generated by openapi under src/main/java/conf.
# API clients can invoke the v1 API using the /api/v1 route, and the v2 API using /api/v2 route.
###################################################################################################
#-> /api v1.Routes
#-> / v2.Routes
#-> /api/v1 v1.Routes
->         /v1/lineage        controllers.v1.LineageRouter
# An example controller showing a sample home page
GET     /                           controllers.HomeController.index()

# Authentication Routes
GET     /auth/authenticate                                              @controllers.AuthController.authenticate
GET     /auth/callback                                                  @controllers.AuthController.callback(code: String)
GET     /auth/signout                                                   @controllers.AuthController.signOut
POST    /auth/signIn                                                    @controllers.AuthController.signIn(redirect:String ?= "")
GET     /auth/generateAPIKey                                            @controllers.AuthController.generateAPIKey(userId:Long ?= -1)
POST    /auth/resetAllAPIKeys                                           @controllers.AuthController.resetAllAPIKeys
# Map static resources from the /public folder to the /assets URL path
GET     /assets/*file               controllers.Assets.versioned(path="/public", file: Asset)


#->   /api/v2                                       generated.Routes

#GET  /*path/                     @controllers.Application.untrail(path:String)


#
# Static resources.
#
###################################################################################################

# Map static resources from the /public folder to the /assets URL path
GET     /                           controllers.UIController.index()
GET     /$resource<api.*>           controllers.UIController.unknown(resource)
GET     /*file                      controllers.UIController.assetOrDefault(file)

```


produces 
```scala

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:14
  controllers_v1_LineageRouter_0: controllers.v1.LineageRouter,
  // @LINE:16
  HomeController_0: controllers.HomeController,
  // @LINE:19
  AuthController_1: javax.inject.Provider[controllers.AuthController],
  // @LINE:26
  Assets_2: controllers.Assets,
  // @LINE:40
  UIController_3: controllers.UIController,
  val prefix: String
) extends GeneratedRouter {
}
```

## Default Routing in Play Framework
Play Framework uses the conf/routes file to define routes. Each route maps an incoming HTTP request (like GET, POST, PUT) to a specific controller and action.


```code
GET   /users/:id   controllers.UserController.getUser(id: String)
POST  /users       controllers.UserController.createUser
```

You need to register your custom router in the Play Framework application.

```code
play.http.router = "router.CustomRouter"
```


```scala

  /**
   * Generates an `Action` that serves a static resource, using the base asset path from configuration.
   */
  def at(file: String): Action[AnyContent] = at(finder.assetsBasePath, file)

  /**
   * Generates an `Action` that serves a versioned static resource, using the base asset path from configuration.
   */
  def versioned(file: String): Action[AnyContent] = versioned(finder.assetsBasePath, Asset(file))

  /**
   * Generates an `Action` that serves a versioned static resource.
   */
  def versioned(path: String, file: Asset): Action[AnyContent] = Action.async { implicit request =>
    val f = new File(file.name)
    // We want to detect if it's a fingerprinted asset, because if it's fingerprinted, we can aggressively cache it,
    // otherwise we can't.
    val requestedDigest = f.getName.takeWhile(_ != '-')
    if (!requestedDigest.isEmpty) {
      val bareFile     = new File(f.getParent, f.getName.drop(requestedDigest.length + 1)).getPath.replace('\\', '/')
      val bareFullPath = path + "/" + bareFile
      blocking(digest(bareFullPath)) match {
        case Some(`requestedDigest`) => assetAt(path, bareFile, aggressiveCaching = true)
        case _                       => assetAt(path, file.name, aggressiveCaching = false)
      }
    } else {
      assetAt(path, file.name, aggressiveCaching = false)
    }
  }

  /**
   * Generates an `Action` that serves a static resource.
   *
   * @param path the root folder for searching the static resource files, such as `"/public"`. Not URL encoded.
   * @param file the file part extracted from the URL. May be URL encoded (note that %2F decodes to literal /).
   * @param aggressiveCaching if true then an aggressive set of caching directives will be used. Defaults to false.
   */
  def at(path: String, file: String, aggressiveCaching: Boolean = false): Action[AnyContent] = Action.async {
    implicit request => assetAt(path, file, aggressiveCaching)
  }

  private def assetAt(path: String, file: String, aggressiveCaching: Boolean)(
      implicit request: RequestHeader
  ): Future[Result] = {
    val assetName: Option[String] = resourceNameAt(path, file)
    val assetInfoFuture: Future[Option[(AssetInfo, AcceptEncoding)]] = assetName
      .map { name => assetInfoForRequest(request, name) }
      .getOrElse(Future.successful(None))

    def notFound = errorHandler.onClientError(request, NOT_FOUND, "Resource not found by Assets controller")

    val pendingResult: Future[Result] = assetInfoFuture.flatMap {
      case Some((assetInfo, acceptEncoding)) =>
        val connection = assetInfo.url(acceptEncoding).openConnection()
        // Make sure it's not a directory
        if (Resources.isUrlConnectionADirectory(if (env != null) env.classLoader else null, connection)) {
          Resources.closeUrlConnection(connection)
          notFound
        } else {
          val stream = connection.getInputStream
          val source = StreamConverters.fromInputStream(() => stream)
          // FIXME stream.available does not necessarily return the length of the file. According to the docs "It is never
          // correct to use the return value of this method to allocate a buffer intended to hold all data in this stream."
          val result = RangeResult.ofSource(
            stream.available(),
            source,
            request.headers.get(RANGE),
            None,
            Option(assetInfo.mimeType)
          )

          Future.successful(maybeNotModified(request, assetInfo, aggressiveCaching).getOrElse {
            cacheableResult(
              assetInfo,
              aggressiveCaching,
              asEncodedResult(result, acceptEncoding, assetInfo)
            )
          })
        }
      case None => notFound
    }

    pendingResult.recoverWith {
      case e: InvalidUriEncodingException =>
        errorHandler.onClientError(request, BAD_REQUEST, s"Invalid URI encoding for $file at $path: " + e.getMessage)
      case NonFatal(e) =>
        // Add a bit more information to the exception for better error reporting later
        errorHandler.onServerError(
          request,
          new RuntimeException(s"Unexpected error while serving $file at $path: " + e.getMessage, e)
        )
    }
  }
  ```

 `main-445522.js` is fingerprinted and  `445522` is a hash of the content of `main.js`

 `GET /assets/main-ab12cd34.css` calls `Assets.versioned("/public", Asset("main-ab12cd34.css"))`

 So it adds "aggressive" caching headers, like:

 ```http
 Cache-Control: public, max-age=31536000, immutable
```

Normal (forward) routing maps:
`URL → Controller`

Reverse routing maps:
`Controller → URL`

```
# routes file
GET     /user/:id         controllers.UserController.show(id: Long)

```

Reverse route in Scala template:

```sh
@routes.UserController.show(42)
#/user/42
#You can use that to create links:
```


```scala
/**
 * Default implementation of [[AssetsMetadata]].
 *
 * If your application uses reverse routing with assets or the [[Assets]] static object, you should use the
 * [[AssetsMetadataProvider]] to set up needed statics.
 */
@Singleton
class DefaultAssetsMetadata(
    config: AssetsConfiguration,
    resource: String => Option[URL],
    fileMimeTypes: FileMimeTypes
) extends AssetsMetadata {
  @Inject
  def this(env: Environment, config: AssetsConfiguration, fileMimeTypes: FileMimeTypes) =
    this(config, env.resource _, fileMimeTypes)

  lazy val finder: AssetsFinder = new AssetsFinder {
    val assetsBasePath  = config.path
    val assetsUrlPrefix = config.urlPrefix

    def findAssetPath(base: String, path: String): String = blocking {
      val minPath = minifiedPath(path)
      digest(minPath)
        .fold(minPath) { dgst =>
          val lastSep = minPath.lastIndexOf("/")
          minPath.take(lastSep + 1) + dgst + "-" + minPath.drop(lastSep + 1)
        }
        .drop(base.length + 1)
    }
  }
}



@Singleton
class Assets @Inject() (errorHandler: HttpErrorHandler, meta: AssetsMetadata, env: Environment)
    extends AssetsBuilder(errorHandler, meta, env) {
  @deprecated("Use Assets(errorHandler,meta,env) instead.", "2.9")
  def this(errorHandler: HttpErrorHandler, meta: AssetsMetadata) = {
    this(errorHandler, meta, null)
  }
}
```


You can tell the routes file to use a different router under a specific prefix by using “->” followed by the given prefix:

`->      /api                        api.MyRouter`
This is especially useful when combined with String Interpolating Routing DSL also known as SIRD routing, or when working with sub projects that route using several routes files.

->      /api                        api.MyRouter


For parameters of type `String`, typing the parameter is optional. If you want Play to transform the incoming parameter into a specific Scala type, you can explicitly type the parameter:

GET   /clients/:id          controllers.Clients.show(id: Long)


Play supports the following Parameter Types:

- String
- Int
- Long
- Double
- Float
- Boolean
- UUID
- AnyVal wrappers for other supported types



```scala

/**
 * An `EssentialAction` underlies every `Action`. Given a `RequestHeader`, an
 * `EssentialAction` consumes the request body (an `ByteString`) and returns
 * a `Result`.
 *
 * An `EssentialAction` is a `Handler`, which means it is one of the objects
 * that Play uses to handle requests.
 */
trait EssentialAction extends (RequestHeader => Accumulator[ByteString, Result]) with Handler { self =>

  /** @return itself, for better support in the routes file. */
  def apply(): EssentialAction = this

  def asJava: play.mvc.EssentialAction = new play.mvc.EssentialAction() {
    def apply(rh: play.mvc.Http.RequestHeader) = self(rh.asScala).map(_.asJava)(Execution.trampoline).asJava
    override def apply(rh: RequestHeader)      = self(rh)
  }
}

/**
 * An action is essentially a (Request[A] => Result) function that
 * handles a request and generates a result to be sent to the client.
 *
 * For example,
 * {{{
 * val echo = Action { request =>
 *   Ok("Got request [" + request + "]")
 * }
 * }}}
 *
 * @tparam A the type of the request body
 */
trait Action[A] extends EssentialAction {
  private lazy val logger = Logger(getClass)

  /** Type of the request body. */
  type BODY_CONTENT = A

  /** Body parser associated with this action. */
  def parser: BodyParser[A]

  /**
   * Invokes this action.
   *
   * @param request the incoming HTTP request
   * @return the result to be sent to the client
   */
  def apply(request: Request[A]): Future[Result]

  def apply(rh: RequestHeader): Accumulator[ByteString, Result]
}
```

an `Action[A]` uses a `BodyParser[A]` to retrieve a value of type `A` from the HTTP request, and to build a `Request[A]` object that is passed to the action code.


```scala
/**
 * A body parser parses the HTTP request body content.
 *
 * @tparam A the body content type
 */
trait BodyParser[+A] extends (RequestHeader => Accumulator[ByteString, Either[Result, A]]) {

}
```

Since BodyParser is a function, we can write 

```scala
import org.apache.pekko.stream.scaladsl._
import org.apache.pekko.util.ByteString
import play.api.libs.streams._
import play.api.mvc.BodyParser

val Action = inject[DefaultActionBuilder]

val csv: BodyParser[Seq[Seq[String]]] = BodyParser { req =>
  // A flow that splits the stream into CSV lines
  val sink: Sink[ByteString, Future[Seq[Seq[String]]]] = Flow[ByteString]
    // We split by the new line character, allowing a maximum of 1000 characters per line
    .via(Framing.delimiter(ByteString("\n"), 1000, allowTruncation = true))
    // Turn each line to a String and split it by commas
    .map(_.utf8String.trim.split(",").toSeq)
    // Now we fold it into a list
    .toMat(Sink.fold(Seq.empty[Seq[String]])(_ :+ _))(Keep.right)

  // Convert the body to a Right either
  Accumulator(sink).map(Right.apply)
}
```

In a Play Framework Scala application, `BuiltInComponents` is a trait provided by Play to help you manually wire together your application components without using dependency injection frameworks like Guice.


Compiled assets in Play must be defined in the `app/assets` directory. They are handled by the build process and CoffeeScript sources are compiled into standard JavaScript files. `The generated JavaScript files` are distributed as standard resources into the same `public/` folder as other unmanaged assets, meaning that there is no difference in the way you use them once compiled.

For example a CoffeeScript source file `app/assets/javascripts/main.coffee` will be available as a standard JavaScript resource, at `public/javascripts/main.js`.



```sh
playAggregateReverseRoutes    playDefaultPort               playGenerateSecret            playPrefixAndAssets           playRunHooks
playAllAssets                 playDependencyClasspath       playInteractionMode           playReload                    playStop
playAssetsClassLoader         playDevSettings               playJarSansExternalized       playReloaderClasspath         playUpdateSecret
playAssetsWithCompilation     playExternalizeResources      playMonitoredFiles            playRoutes
playCommonClassloader         playExternalizedResources     playNamespaceReverseRouter    playRoutesGenerator
playCompileEverything         playGenerateJsReverseRouter   playPackageAssets             playRoutesImports
playDefaultAddress            playGenerateReverseRouter     playPlugin                    playRoutesTasks
```


```scala
  object autoImport {
    val publishToEcr = SettingKey[Boolean]("publish-to-ecr", "Flag to enable publishing docker images to ECR")
    // Returns an Option in case e.g. the user doesn't have AWS creds
    val ecrLogin = TaskKey[Option[URL]]("ecr-login", "Login to ECR, returning the URL to the docker registry")
  }

  ```


  import read on domain names and sub domains

  [architecture/guide/multitenant/considerations/domain-names](https://learn.microsoft.com/en-us/azure/architecture/guide/multitenant/considerations/domain-names)

   All of Contoso's tenants might be assigned their own subdomain, under the `contoso.com` domain name. Or, if Contoso uses regional deployments, they might assign subdomains under the `us.contoso.com` and `eu.contoso.com` domains


   For example, Tailwind Toys might be assigned `tailwind.contoso.com`, and in a regional deployment model, Adventure Works might be assigned `adventureworks.us.contoso.com`.


   [architecture/best-practices/host-name-preservation](https://learn.microsoft.com/en-us/azure/architecture/best-practices/host-name-preservation)

   most dns registrars are also dns servers,They run authoritative DNS servers where you can configure DNS records (A, MX, etc.)



   `There is one registry per TLD — not many.`

   Each `Top-Level Domain` (TLD) like `.com`, `.org`, `.net`, `.uk`, etc., is operated by a single registry organization, which holds the authoritative database for all domain names under that TLD. 


Each of these registry operators:

- Maintains the official list of all domains under that TLD.

- Publishes the nameserver info to the DNS root servers.

- Works with many registrars who act as resellers.

✅ One registry per TLD.

✅ Many registrars per registry.


`Yes — the registry operates the TLD-level nameservers, but not the nameservers for individual domains.`


1. Root name server
When a recursive DNS server does not have cached data, it sends a DNS query to the DNS root name server. The root name server accepts the query and forwards it to a top level domain (TLD) name server. Which TLD server the query is forwarded to depends on the desired sites extension: .com, .org or .net, for example. There are 13 main DNS root servers operated by the Internet Corporation for Assigned Names and Numbers (ICANN).


2. Top level domain (TLD) name server
TLD name servers contain data related to domain names with the same extension. This means there are designated TLD servers for websites with the extensions .com, .org and .net. Once the query reaches the correct TLD name server, it is then directed to the authoritative name server.


## Authoritative name server
Generally the final step in the process of retrieving an IP address, authoritative DNS servers store information related to specific domain names in DNS resource records. These DNS records contain information about a specific domain and its corresponding IP address.


registries  are called TLD nameservers, and they:

Respond to queries like:

"Who is the authoritative DNS server for example.com?"


- The domain’s nameservers — the ones that actually store your A, MX, TXT, etc. records — are not owned by the registry. They're usually Provided by your DNS hosting provider (Cloudflare, AWS Route53, etc.)

- Or by your registrar (if you use their DNS service)

- Or self-hosted (if you run your own DNS server)


The registry just stores a reference to your domain’s nameservers. For example:

For `example.com`, the `.com` registry might say:
“`Ask ns1.digitalocean.com` and `ns2.digitalocean.com`.”

When you switch to a third-party DNS provider (like Cloudflare, AWS Route 53, or your own server), you need to:

- Log in to your registrar's control panel

- Go to your domain’s settings

- Update the nameservers to the ones given by your new DNS provider
(e.g., ns1.cloudflare.com, ns2.cloudflare.com)

- The registrar then sends that update to the registry (e.g., Verisign for .com)

The registry updates the TLD DNS servers, so future lookups go to your new nameservers

```sh
Yes — a complete domain registration must include nameservers, but they don’t have to be provided by the registrar. Here's a more detailed breakdown:
```




### What happens during a domain registration?
- You Register the Domain:
When you register a domain, the registrar must specify nameservers for the domain. These nameservers could be:

1. The registrar’s own nameservers (if they offer DNS hosting)

2. External nameservers (like Cloudflare, AWS Route 53, etc.)

- Nameservers are Provided to the TLD Registry:
The registrar sends the nameserver information to the TLD registry (like Verisign for .com).

- TLD Registry Adds the Nameserver Info:
The registry updates its own authoritative nameservers for your domain. This makes it so that DNS queries for your domain are directed to the right nameservers.

One registry → Many TLD servers

The servers like a.gtld-servers.net through m.gtld-servers.net are globally distributed authoritative DNS servers.

They all serve the .com and .net zones, and they are:

- Maintained

- Secured

- Upgraded

- And monitored

...by Verisign, the registry operator.


 anycast: Dozens or hundreds of physical servers around the world share the same IP.

 When your DNS resolver queries a.gtld-servers.net, it’s routed to the nearest or fastest physical server.

 What is Anycast?
Anycast is a network routing technique where multiple physical servers share the same IP address, and the network automatically routes users to the “nearest” server — typically in terms of network distance (latency)


DNS servers — especially root servers and TLD servers 

There are 13 named root servers (a.root-servers.net through m.root-servers.net), but:

- Each name (e.g., j.root-servers.net) is served from hundreds of locations.

- All of them share the same IP (via Anycast).
- So when you query a.root-servers.net, you're routed to the closest one, say in Kenya or Singapore or France — depending on your location.
