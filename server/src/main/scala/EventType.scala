sealed abstract class EventType

object EventType {

  case object LapSwim extends EventType
  case object RecreationSwim extends EventType
  case object SeniorSwim extends EventType
  case class Unknown(text: String) extends EventType

  val rLapSwim = "(?i)\\s*lap.*".r
  val rOpenSwim = "(?i)\\s*recreation.*".r
  val rSeniorSwim = "(?i)\\s*senior.*".r

  def parse(s: String): EventType = s match {
    case rLapSwim() => LapSwim
    case rOpenSwim() => RecreationSwim
    case rSeniorSwim() => SeniorSwim
    case _ => Unknown(s)
  }
}
