import scala.concurrent.Future

import controllers.AssetsBuilder
import controllers.AssetsMetadata
import javax.inject.Inject
import play.api.http.HttpErrorHandler
import play.api.mvc.{RequestHeader, Result}
import play.api.Environment

class AssetsController @Inject() (metaData: AssetsMetadata, env: Environment)
    extends AssetsBuilder(
      new HttpErrorHandler {

        override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] =
          throw new RuntimeException(s"$getClass onServerError", exception) // FIXME

        override def onClientError(
            request: RequestHeader,
            statusCode: Int,
            message: String
        ): Future[Result] =
          throw new RuntimeException(s"$getClass onClientError $statusCode : $message") // FIXME
      },
      metaData,
      env
    )
