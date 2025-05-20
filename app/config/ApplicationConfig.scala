package config

import java.time.LocalDate
import java.util.Base64

import scala.util.Try

import com.google.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton()
class ApplicationConfig @Inject() (configuration: Configuration) {

  protected def loadConfig(key: String): String =
    configuration
      .getOptional[String](key)
      .getOrElse(throw new Exception(s"Missing configuration key: $key"))

  protected def loadBooleanConfig(key: String): Boolean =
    configuration.getOptional[String](key).fold(false)(_.toBoolean)

  protected def loadInt(key: String): Int =
    configuration
      .getOptional[Int](key)
      .getOrElse(throw new Exception(s"Missing configuration key: $key"))

  // ISO_LOCAL_DATE format (e.g. 2007-12-03)
  protected def loadLocalDate(key: String): LocalDate =
    Try[LocalDate] {
      LocalDate.parse(loadConfig(key))
    }.getOrElse(
      throw new Exception(
        s"LocalDate value badly formatted for key: $key. Should be yyyy-MM-dd (e.g. 2007-04-01)"
      )
    )

  def businessRatesValuationFrontendUrl(page: String): String =
    loadConfig("business-rates-valuation.url") + s"/$page"

  def businessRatesCheckUrl(page: String): String =
    loadConfig("business-rates-check.url") + s"/$page"

  def businessTaxAccountUrl(page: String): String =
    loadConfig("business-tax-account.url") + s"/$page"

  def dashboardUrl(page: String): String =
    loadConfig("business-rates-dashboard-frontend.url") + s"/$page"

  def yourDetailsUrl(page: String): String =
    loadConfig("business-rates-dashboard-frontend.url") + s"/$page"

  def businessRatesChallengeUrl(page: String): String =
    loadConfig("business-rates-challenge-frontend.url") + s"/$page"

  lazy val appName: String = loadConfig("appName")

  lazy val ivBaseUrl = loadConfig(
    "microservice.services.identity-verification.url"
  )

  lazy val vmvUrl                      = loadConfig("vmv-frontend.url")
  lazy val basGatewaySignInUrl: String = loadConfig("bas-gateway-sign-in.url")
  lazy val attachmentsUrl: String      = loadConfig("business-rates-attachments.url")
  lazy val ggRegistrationUrl: String   = loadConfig("gg-registration.url")
  lazy val serviceUrl: String          = loadConfig("voa-property-linking-frontend.url")
  lazy val upliftCompletionUrl: String = loadConfig("upliftCompletion.url")
  lazy val upliftFailureUrl: String    = loadConfig("upliftFailure.url")

  lazy val identityVerificationUrl: String = loadConfig(
    "microservice.services.identity-verification-frontend.url"
  )

  lazy val agentAppointDelay: Int = loadInt("agent.appoint.async.delay")

  // Google Analytics (GTM)
  // looks clunky, but spaces not allowed in app-config yaml, so construct the dimension here
  lazy val personIdDimension: String =
    s"VOA_person_ID (${configuration.get[String]("google-analytics.dimension.personId")})"

  lazy val loggedInDimension: String =
    s"Logged_in (${configuration.get[String]("google-analytics.dimension.loggedIn")})"

  lazy val ccaAgentDimension: String =
    s"CCA_Agent (${configuration.get[String]("google-analytics.dimension.ccaAgent")})"

  lazy val earliestEnglishStartDate: LocalDate = loadLocalDate(
    "property-linking.default.earliestEnglishStartDate"
  )

  lazy val earliestWelshStartDate: LocalDate = loadLocalDate(
    "property-linking.default.earliestWelshStartDate"
  )

  lazy val ivEnabled: Boolean = loadBooleanConfig("featureFlags.ivEnabled")

  lazy val newRegistrationJourneyEnabled: Boolean = loadBooleanConfig(
    "featureFlags.newRegistrationJourneyEnabled"
  )

  lazy val draftListEnabled: Boolean = loadBooleanConfig(
    "feature-switch.draftListEnabled"
  )

  lazy val signOutUrl =
    s"${loadConfig("sign-out.url")}?continue_url=${dashboardUrl("home")}&accountType=organisation&origin=voa"

  lazy val signOutUrlWithoutParams = loadConfig("sign-out.url")

  lazy val stubEnrolment: Boolean = loadBooleanConfig("enrolment.useStub")

  lazy val bannerContent: Option[String] =
    configuration
      .getOptional[String]("encodedBannerContent")
      .map(e => new String(Base64.getUrlDecoder.decode(e)))

  lazy val bannerContentWelsh: Option[String] =
    configuration
      .getOptional[String]("encodedBannerContentWelsh")
      .map(e => new String(Base64.getUrlDecoder.decode(e)))

  lazy val plannedImprovementsContent: Option[String] = configuration
    .getOptional[String]("plannedImprovementsContent")
    .map(e => new String(Base64.getUrlDecoder.decode(e)))

  val default2017AssessmentEndDate: LocalDate = LocalDate.of(2023, 3, 31)

  lazy val environmentHost: String =
    configuration.get[String]("environment-base.host")

  lazy val currentListYear: String = loadConfig("currentListYear")

  lazy val comparablePropertiesEnabled: Boolean = loadBooleanConfig(
    "feature-switch.comparablePropertiesEnabled"
  )

  lazy val agentJourney2026: Boolean = loadBooleanConfig(
    "feature-switch.agentJourney2026Enabled"
  )

  lazy val environment: String = loadConfig("environment")

}

private case class ConfigMissing(key: String)

// package config

// import com.google.inject.{Inject, Singleton}
// import controllers.routes
// import play.api.Configuration
// import play.api.i18n.Lang
// import play.api.mvc.Call

// @Singleton
// class FrontendAppConfig @Inject() (configuration: Configuration) {

//   private val contactHost = configuration.get[String]("contact-frontend.host")
//   private val contactFormServiceIdentifier = "play26frontend"

//   val analyticsToken: String = configuration.get[String](s"google-analytics.token")
//   val analyticsHost: String = configuration.get[String](s"google-analytics.host")
//   val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
//   val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
//   val betaFeedbackUrl = s"$contactHost/contact/beta-feedback"
//   val betaFeedbackUnauthenticatedUrl = s"$contactHost/contact/beta-feedback-unauthenticated"

//   lazy val authUrl: String = configuration.get[Service]("auth").baseUrl
//   lazy val loginUrl: String = configuration.get[String]("urls.login")
//   lazy val loginContinueUrl: String = configuration.get[String]("urls.loginContinue")

//   lazy val languageTranslationEnabled: Boolean =
//     configuration.get[Boolean]("microservice.services.features.welsh-translation")

//   def languageMap: Map[String, Lang] = Map(
//     "english" -> Lang("en"),
//     "cymraeg" -> Lang("cy")
//   )

//   def routeToSwitchLanguage: String => Call =
//     (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)
// } extends Exception(s"Missing config for $key")
