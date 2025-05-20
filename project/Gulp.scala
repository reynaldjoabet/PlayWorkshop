import scala.sys.process._

import play.sbt.PlayRunHook
import sbt.{Command, ConsoleLogger, File}

object Gulp {
// def apply(base: File): PlayRunHook = GulpProcess

//   private object GulpProcess extends PlayRunHook {

//     var gulpRun: Option[Process] = None

//     override def beforeStarted(): Unit = {
//       val log = ConsoleLogger()
//       log.info("run npm install...")
//       npmProcess(base, "install").!

//       log.info("Starting default Gulp task..")
//       gulpRun = Some(gulpProcess(base, "default").run())
//     }

//     override def afterStopped(): Unit = {
//       // Stop gulp when play run stops
//       gulpRun.foreach(p => p.destroy())
//       gulpRun = None
//     }

//   }

  def gulpProcess(base: File, args: String*): ProcessBuilder = {
    if (sys.props("os.name").toLowerCase contains "windows") {
      Process(
        "cmd" :: "/c" :: "node_modules\\.bin\\gulp.cmd" :: args.toList,
        base
      )
    } else {
      Process("node" :: "node_modules/.bin/gulp" :: args.toList, base)
    }
  }

  def npmProcess(base: File, args: String*): ProcessBuilder = {
    if (sys.props("os.name").toLowerCase contains "windows") {
      Process("cmd" :: "/c" :: "npm" :: args.toList, base)
    } else {
      Process("npm" :: args.toList, base)
    }
  }

  def gulpCommand(base: File) = Command.args("gulp", "<gulp-command>") { (state, args) =>
    gulpProcess(base, args: _*) !;
    state
  }

}
