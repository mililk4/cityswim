package server

import java.io.{File, FileWriter}

import akka.actor._
import fetcher.SanFranciscoPoolScheduleFetcher
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import swim.{EventType, SystemSchedule}

import scala.collection.mutable.ListBuffer

case object Reload extends MyMessage

class Crawler extends MyActor {

  private val fetcher = new SanFranciscoPoolScheduleFetcher


  override def receive: Actor.Receive = {
    case Reload =>
      log.info("starting reload")
      val schedule = fetcher.systemSchedule
      log.info("schedules fetched")
      val swims = upcomingSwims(schedule)
      log.info("converted to swims")
      val json = jsonForSwims(swims)
      log.info("rendered to JSON; saving to " + Settings.targetFile)
      atomicWrite(json, Settings.targetFile)
      log.info("JSON saved")

      log.info("reload finished")
  }

  def jsonForSwims(swims: ListBuffer[SwimTime]): String = {
    val json = "swims" -> swims.map { s =>
      ("pool_name" -> s.poolName) ~
        ("day_label" -> s.dayLabel) ~
        ("start_label" -> s.startLabel) ~
        ("end_label" -> s.endLabel) ~
        ("time_label" -> s.timeLabel) ~
        ("start" -> s.actualStart.getMillis) ~
        ("end" -> s.actualEnd.getMillis)
    }

    pretty(render(json))
  }


  def atomicWrite(json: String, filename: String): Unit = {
    val dest = new File(filename)
    val temp = new File(filename + "-" + System.currentTimeMillis())
    val writer = new FileWriter(temp)
    writer.write(json)
    writer.close()
    temp.renameTo(dest)
  }

  def upcomingSwims(schedule: SystemSchedule): ListBuffer[SwimTime] = {
    val swims = new ListBuffer[SwimTime]


    schedule.foreach { case (pool, entries) =>
      for (entry <- entries if entry.eventType == EventType.LapSwim if entry.start.isDefined if entry.end.isDefined) {
        swims += SwimTime(entry.day, entry.start.get, entry.end.get, pool)
      }
    }

    swims.sorted
  }

}
