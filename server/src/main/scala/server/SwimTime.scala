package server

import org.joda.time.{DateTime, DurationFieldType, LocalTime}
import swim.{Pool, Weekday}

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
    pool.name
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

  private def sortTuple = (actualStart.getMillis, actualEnd.getMillis, pool.name)

  override def compare(that: SwimTime): Int = ORDERING.compare(this.sortTuple, that.sortTuple)
}
