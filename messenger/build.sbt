name := "InstantMessenger"

version := "1.0"

scalaVersion := "2.12.0"

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.102-R11"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.5.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8", "-feature")

fork := true

