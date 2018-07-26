package com.intersys.mdc.demo.reviews.service.storage

import akka.actor.{Actor, ActorLogging}
import com.intersys.mdc.demo.reviews.config.{Context, Settings}
import com.intersys.mdc.demo.reviews.model.{MongoModel, MongoReview}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Storage {
  case object Ack
  case object StreamInitialized
  case object StreamCompleted
  final case class StreamFailure(ex: Throwable)
}

class Storage extends Actor with Context with ActorLogging {
  import Storage._

  val mongoClient: MongoClient      = MongoClient(Settings.Mongo.connection)
  val mongoDatabase: MongoDatabase  = mongoClient.getDatabase(Settings.Mongo.db).withCodecRegistry(MongoModel.codecRegistry)
  val mongoCollection: MongoCollection[MongoReview] = mongoDatabase.getCollection(Settings.Mongo.collectionName)

  override def receive: Receive = {
    case StreamInitialized =>
      log.info("Stream initialized.")
      sender() ! Ack
    case StreamCompleted =>
      log.info("Stream completed")
      sender() ! Ack
    case StreamFailure(ex) =>
      log.info("Stream failure: " + ex.toString)
    case review: MongoReview =>
      log.info("Receive review:" + review.toString)
      Await.ready(mongoCollection.insertOne(review).toFuture(), Duration.Inf).onComplete {
        case Success(_) =>
          log.info("Success:" + review.toString)
        case Failure(exception) =>
          log.info("Failure: " + exception.toString)
      }
      sender() ! Ack
    case _ =>
      log.info("Received unknown object.")
  }
}
