import org.joda.time.{LocalDate, LocalTime}

object ScheduleEntry {
  def make(day: Weekday.Weekday, text: Seq[String]): ScheduleEntry = {
    new ScheduleEntry(EventType.parse(text(0)),
      day,
      TimeParser.parse(text(2)),
      TimeParser.parse(text(3)),
      DateParser.parse(text(9)),
      DateParser.parse(text(10)))
  }
}

class ScheduleEntry(eventType: EventType,
                    day: Weekday.Weekday,
                    start: Option[LocalTime], end: Option[LocalTime],
                    validFrom: Option[LocalDate], validTo: Option[LocalDate]) {
  override def toString: String = s"ScheduleEntry($eventType, $day, $start, $end, $validFrom, $validTo)"
}