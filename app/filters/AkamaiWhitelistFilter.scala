package filters

import scala.concurrent.Future

import play.api.mvc.{Call, Filter, RequestHeader, Result}
import play.api.mvc.Results._

trait AkamaiWhitelistFilter extends Filter {

  val trueClient = "True-Client-IP"

  private def isCircularDestination(requestHeader: RequestHeader): Boolean =
    requestHeader.uri == destination.url

  private def toCall(rh: RequestHeader): Call =
    Call(rh.method, rh.uri)

  def whitelist: Seq[String]

  def excludedPaths: Seq[Call] = Seq.empty

  def destination: Call

  def noHeaderAction(
      f: (RequestHeader) => Future[Result],
      rh: RequestHeader
  ): Future[Result] = Future.successful(NotImplemented)

  override def apply(
      f: (RequestHeader) => Future[Result]
  )(rh: RequestHeader): Future[Result] =
    if (excludedPaths contains toCall(rh)) {
      f(rh)
    } else {
      rh.headers
        .get(trueClient)
        .map { ip =>
          if (whitelist.contains(ip))
            f(rh)
          else if (isCircularDestination(rh))
            Future.successful(Forbidden)
          else
            Future.successful(Redirect(destination))
        }
        .getOrElse(noHeaderAction(f, rh))
    }

}
