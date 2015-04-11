package fetcher

import org.jsoup.nodes.{Document, Element, Node}
import org.jsoup.select.NodeVisitor
import swim._

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


  def allPoolUrls(rootUrl: String): List[String] = {
    val document = get(rootUrl)
    val links = document.select(".sidebar-list h4 a[href]").asScala
    links.map { case e: Element => e.attr("href") }.toList
  }

  def extractPool(document: Document): Pool = {
    val poolName = document.select("title").text.replaceFirst("\\s*\\|.*", "")
    val infoPattern = "(.*?):\\s+(.*)".r
    val destinationInfoUl = document.select(".sidebar-list ul").first()
    val text = destinationInfoUl.select("li").asScala.map { case e: Element => e.text }
    val info = text.filter(infoPattern.pattern.matcher(_).matches).map { case infoPattern(k, v) => (k, v) }.toMap
    new Pool(poolName, info.get("Latitude").get.toDouble, info.get("Longitude").get.toDouble, info)
  }


  def extractSchedule(document: Document): Seq[ScheduleEntry] = {
    val entries = collection.mutable.LinkedList.newBuilder[ScheduleEntry]
    var day: Option[Weekday] = None

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
