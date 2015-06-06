name := """play-sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "mysql" % "mysql-connector-java" % "5.1.35",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.3.0-akka-2.3.x"
)


fork in run := true

PlayKeys.playWatchService := play.sbtplugin.run.PlayWatchService.sbt(pollInterval.value)

javaOptions in run += "-Dconfig.resource=application_local.conf"