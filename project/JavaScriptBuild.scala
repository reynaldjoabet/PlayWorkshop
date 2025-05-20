import java.io.File

import scala.sys.process.{Process, ProcessBuilder}

import com.typesafe.sbt.packager.Keys.*
import play.sbt.PlayImport.PlayKeys.*
import sbt._
import sbt.Keys._

/**
  * Build of UI in JavaScript
  */
object JavaScriptBuild {

  def npmProcess(base: File, args: String*): ProcessBuilder = {
    if (sys.props("os.name").toLowerCase contains "windows") {
      Process("cmd" :: "/c" :: "npm" :: args.toList, base, "CI" -> "false")
    } else {
      Process("npm" :: args.toList, base, "CI" -> "false")
    }
  }

  val uiDirectory = SettingKey[File]("ui-directory")

  val npmInstall = taskKey[File]("npm-install")

  val gulpBuild = taskKey[Unit]("gulp-build")

}
