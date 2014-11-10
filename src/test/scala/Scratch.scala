
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element, Node}
import org.jsoup.select.NodeVisitor

import scala.collection.JavaConverters._








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
