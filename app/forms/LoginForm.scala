package forms

import java.util.UUID

import play.api.data.validation.Constraints.nonEmpty
import play.api.data.Form
import play.api.data.Forms._

final case class LoginData(username: String, password: String)
object LoginForm {

  val loginForm: Form[LoginData] = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )((username, password) => LoginData(username, password))(loginData =>
      Some(loginData.username, loginData.password)
    )
  )

}

case class LoginData2(email: String, password: String)

val loginForm: Form[LoginData2] = Form(
  mapping(
    "email"    -> email.verifying(nonEmpty), // built-in validator
    "password" -> nonEmptyText
  )(LoginData2.apply)(loginData => Some(loginData.email, loginData.password))
)

val form = Form(
  mapping(
    "userID"           -> uuid,
    "sharedKey"        -> nonEmptyText,
    "rememberMe"       -> boolean,
    "verificationCode" -> nonEmptyText(minLength = 6, maxLength = 6)
  )(Data.apply)(Data.unapply)
)

/**
  * The form data.
  * @param userID
  *   The unique identifier of the user.
  * @param sharedKey
  *   the TOTP shared key
  * @param rememberMe
  *   Indicates if the user should stay logged in on the next visit.
  * @param verificationCode
  *   Verification code for TOTP-authentication
  */
case class Data(userID: UUID, sharedKey: String, rememberMe: Boolean, verificationCode: String = "")

object Data {

  def apply(userID: UUID, sharedKey: String, rememberMe: Boolean, verificationCode: String) =
    new Data(userID, sharedKey, rememberMe, verificationCode)

  def unapply(data: Data): Option[(UUID, String, Boolean, String)] =
    Some((data.userID, data.sharedKey, data.rememberMe, data.verificationCode))

}
