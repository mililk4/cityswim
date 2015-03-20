
import org.joda.time._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.mutable

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

  val ORDERING = Ordering.Tuple3(Ordering.Long, Ordering.Long, Ordering.String)

  private def sortTuple = (actualStart.getMillis, actualStart.getMillis, pool.name)

  override def compare(that: SwimTime): Int = ORDERING.compare(this.sortTuple, that.sortTuple)
}

object Scratch extends App {
  val schedules = (new SanFranciscoPoolScheduleFetcher).systemSchedule
  println("Upcoming lap swims:")
  printUpcomingLapSwims(schedules)
  println("-" * 50)
  dump(schedules)

  def printUpcomingLapSwims(schedule: SystemSchedule): Unit = {
    val swims = new mutable.ListBuffer[SwimTime]

    schedules.foreach { case (pool, entries) =>
      for (entry <- entries if entry.eventType == EventType.LapSwim if entry.start.isDefined if entry.end.isDefined) {
        swims += SwimTime(entry.day, entry.start.get, entry.end.get, pool)
      }
    }
    println("-" * 50)

    for (swim <- swims.sorted) {
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
