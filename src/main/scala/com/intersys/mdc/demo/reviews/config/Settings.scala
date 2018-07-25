package com.intersys.mdc.demo.reviews.config

import com.typesafe.config.{Config, ConfigFactory}
import akka.stream.OverflowStrategy
import scala.concurrent.duration._

object Settings {
  private val app: Config = ConfigFactory.load().getConfig("application")
  object Http {
    private val http: Config = app.getConfig("http")
    val host: String = http.getString("host")
    val port: Int    = http.getInt("port")
  }
  object Api {
    private val api: Config = app.getConfig("api")
    val version: String     = api.getString("version")
  }
  object Akka {
    private val akka: Config = app.getConfig("akka")
    val askTimeout: FiniteDuration = akka.getInt("askTimeout").second
    object Source {
      private val source: Config = akka.getConfig("source")
      val bufferSize: Int = source.getInt("bufferSize")
      val overflowStrategy: OverflowStrategy = source.getString("overflowStrategy") match {
        case "backpressure" => OverflowStrategy.backpressure
        case "dropBuffer" => OverflowStrategy.dropBuffer
        case "dropHead" => OverflowStrategy.dropHead
        case "dropTail" => OverflowStrategy.dropTail
        case _ => OverflowStrategy.fail
      }
    }
  }
  object Kafka {
    private val kafka: Config = app.getConfig("kafka")
    val topic: String = kafka.getString("topic")
    object Label {
      private val label: Config     = kafka.getConfig("label")
      val bootstrapServer: String   = label.getString("bootstrapServer")
      val keySerializer: String     = label.getString("keySerializer")
      val valueSerializer: String   = label.getString("valueSerializer")
    }
    object Value {
      private val value: Config     = kafka.getConfig("value")
      val bootstrapServer: String   = value.getString("bootstrapServer")
      val keySerializer: String     = value.getString("keySerializer")
      val valueSerializer: String   = value.getString("valueSerializer")
    }
  }
  object Mongo {
    private val mongo: Config = app.getConfig("mongo")
    val user: String    = mongo.getString("user")
    val pwd: String     = mongo.getString("pwd")
    val port: String    = mongo.getString("port")
    val cluster: String = mongo.getString("cluster")
    val db: String      = mongo.getString("database")
    val connection: String = s"mongodb://$user:$pwd@$cluster:$port/admin"
    val collectionName: String = mongo.getString("collectionName")
  }
}
