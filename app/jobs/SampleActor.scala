package jobs

import play.api.Logger

/**
 * @author work
 */
class SampleActor extends ActorBase {

  def execute = {
    Logger.info("hogehoge")
  }
}