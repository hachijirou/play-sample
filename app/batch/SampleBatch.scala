package batch

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props

object SampleBatch {

  case class Greeting(who: String)

  def main(args: Array[String]) : Unit = {
    val system = ActorSystem("MySystem")
    val greeter  = system.actorOf(Props[GreetingActor], name = "greeter")
    greeter ! Greeting("Charlie Paker")
    Thread.sleep(3000)
    sys.exit
  }

  class GreetingActor extends Actor with ActorLogging {
    def receive = {
      case Greeting(who) => log.info("Hello " + who)
    }
  }
}