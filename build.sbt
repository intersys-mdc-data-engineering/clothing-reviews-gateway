import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.intersys.mdc.demo.reviews",
      scalaVersion := "2.12.6",
      version      := "0.0.0"
    )),
    name := "clothing-reviews-gateway",
    logLevel := Level.Warn,
    libraryDependencies ++= {
      val mongoVersion     = "2.3.0"
      val mongoBsonVersion = "3.7.0"
      val nettyVersion = "4.1.17.Final"
      val kafkaVersion = "1.1.1"
      val akkaHttpVersion = "10.1.1"
      val akkaVersion     = "2.5.12"
      val configVersion   = "1.3.1"
      val logbackVersion  = "1.2.3"
      val loggingVersion  = "3.8.0"
      Seq(
        // Configuration
        "com.typesafe" % "config" % configVersion,
        // Mongo
        "org.mongodb.scala" %% "mongo-scala-driver" % "2.3.0",
        "io.netty" % "netty-all" % nettyVersion,
        "org.mongodb" % "bson" % mongoBsonVersion,
        // Akka Toolkit
        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-stream" % akkaVersion,
        "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
        // Kafka
        "org.apache.kafka" %% "kafka" % kafkaVersion,
        // Logging
        "ch.qos.logback" % "logback-classic" % logbackVersion,
        "com.typesafe.scala-logging" %% "scala-logging" % loggingVersion,
        // Tests
        "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
        scalaTest % Test
      )
    }
  )
