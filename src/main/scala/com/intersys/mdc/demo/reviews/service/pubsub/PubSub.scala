package com.intersys.mdc.demo.reviews.service.pubsub

import akka.Done
import akka.stream.scaladsl.Sink

import scala.concurrent.Future

trait PubSub {
  def publishStringSink: Sink[String, Future[Done]]
  def publishString(string: String): Unit
}
