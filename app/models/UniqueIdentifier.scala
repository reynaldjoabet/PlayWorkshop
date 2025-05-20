package models

import java.util.UUID

import play.api.libs.json._

case class UniqueIdentifier(uuid: UUID) {
  override def toString: String = uuid.toString
}

object UniqueIdentifier {

  def apply(value: String): UniqueIdentifier =
    UniqueIdentifier(UUID.fromString(value))

  implicit val uniqueIdentifierFormats: Writes[UniqueIdentifier] = Writes {
    (identifier: UniqueIdentifier) => JsString(identifier.toString())
  }

  implicit val uniqueIdentifierReads: Reads[UniqueIdentifier] = Reads { (jsValue: JsValue) =>
    JsSuccess(UniqueIdentifier(jsValue.as[String]))
  }

}
