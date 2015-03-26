package fetcher

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import swim.SystemSchedule

abstract class PoolScheduleFetcher {
  def systemSchedule: SystemSchedule

  def get(url: String): Document = {
    Jsoup.connect(url).userAgent("CityPool_Crawler-0.3").get()
  }

}
