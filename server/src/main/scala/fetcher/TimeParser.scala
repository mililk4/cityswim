package fetcher

import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

object TimeParser {
  val parsers = List(DateTimeFormat.forPattern("hh:mm aa"))
  def parse(text: String) : Option[LocalTime] = {
    parsers.foreach( f => return Some(LocalTime.parse(text, f)))
    None
  }
}
