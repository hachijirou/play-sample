package jobs

import akka.actor.Actor
import play.api.Logger

/**
 * @author work
 */
trait ActorBase extends Actor {
  def receive = {
    case message: String => {
      Logger.info(message)
      execute
    }
  }

  def execute()
}