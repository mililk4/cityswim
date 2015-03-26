
import fetcher.SanFranciscoPoolScheduleFetcher
import org.joda.time._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import server.SwimTime
import swim._

import scala.collection.mutable.ListBuffer




object Scratch extends App {

  import org.json4s.JsonDSL._
  import org.json4s.native.JsonMethods._

  val schedules = (new SanFranciscoPoolScheduleFetcher).systemSchedule
  println("Upcoming lap swims:")
  printUpcomingLapSwims(schedules)
  println("-" * 50)
  printClientJson(schedules)

  //  println("-" * 50)
  //  dump(schedules)


  def printClientJson(schedule: SystemSchedule) = {
    val swims = upcomingSwims(schedule)
    val json = "swims" -> swims.map { s =>
      ("pool_name" -> s.poolName) ~
        ("day_label" -> s.dayLabel) ~
        ("start_label" -> s.startLabel) ~
        ("end_label" -> s.endLabel) ~
        ("time_label" -> s.timeLabel) ~
        ("start" -> s.actualStart.getMillis) ~
        ("end" -> s.actualEnd.getMillis)
    }

    println(pretty(render(json)))

  }

  def printUpcomingLapSwims(schedule: SystemSchedule): Unit = {
    val swims = upcomingSwims(schedule)

    for (swim <- swims) {
      val now = new DateTime
      print(swim)
      if (swim.actualStart.isBefore(now)) {
        print(" runs for " + minutesFromNow(swim.actualEnd) + " more minutes")
      } else {
        print(" starts in " + minutesFromNow(swim.actualStart) + " minutes")
      }

      println()
    }

    def minutesFromNow(dt: DateTime) = new Duration(new DateTime(), dt).getStandardMinutes
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

  def dump(schedules: SystemSchedule) {
    schedules.foreach { case (pool, entries) => dump(pool, entries)}
    def dump(pool: Pool, entries: Seq[ScheduleEntry]) {
      println(pool + ":")
      entries.foreach { entry => println("  " + entry)}
    }
  }


  def get(url: String): Document = {
    Jsoup.connect(url).userAgent("CityPool_Crawler-0.3").get()
  }
}
