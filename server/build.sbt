import _root_.sbtassembly.Plugin.AssemblyKeys._
import _root_.sbtassembly.Plugin._

name := "cityswim"

version := "1.0"


libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"

libraryDependencies += "org.jsoup" % "jsoup" % "1.8.1"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.4.0"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.11"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.6"

mainClass := Some("server.Daemon")

exportJars := true


assemblySettings

jarName in assembly := "cityswim.jar"