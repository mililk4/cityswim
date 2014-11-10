import java.util.NoSuchElementException

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
