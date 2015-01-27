import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element, Node}
import org.jsoup.select.NodeVisitor

import scala.collection.JavaConverters._

class SanFranciscoPoolScheduleFetcher extends PoolScheduleFetcher {

  override def systemSchedule: SystemSchedule = {
    val schedules = new SystemSchedule
      val urls = allPoolUrls("http://sfrecpark.org/recreation-community-services/aquatics-pools/")
    urls.foreach { url =>
      val document = get(url)
      schedules(extractPool(document)) = extractSchedule(document)

    }
    schedules
  }


  def allPoolUrls(rootUrl:String): List[String] = {
    val document = get(rootUrl)
    val links = document.select(".sidebar-list h4 a[href]").asScala
    links.map { case e: Element => e.attr("href")}.toList
  }

  def extractPool(document: Document): Pool = {
    new Pool(document.select("title").text.replaceFirst("\\s*\\|.*", ""))
  }


  def extractSchedule(document: Document): Seq[ScheduleEntry] = {
    val entries = collection.mutable.LinkedList.newBuilder[ScheduleEntry]
    var day: Option[Weekdays.Weekday] = None

    document.traverse(new NodeVisitor {
      override def tail(node: Node, depth: Int): Unit = {
        node match {
          case e: Element =>
            if (e.nodeName == "h3") {
              day = Weekdays.parse(e.text)
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
