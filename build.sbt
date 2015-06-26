name := """trollbot"""

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0-RC4",
  "org.twitter4j" % "twitter4j-core" % "3.0.5",
  "org.twitter4j" % "twitter4j-stream" % "3.0.5",
  "com.typesafe" % "config" % "1.3.0"
)
