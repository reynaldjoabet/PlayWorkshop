import play.sbt.PlayInteractionMode
name         := """playworkshop"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

//.enablePlugins(PlayScala, PlayNettyServer)
//  .disablePlugins(PlayPekkoHttpServer)

// -Ysemanticdb tells the Scala 3 compiler to generate SemanticDB data.

Global / semanticdbEnabled := true
//ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
Global / semanticdbVersion := "4.13.4"
scalaVersion               := "3.3.3"

libraryDependencies ++= Seq(guice, ws, openId, jdbc, evolutions, logback)
libraryDependencies  += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

//SBT Task to Trigger Build and Package Dist Files

lazy val webpack = taskKey[Unit]("Run webpack in js directory")
webpack := {
  val workDir = new File("./js")
  Process("npm" :: "install" :: Nil, workDir) #&& Process(
    "npx" :: "webpack" :: Nil,
    workDir
  ) !
}

//It defines a task called `webpack`, so when you run `sbt webpack`, it will run `npm install && npx webpack` under `js`.

//Then we define another task to copy all the dist files to generated resource directory:

Compile / resourceGenerators += Def
  .task {
    val _    = webpack.value
    val file = (Compile / resourceManaged).value / "webview" / "static" / "dist"
    IO.copyDirectory(new File("./js/dist"), file, overwrite = true)
    IO.listFiles(file).toSeq
  }
  .taskValue

ThisBuild / excludeDependencies ++= Seq(
  // As of Play 3.0, groupId has changed to org.playframework; exclude transitive dependencies to the old artifacts
  // Specifically affects play-json-extensions
  // ExclusionRule(organization = "com.typesafe.play")
)

import scala.sys.process._

val reactDirectory =
  settingKey[File]("The directory where the react application is located")

val installReactDependencies =
  taskKey[Unit]("Install the dependencies for the react application")

val buildReactApp = taskKey[Unit]("Build the react application")
val copyInJS      = taskKey[File]("Build and copy in the JS file")

val moveReact =
  taskKey[Int]("move the compiled react application into the play assets")

val build = taskKey[Unit]("Copy JS and Config to react app")

installReactDependencies := {
  val result =
    JavaScriptBuild.npmProcess(reactDirectory.value, "ci").run().exitValue()
  if (result != 0)
    throw new Exception("Npm install failed.")
}

copyInJS := new File(".")
//   // generate the Javascript logic
//   val Attributed(outFiles) = (frontend / Compile / fullOptJS).value
//   val dest = reactDirectory.value / "src" / "calculation.js"
//   println(s"copying $outFiles to $dest")

//   // suppress warnings in generated code
//   (Process("echo" ::
//     """|/* eslint-disable */
//        |var BigInt = function(n) { return n; };""".stripMargin :: Nil)
//     #> dest
//   ).run().exitValue()

//   // get around BigInt compatibility issues with IE11/scalajs1
//   (Process("sed" ::
//     "-e" :: """s/"object"===typeof __ScalaJSEnv&&__ScalaJSEnv?__ScalaJSEnv://""" ::
//     outFiles.getAbsolutePath :: Nil,
//     baseDirectory.value) #>> dest).run().exitValue()
//   dest
// }

buildReactApp := {
  // val deps: Unit = installReactDependencies.value
  val reactJsFile: Unit = () // copyInJS
  val result            = JavaScriptBuild.npmProcess(reactDirectory.value, "run", "build").run().exitValue()
  if (result != 0)
    throw new Exception("npm run build failed.")
}

moveReact := {
  val reactApp: Unit = buildReactApp.value
  import scala.sys.process.{Process, ProcessBuilder}
  val result =
    Process("./sync-build.sh" :: Nil, baseDirectory.value).run().exitValue()
  1
}

//play.sbt.routes.RoutesKeys.routesImport += "play.sbt.routes.JavascriptBuild"

//play.sbt.routes.RoutesKeys.routesImport += "calculatenifrontend.controllers.Binders._"

PlayKeys.playDefaultPort := 9000

reactDirectory := (Compile / baseDirectory)(_ / "react").value

//Compile / unmanagedSourceDirectories ++= (common.jvm / (Compile / unmanagedSourceDirectories)).value
Compile / unmanagedResources += file("national-insurance.conf")

dist := dist.dependsOn(moveReact).value

//    Concat.groups := Seq(
//       "javascripts/all-services.js" -> group((baseDirectory.value / "app" / "assets" / "javascripts" / "src" * "*.js"))
//     )

// ------------------------------------------------------------------------------------------------
// Task Keys
// ------------------------------------------------------------------------------------------------

lazy val cleanPlatform = taskKey[Int]("Clean Yugabyte Platform")

lazy val compilePlatform = taskKey[Int]("Compile Yugabyte Platform")

lazy val runPlatformTask = taskKey[Unit]("Run Yugabyte Platform helper task")

lazy val runPlatform = inputKey[Unit]("Run Yugabyte Platform with UI")

lazy val consoleSetting =
  settingKey[PlayInteractionMode]("custom console setting")

lazy val versionGenerate = taskKey[Int]("Add version_metadata.json file")

lazy val buildVenv               = taskKey[Int]("Build venv")
lazy val generateCrdObjects      = taskKey[Int]("Generating CRD classes..")
lazy val generateOssConfig       = taskKey[Int]("Generating OSS class.")
lazy val buildModules            = taskKey[Int]("Build modules")
lazy val buildDependentArtifacts = taskKey[Int]("Build dependent artifacts")
lazy val releaseModulesLocally   = taskKey[Int]("Release modules locally")

lazy val downloadThirdPartyDeps =
  taskKey[Int]("Downloading thirdparty dependencies")

