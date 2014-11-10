import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

object DateParser {
  // TODO: handle dates of MM/dd with implicit years
  val parsers = List("MM/dd/YYYY").map(s=> DateTimeFormat.forPattern(s))
  def parse(text: String) : Option[LocalDate] = {
    parsers.foreach{f =>
      try {
        val date = LocalDate.parse(text, f)
        return Some(date)
      }
      catch {
        case e: IllegalArgumentException => None
      }
    }
    None
  }
}
