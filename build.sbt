name := "StreamIOUtils"

version := "1.0"

organization := "com.danny"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.1.0",
  "net.sf.opencsv" % "opencsv" % "2.0",
  "org.scalaz" % "scalaz-core_2.10" % "7.1.0-M5",
  "javax.validation" % "validation-api" % "1.0.0.GA",
  "org.hibernate" % "hibernate-validator" % "4.2.0.Final",
  "org.scalaz.stream" %% "scalaz-stream" % "0.5a",
  "com.google.guava" % "guava" % "17.0",
  "com.google.code.findbugs" % "jsr305" % "1.3.9"
//  "org.hibernate" % "hibernate-validator" % "5.0.1.Final",
//  "javax.el" % "el-api" % "2.2",
//  "org.glassfish.web" % "el-impl" % "2.2.1-b05"
//  ,
//  "org.scalacheck" % "scalacheck_2.10" % "1.10.1",
//  "org.scalaz" % "scalaz-core_2.10" % "7.1.0-M5",
//  "com.chuusai" % "shapeless_2.10.4" % "2.0.0",
//  //  "com.typesafe.akka" % "akka-actor_2.10" % "2.3.0",
//  "org.specs2" % "specs2_2.10" % "2.3.11-scalaz-7.1.0-M6",
//  "org.mockito" % "mockito-all" % "1.9.5",
//  "org.scala-sbt" % "io" % "0.13.2",
//  "org.scala-lang.plugins" % "continuations" % "2.10.3",
//  "org.openimaj" % "image-processing" % "1.2.1",
//  "com.netflix.rxjava" % "rxjava-scala" % "0.20.0-RC3",
//  "com.typesafe.play" % "play-iteratees_2.10" % "2.3.0",
//  "com.typesafe.akka" % "akka-actor_2.10" % "2.3.5",
//  "com.typesafe.akka" % "akka-stream-experimental_2.10" % "0.5",
//  "org.openimaj" % "image-processing" % "1.2.1",
//  "org.json4s" % "json4s-native_2.10" % "3.2.10",
//  "org.json4s" % "json4s-jackson_2.10" % "3.2.10"
)

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"