lazy val devSpaceReload =
  taskKey[Int]("Do a build without UI for DevSpace and reload")

lazy val cleanUI             = taskKey[Int]("Clean UI")
lazy val cleanVenv           = taskKey[Int]("Clean venv")
lazy val cleanModules        = taskKey[Int]("Clean modules")
lazy val cleanCrd            = taskKey[Int]("Clean CRD")
lazy val cleanOperatorConfig = taskKey[Unit]("Clean OperatorConfig")

def ybLog(s: String): Unit =
  println("[Yugabyte sbt log] " + s)

/**
  * Add UI Run hook to run UI alongside with API.
  */
// runPlatform := {
//   val curState = state.value
//   val newState = Project.extract(curState).appendWithoutSession(
//     Vector(PlayKeys.playRunHooks += UIRunHook(baseDirectory.value / "ui")),
//     curState
//   )
//   Project.extract(newState).runTask(runPlatformTask, newState)
// }

/**
  * UI Build Tasks like clean node modules, npm install and npm run build
  */

// Delete node_modules directory in the given path. Return 0 if success.
def cleanNodeModules(implicit dir: File): Int =
  Process("rm -rf node_modules", dir) !

(Compile / compile) := (Compile / compile).dependsOn(buildDependentArtifacts).value

buildDependentArtifacts := {
  ybLog("Building dependencies...")
//   (Compile / openApiProcessServer).value
//   openApiProcessClients.value
//   generateCrdObjects.value
//   generateOssConfig.value
  /// val status = Process("mvn install -P buildDependenciesOnly", baseDirectory.value / "parent-module").!
  // status
  // (Compile / compile).value
  0
}

// Execute `npm ci` command to install all node module dependencies. Return 0 if success.
def runNpmInstall(implicit dir: File): Int =
  if (cleanNodeModules != 0) throw new Exception("node_modules not cleaned up")
  else {
    println(
      "node version: " + Process("node" :: "--version" :: Nil).lineStream_!.head
    )
    println(
      "npm version: " + Process("npm" :: "--version" :: Nil).lineStream_!.head
    )
    println(
      "npm config get: " + Process(
        "npm" :: "config" :: "get" :: Nil
      ).lineStream_!.head
    )
    println(
      "npm cache verify: " + Process(
        "npm" :: "cache" :: "verify" :: Nil
      ).lineStream_!.head
    )
    Process("npm" :: "ci" :: "--legacy-peer-deps" :: Nil, dir).!
  }

// Execute `npm run build` command to build the production build of the UI code. Return 0 if success.
def runNpmBuild(implicit dir: File): Int =
  Process("npm run build-and-copy", dir) !

lazy val uIInstallDependency = taskKey[Unit]("Install NPM dependencies")
lazy val uIBuild             = taskKey[Unit]("Build production version of UI code.")
uIInstallDependency := {
  implicit val uiSource = baseDirectory.value / "ui"
  if (runNpmInstall != 0) throw new Exception("npm install failed")
}
uIBuild := {
  implicit val uiSource = baseDirectory.value / "ui"
  if (runNpmBuild != 0) throw new Exception("UI Build crashed.")
}

uIBuild := uIBuild.dependsOn(buildDependentArtifacts).value

/*
 * UI Build Scripts
 */

//Assets/unmanagedResourceDirectories  += (baseDirectory.value / "ranker-ui" / "dist")

def runNpmInstall2(dir: File): Int =
  if ((dir / "node_modules").exists()) 0
  else Process("npm" :: "install" :: Nil, dir) !

def runBuild(dir: File) = {
  val packagesInstall = runNpmInstall2(dir)
  if (packagesInstall == 0)
    Process(
      "ng" :: "build" :: "--prod" :: Nil,
      dir,
      "WEBPACK_ENV" -> "production"
    ) !
  else packagesInstall
}

def runKarma(dir: File) =
  Process("karma" :: "start" :: Nil, dir, "NODE_ENV" -> "true")

lazy val `ui-build` =
  taskKey[Unit]("Run UI build when packaging the application.")

`ui-build` := {
  val UIroot = baseDirectory.value / "ranker-ui"
  if (runBuild(UIroot) != 0) throw new Exception("Oops! UI Build crashed.")
}

lazy val `ui-test` = taskKey[Unit]("Run UI tests when testing application.")

`ui-test` := {
  val UIroot = baseDirectory.value / "ranker-ui"
  if ((runKarma(UIroot) !) != 0) throw new Exception("UI tests failed!")
}

`ui-test` := `ui-test`.dependsOn(`ui-build`).value

dist := dist.dependsOn(`ui-build`).value

stage := stage.dependsOn(`ui-build`).value

test := (Test / test).dependsOn(`ui-test`).value

routesGenerator := InjectedRoutesGenerator

generateJsReverseRouter := true

generateReverseRouter := true

//Apply these changes by running `reload`.
//Automatically reload the build when source changes are detected by setting
//Global / onChangedBuildSource := ReloadOnSourceChanges

//Disable this warning by setting
Global / onChangedBuildSource := ReloadOnSourceChanges

// This will help you serve React from the Play static folder
//inFile("public/javascripts") := (sourceDirectory in Compile).value / "public" / "javascripts"

PlayKeys.devSettings += "play.server.deferBodyParsing" -> "true"

Test / javaOptions       += "-Dconfig.file=conf/application.test.conf"
Production / javaOptions += "-Dconfig.file=conf/application.prod.conf" // for 'sbt runProd'
Universal / javaOptions ++= Seq(
  s"-Dpidfile.path=/dev/null",
  s"-Dconfig.file=/usr/share/${packageName.value}/conf/application.conf"
)

//.settings(PlayNpm.projectSettings)
