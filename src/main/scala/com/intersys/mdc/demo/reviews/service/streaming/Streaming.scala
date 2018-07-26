package com.intersys.mdc.demo.reviews.service.streaming

import akka.{Done, NotUsed}
import akka.pattern.ask
import akka.actor.{ActorRef, Props}
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}
import akka.util.Timeout
import com.intersys.mdc.demo.reviews.service.pubsub.impl.Kafka
import com.intersys.mdc.demo.reviews.config._
import com.intersys.mdc.demo.reviews.model.MongoReview
import com.intersys.mdc.demo.reviews.service.storage.Storage

import scala.concurrent.Future

case object Streaming extends Context {
  implicit val askTimeout: Timeout = Timeout(Settings.Akka.askTimeout)

  // Use an actor as the streaming source
  val actorSource: Source[MongoReview, ActorRef] =
    Source.actorRef[MongoReview](Settings.Akka.Source.bufferSize, Settings.Akka.Source.overflowStrategy)

  // Use a publish-subscribe broker as a stream sink
  val pubSubSink: Sink[String, Future[Done]] = Kafka.publishStringSink

  // Flow
  val parallelism = 100
  val storageActor: ActorRef = actorSystem.actorOf(Props[Storage])
  val storageFlow: Flow[MongoReview, String, NotUsed] =
    Flow[MongoReview].mapAsync(parallelism)(review => (storageActor ? review).mapTo[MongoReview])
      .map(_.toString)

  val graph: RunnableGraph[ActorRef] = actorSource.via(storageFlow).to(pubSubSink)

  val ref: ActorRef = graph.run()

}
