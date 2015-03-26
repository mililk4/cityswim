package swim

import org.joda.time.DateTimeConstants
sealed abstract class Weekday(val name:String, val asInt: Int) extends Ordered[Weekday]{
  def compare(that: Weekday)  = this.asInt - that.asInt
  def shortName = name.substring(0,3)
}

object Weekdays  {
  case object Sunday extends Weekday("Sunday", DateTimeConstants.SUNDAY)
  case object Monday extends Weekday("Monday", DateTimeConstants.MONDAY)
  case object Tuesday extends Weekday("Tuesday", DateTimeConstants.TUESDAY)
  case object Wednesday extends Weekday("Wednesday", DateTimeConstants.WEDNESDAY)
  case object Thursday extends Weekday("Thursday", DateTimeConstants.THURSDAY)
  case object Friday extends Weekday("Friday", DateTimeConstants.FRIDAY)
  case object Saturday extends Weekday("Saturday", DateTimeConstants.SATURDAY)

  val all = Seq(Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday)


  def parse(string: String): Option[Weekday] = all.find(string == _.name)
}
