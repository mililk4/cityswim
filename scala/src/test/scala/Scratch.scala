
import org.joda.time._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.mutable.ListBuffer

/* The beginnings of the view model. */
case class SwimTime(day: Weekday, start: LocalTime, end: LocalTime, pool: Pool) extends Ordered[SwimTime] {
  /* This calculation of times is a little hinky. It depends on sessions never crossing midnight boundaries.
    * But I guess the data model does as well, so it's fine? Hm. */

  def actualEnd: DateTime = {
    val now = new DateTime()
    var actualEnd = end.toDateTime(now).withDayOfWeek(day.asInt)
    while (actualEnd.isBefore(now)) {
      actualEnd = actualEnd.withFieldAdded(DurationFieldType.weeks(), 1)
    }
    actualEnd.toDateTime
  }

  def actualStart: DateTime = {
    start.toDateTime(actualEnd)
  }

  def poolName = {
    pool.name.replaceAll("\\s*Pool$", "").replaceAll("Mission Community","Mission").replaceAll("Martin Luther King Jr", "MLK")
  }

  def timeLabel = {
    dayLabel + " " + startLabel + "-" + endLabel
  }

  def endLabel: String = {
    shortTime(end)
  }

  def startLabel: String = {
    shortTime(start)
  }

  def dayLabel: String = {
    day.shortName
  }

  private def shortTime(time: LocalTime): String = {
    val hour = time.getHourOfDay
    val min = time.getMinuteOfHour
    val hourText = if (hour <= 12) hour else hour - 12
    val minText = if (min == 0) "" else f":$min%02d"
    val ap = if (hour < 12) "a" else "p"
    hourText + minText + ap
  }


  val ORDERING = Ordering.Tuple3(Ordering.Long, Ordering.Long, Ordering.String)

  private def sortTuple = (actualStart.getMillis, actualStart.getMillis, pool.name)

  override def compare(that: SwimTime): Int = ORDERING.compare(this.sortTuple, that.sortTuple)
}


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

    schedules.foreach { case (pool, entries) =>
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
