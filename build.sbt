import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.intersys.mdc.demo.reviews",
      scalaVersion := "2.12.6",
      version      := "0.0.0"
    )),
    name := "clothing-reviews-gateway",
    libraryDependencies ++= {
      val mongoVersion = "2.3.0"
      val mongoBsonVersion = "2.3"
      val nettyVersion = "4.1.17.Final"
      val kafkaVersion = "1.1.1"
      val akkaHttpVersion = "10.1.1"
      val akkaVersion = "2.5.12"
      val configVersion = "1.3.1"
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
        // Tests
        "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
        scalaTest % Test
      )
    }
  )
