package server

import akka.actor._
import akka.event.Logging

import scala.concurrent.duration._



object Daemon extends App {
  val system = ActorSystem("cityswim")
  val daemon = system.actorOf(Props[Daemon], "daemon")
  daemon ! Start
}

case object Start extends MyMessage

class Daemon extends MyActor {

  import context._

  val crawler = context.system.actorOf(Props[Crawler], "crawler")

  override def receive: Receive = {
    case Start =>
      log.info("starting daemon")
      context.system.scheduler.schedule(1.second, Settings.reloadFrequency, crawler, Reload)
      log.info("daemon started")
  }

}



