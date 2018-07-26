package com.intersys.mdc.demo.reviews.service.streaming

import akka.NotUsed
import akka.actor.{ActorRef, Props}
import akka.stream.scaladsl.GraphDSL.Builder
import akka.stream.ClosedShape
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}
import akka.util.Timeout
import com.intersys.mdc.demo.reviews.service.pubsub.impl.Kafka
import com.intersys.mdc.demo.reviews.config._
import com.intersys.mdc.demo.reviews.model.MongoReview
import com.intersys.mdc.demo.reviews.service.storage.Storage


case object Streaming extends Context {
  implicit val askTimeout: Timeout = Timeout(Settings.Akka.askTimeout)

  // Use an actor as the streaming source
  val actorSource: Source[MongoReview, ActorRef] =
    Source.actorRef[MongoReview](Settings.Akka.Source.bufferSize, Settings.Akka.Source.overflowStrategy)

  // Use a publish-subscribe broker as a stream sink
  val pubSubSink: Sink[MongoReview, NotUsed] = Flow[MongoReview].map(_.toString).to(Kafka.publishStringSink)

  // Use an actor as the streaming sink
  val storageActor: ActorRef = actorSystem.actorOf(Props[Storage])
  val onErrorMessage: Throwable => Storage.StreamFailure = (ex: Throwable) => Storage.StreamFailure(ex)
  val storageSink: Sink[MongoReview, NotUsed] = Sink.actorRefWithAck[MongoReview](storageActor,
    onInitMessage     = Storage.StreamInitialized,
    ackMessage        = Storage.Ack,
    onCompleteMessage = Storage.StreamCompleted,
    onFailureMessage  = onErrorMessage
  )

  // Create the runnable graph (sources to sinks)
  val graph: RunnableGraph[ActorRef] = RunnableGraph.fromGraph {
    GraphDSL.create(actorSource) { implicit builder: Builder[ActorRef] => actorSource =>
      import GraphDSL.Implicits._
      val bReceivedMongoReview = builder.add(Broadcast[MongoReview](outputPorts = 2))

      actorSource ~>  bReceivedMongoReview ~> pubSubSink
                      bReceivedMongoReview ~> storageSink
      ClosedShape
    }
  }

  val ref: ActorRef = graph.run()
}
