import java.security.Security
import java.util.regex.Pattern

import scala.jdk.CollectionConverters._

import com.typesafe.config.Config
import javax.inject.Singleton

@Singleton
class TLSConfig {
  /*
   * Helper class for modifying the TLS config property for the Java application.
   */

  val tlsDisabledKey = "jdk.tls.disabledAlgorithms"

  def getTLSDisabledAlgorithms: String =
    Security.getProperty(tlsDisabledKey)

  def setTLSDisabledAlgorithms(algorithms: String): Unit =
    Security.setProperty(tlsDisabledKey, algorithms)

  def modifyTLSDisabledAlgorithms(config: Config): Unit = {
    val pattern       = Pattern.compile("(\\w+\\s+keySize)\\s*<\\s*(\\d+)")
    val algorithmsStr = getTLSDisabledAlgorithms
    val algorithmList = algorithmsStr.split(", ").toBuffer

    for (i <- algorithmList.indices) {
      val matcher = pattern.matcher(algorithmList(i))
      if (matcher.find()) {
        val keySize = matcher.group(2).toInt
        if (keySize % 256 == 0 && keySize < 2048) {
          algorithmList(i) = s"${matcher.group(1)} < 2048"
        }
      }
    }

    if (config.hasPath("playworkshop.security.java.tls.disabled_algorithms")) {
      val toDisable = config
        .getStringList("playworkshop.security.java.tls.disabled_algorithms")
        .asScala
      algorithmList ++= toDisable
    }

    setTLSDisabledAlgorithms(algorithmList.mkString(", "))
  }

}
