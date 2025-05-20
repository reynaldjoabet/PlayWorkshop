import com.typesafe.sbt.packager.Keys._
import play.sbt.PlayImport.PlayKeys
import sbt._
import sbt.Keys._
import sys.process.*

/**
  * Build of UI in JavaScript
  */
object JavaScriptBuild3 {

  val uiDirectory = SettingKey[File]("ui-directory")

  val gulpBuild  = taskKey[Int]("gulp-build")
  val gulpWatch  = taskKey[Int]("gulp-watch")
  val gulpTest   = taskKey[Int]("gulp-test")
  val npmInstall = taskKey[Int]("npm-install")

  val javaScriptUiSettings = Seq(
    // the JavaScript application resides in "ui"
    // uiDirectory := (Compile/baseDirectory) { _ /"app" / "assets" }.value,
    uiDirectory := (Compile / baseDirectory).value / "app" / "assets",

// Global / commands := {
//   val base = (ThisBuild / baseDirectory).value / "app" / "assets"
//   Seq(gulpCommand(base), npmCommand(base))
// },
    npmInstall := {
      val result = Gulp.npmProcess(uiDirectory.value, "install").run().exitValue()
      if (result != 0)
        throw new Exception("Npm install failed.")
      result
    },
    gulpBuild := {
      val result = Gulp.gulpProcess(uiDirectory.value, "default").run().exitValue()
      if (result != 0)
        throw new Exception("Gulp build failed.")
      result
    },
    gulpTest := {
      val result = Gulp.gulpProcess(uiDirectory.value, "test").run().exitValue()
      if (result != 0)
        throw new Exception("Gulp test failed.")
      result
    },
    gulpTest  := gulpTest.dependsOn(npmInstall).value,
    gulpBuild := gulpBuild.dependsOn(npmInstall).value,

    // runs gulp before staging the application
    dist := dist.dependsOn(gulpBuild).value

//(Test / test) := (gulpTest.dependsOn(Test / test)).value

    // Turn off play's internal less compiler
    // lessEntryPoints := Nil,

    // Turn off play's internal JavaScript and CoffeeScript compiler
    // javascriptEntryPoints := Nil,
    // coffeescriptEntryPoints := Nil,

    // integrate JavaScript build into play build
    // PlayKeys.playRunHooks += {uiDirectory.map(ui => Gulp(ui))}.value
  )

  def npmCommand(base: File) = Command.args("npm", "<npm-command>") { (state, args) =>
    if (sys.props("os.name").toLowerCase contains "windows") {
      scala.sys.process.Process("cmd" :: "/c" :: "npm" :: args.toList, base) !
    } else {
      scala.sys.process.Process("npm" :: args.toList, base) !
    }
    state
  }

}
