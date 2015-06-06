import play.api._
import play.api.mvc._
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current
import akka.actor.Props

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    // Akkaのスケジューリング
    val scheduler = QuartzSchedulerExtension(Akka.system)
    scheduler.schedules.foreach {
      case (key, setting) =>
        scheduler.schedule(
          setting.name,
          Akka.system.actorOf(Props(Class.forName(s"jobs.${setting.name}"))),
          setting.description.getOrElse(setting.name))
    }
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}