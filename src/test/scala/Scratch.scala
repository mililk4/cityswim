import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}

import scala.collection.JavaConverters._




object Scratch extends App {

  val sf = new SanFranciscoPoolScheduleFetcher
  val schedules = sf.systemSchedule
  schedules.foreach { case (pool, entries) => println(pool + ":"); entries.foreach { entry => println("  " + entry)}}

  def get(url: String): Document = {
    Jsoup.connect(url).userAgent("CityPool_Crawler-0.3").get()
  }
}
