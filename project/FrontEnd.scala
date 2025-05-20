import scala.sys.process._

import sbt._
import sbt.Keys._

object FrontEnd extends AutoPlugin {

  object autoImport {

    val frontendDev   = taskKey[Unit]("Run front-end wiredep for dev")
    val frontendFiles = taskKey[Seq[(File, String)]]("Prepare front-end files for prod")

  }

  import autoImport._

  override def trigger = allRequirements

  override def projectSettings: Seq[Setting[_]] = Seq(
    frontendDev := {
      val log   = streams.value.log
      val uiDir = baseDirectory.value / "ui"

      log.info("Running: grunt wiredep")
      Process(Seq("grunt", "wiredep"), uiDir) ! log
      ()
    },
    frontendFiles := {
      val log   = streams.value.log
      val uiDir = baseDirectory.value / "ui"

      log.info("Running: npm install")
      Process(Seq("npm", "install"), uiDir) ! log

      log.info("Running: bower install")
      Process(Seq("bower", "install"), uiDir) ! log

      log.info("Running: grunt build")
      Process(Seq("grunt", "build"), uiDir) ! log

      val distDir = uiDir / "dist"
      Path
        .allSubpaths(distDir)
        .map { case (file, subPath) =>
          file -> s"ui/${subPath.toString}"
        }
        .toSeq
    }
  )

}
