package server
import akka.actor._
import akka.event.Logging

class MyMessage


abstract class MyActor extends Actor {
  val log = Logging(context.system, this)
}
