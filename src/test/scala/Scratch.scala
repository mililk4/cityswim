


object Scratch extends App {

  val sf = new SanFranciscoPoolScheduleFetcher
  val schedules = sf.systemSchedule
  schedules.foreach { case (pool, entries) => println(pool + " :"); entries.foreach { entry => println("  " + entry)}}

}
