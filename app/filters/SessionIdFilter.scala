package filters

import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}

import com.google.inject.Inject
import org.apache.pekko.stream.Materializer
import play.api.mvc._
import play.api.mvc.request.{Cell, RequestAttrKey}

class SessionIdFilter(
    override val mat: Materializer,
    uuid: => UUID,
    implicit val ec: ExecutionContext
) extends Filter {

  @Inject
  def this(mat: Materializer, ec: ExecutionContext) =
    this(mat, UUID.randomUUID(), ec)

  override def apply(f: RequestHeader => Future[Result])(
      rh: RequestHeader
  ): Future[Result] = ???

}
