package com.intersys.mdc.demo.reviews.service.streaming

import akka.{Done, NotUsed}
import akka.pattern.ask
import akka.actor.{ActorRef, Props}
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}
import akka.util.Timeout
import com.intersys.mdc.demo.reviews.service.pubsub.impl.Kafka
import com.intersys.mdc.demo.reviews.config._
import com.intersys.mdc.demo.reviews.model.Review
import com.intersys.mdc.demo.reviews.service.storage.Storage

import scala.concurrent.Future

case object Streaming extends Context {
  implicit val askTimeout: Timeout = Timeout(Settings.Akka.askTimeout)

  // Use an actor as the streaming source
  val actorSource: Source[Review, ActorRef] =
    Source.actorRef[Review](Settings.Akka.Source.bufferSize, akka.stream.OverflowStrategy.fail)

  // Use a publish-subscribe broker as a stream sink
  val pubSubSink: Sink[String, Future[Done]] = Kafka.publishStringSink
  
  val parallelism = 100
  val storageActor: ActorRef = actorSystem.actorOf(Props[Storage])
  val storageFlow: Flow[Review, String, NotUsed] =
    Flow[Review].mapAsync(parallelism)(review => (storageActor ? review).mapTo[Review])
      .map(_.toString)

  val graph: RunnableGraph[ActorRef] = actorSource.via(storageFlow).to(pubSubSink)

  val ref: ActorRef = graph.run()

}
