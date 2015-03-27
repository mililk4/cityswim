package server
import scala.concurrent.duration._

object Settings {
  val reloadFrequency = 1.hour
  val targetFile = "/var/www/cityswim/v1/swims.json"
}
