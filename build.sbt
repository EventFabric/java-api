organization := "com.eventfabric"

name := "EventFabricAPIClient"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.3"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

resolvers += "Maven2 Repository" at "http://repo2.maven.org/maven2/"

resolvers += "Apache Maven2 Repository" at "http://repo1.maven.org/maven2/"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

libraryDependencies ++= Seq(
    "commons-logging" % "commons-logging" % "1.1.3",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.slf4j" % "slf4j-log4j12" % "1.7.5",
    "org.scalatest" % "scalatest_2.9.3" % "2.0.M5b" % "test",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.2.2",
    "org.apache.httpcomponents" % "httpclient" % "4.2.5",
    "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13",
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13"
)
