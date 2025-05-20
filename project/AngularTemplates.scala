import sbt._
import sbt.Keys._

object AngularTemplates {

  val compress =
    SettingKey[Boolean]("angular-templates-compress", "Compress HTML using HtmlCompressor [true].")

  val compressRemoveComments =
    SettingKey[Boolean]("angular-templates-compress-comments", "Remove HTML comments [true].")

  val compressRemoveMultiSpaces = SettingKey[Boolean](
    "angular-templates-compress-multi-spaces",
    "Remove multiple whitespace characters [true]."
  )

  val compressRemoveIntertagSpaces = SettingKey[Boolean](
    "angular-templates-compress-intertag-spaces",
    "Remove inter-tag whitespace characters [false]."
  )

  val compressRemoveQuotes = SettingKey[Boolean](
    "angular-templates-compress-quotes",
    "Remove unnecessary tag attribute quotes [false]."
  )

  val compressPreserveLineBreaks = SettingKey[Boolean](
    "angular-templates-compress-line-breaks",
    "Preserve original line breaks [false]."
  )

  val compressRemoveSurroundingSpaces = SettingKey[Seq[String]](
    "angular-templates-compress-surrounding-spaces",
    "Remove spaces around provided tags."
  )

  val module = SettingKey[String](
    "angular-templates-module",
    "Name of angular application module variable (needed for outputJs) [\"module\"]."
  )

  val naming = SettingKey[String => String](
    "angular-templates-naming",
    "Function to use to name templates based on their (relative) path [identity]."
  )

  val outputHtml = SettingKey[Option[String]](
    "angular-templates-output-html",
    "Output an html file containing <script> text/ng-template tags [templates.html]."
  )

  val outputJs = SettingKey[Option[String]](
    "angular-templates-output-js",
    "Output a js file that puts templates into $templateCache [templates.js]."
  )

  val angularTemplates =
    TaskKey[Seq[File]]("angular-templates", "Generate AngularJS templates from HTML files.")

  compress                        := true
  compressRemoveComments          := true
  compressRemoveMultiSpaces       := true
  compressRemoveIntertagSpaces    := false
  compressRemoveQuotes            := false
  compressPreserveLineBreaks      := false
  compressRemoveSurroundingSpaces := Nil
  module                          := "module"
  naming                          := identity _
  outputHtml                      := Some("templates.html")
  outputJs                        := Some("templates.js")

//(Assets / managedResourceDirectories) += (Assets / angularTemplates / resourceManaged).value
}
