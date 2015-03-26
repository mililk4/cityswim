package swim

import fetcher.{DateParser, TimeParser}
import org.joda.time.{LocalDate, LocalTime}

object ScheduleEntry {
  def make(day: Weekday, text: Seq[String]): ScheduleEntry = {
    new ScheduleEntry(EventType.parse(text(0)),
      day,
      TimeParser.parse(text(2)),
      TimeParser.parse(text(3)),
      DateParser.parse(text(9)),
      DateParser.parse(text(10)))
  }
}

class ScheduleEntry(val eventType: EventType,
                    val day: Weekday,
                    val start: Option[LocalTime], val end: Option[LocalTime],
                    val validFrom: Option[LocalDate], val validTo: Option[LocalDate]) {
  override def toString: String = s"ScheduleEntry($eventType, $day, $start, $end, $validFrom, $validTo)"
}