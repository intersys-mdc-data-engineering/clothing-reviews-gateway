package com.intersys.mdc.demo.reviews.service.pubsub.impl

import java.util.Properties

import akka.Done
import akka.stream.scaladsl.Sink
import com.intersys.mdc.demo.reviews.config.Settings
import com.intersys.mdc.demo.reviews.service.pubsub.PubSub
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.concurrent.Future

case object Kafka extends PubSub {

  implicit class PropertiesUtil (properties: Properties) {
    def add(key: String, value: String): Properties = {
      properties.put(key, value)
      properties
    }
  }

  private val properties: Properties = new Properties()
    .add(Settings.Kafka.Label.bootstrapServer,  Settings.Kafka.Value.bootstrapServer)
    .add(Settings.Kafka.Label.keySerializer,    Settings.Kafka.Value.valueSerializer)
    .add(Settings.Kafka.Label.valueSerializer,  Settings.Kafka.Value.valueSerializer)

  val producer = new KafkaProducer[String, String](properties)

  def publishSink[T](key: String, topic: String): Sink[T, Future[Done]] = Sink.foreach[T]((element: T) => {
    val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, key, element.toString)
    producer.send(record)
  })

  override def publishStringSink: Sink[String, Future[Done]] = publishSink[String]("<key>", Settings.Kafka.topic)

  override def publishString(string: String): Unit = producer.send(
    new ProducerRecord[String, String](Settings.Kafka.topic, "<key>", string)
  )
}

