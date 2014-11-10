import java.io.File
import java.util.NoSuchElementException

import org.joda.time.format.DateTimeFormat
import org.joda.time.{LocalDate, DateTime, Interval, LocalTime}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element, Node}
import org.jsoup.select.NodeVisitor

import scala.collection.JavaConverters._

object Weekday extends Enumeration {
  type Weekday = Value
  val Sunday = Value("Sunday")
  val Monday = Value("Monday")
  val Tuesday = Value("Tuesday")
  val Wednesday = Value("Wednesday")
  val Thursday = Value("Thursday")
  val Friday = Value("Friday")
  val Saturday = Value("Saturday")

  def parse(string: String): Option[Weekday.Value] = {
    try {
      Option(Weekday.withName(string))
    } catch {
      case e: NoSuchElementException => None
    }
  }
}

object EventType extends Enumeration {
  type EventType = EventType.Value
  val LapSwim = Value
  val RecreationSwim = Value
  val SeniorSwim = Value
  val Unknown = Value

  val rLapSwim = "(?i)\\s*lap.*".r
  val rOpenSwim = "(?i)\\s*recreation.*".r
  val rSeniorSwim = "(?i)\\s*senior.*".r

  def parse(s: String): EventType.EventType = s match {
    case rLapSwim() => LapSwim
    case rOpenSwim() => RecreationSwim
    case rSeniorSwim() => SeniorSwim
    case _ => Unknown
  }
}


object TimeParser {
  val parsers = List(DateTimeFormat.forPattern("hh:mm aa"))
  def parse(text: String) : Option[LocalTime] = {
    parsers.foreach( f => return Some(LocalTime.parse(text, f)))
    None
  }
}

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

class ScheduleEntry(eventType: EventType.EventType,
                    day: Weekday.Weekday,
                    start: Option[LocalTime], end: Option[LocalTime],
                    validFrom: Option[LocalDate], validTo: Option[LocalDate]) {
  override def toString: String = s"ScheduleEntry($eventType, $day, $start, $end, $validFrom, $validTo)"
}

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

case class Pool(name : String)

object Scratch extends App {

  // http://sfrecpark.org/recreation-community-services/aquatics-pools/
  val urls = List("http://sfrecpark.org/destination/mission-playground/mission-community-pool/", "http://sfrecpark.org/destination/garfield-square/garfield-pool/")
  val schedules = new collection.mutable.HashMap[Pool, Seq[ScheduleEntry]]
  urls.foreach {url =>
    val document = Jsoup.connect(url).userAgent("CityPool_Crawler-0.3").get()
    schedules(extractPool(document)) = extractSchedule(document)

  }
  schedules.foreach{case(pool,entries)=> println(pool +  " :"); entries.foreach{entry => println("  " + entry)} }

  def extractPool(document: Document): Pool = {
    new Pool(document.select("title").text.replaceFirst("\\s*\\|.*", ""))
  }


  def extractSchedule(document:Document): Seq[ScheduleEntry] = {
    val entries = collection.mutable.LinkedList.newBuilder[ScheduleEntry]
    var day: Option[Weekday.Weekday] = None
    document.traverse(new NodeVisitor {
      override def tail(node: Node, depth: Int): Unit = {
        node match {
          case e: Element =>
            if (e.nodeName == "h3") {
              day = Weekday.parse(e.text)
            }
            if (day.isDefined && e.nodeName == "tbody") {
              entries += ScheduleEntry.make(day.get, e.select("td").asScala.map(cell => cell.text))
            }
          case _ => None
        }
      }

      override def head(p1: Node, p2: Int): Unit = {}
    })
     entries.result()
  }

}
