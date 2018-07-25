package com.intersys.mdc.demo.reviews.service.storage

import akka.actor.Actor
import com.intersys.mdc.demo.reviews.config.{Context, Settings}
import com.intersys.mdc.demo.reviews.model.{MongoModel, Review}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


class Storage extends Actor with Context {

  val mongoClient: MongoClient      = MongoClient(Settings.Mongo.connection)
  val mongoDatabase: MongoDatabase  = mongoClient.getDatabase(Settings.Mongo.db).withCodecRegistry(MongoModel.codecRegistry)
  val mongoCollection: MongoCollection[Review] = mongoDatabase.getCollection(Settings.Mongo.collectionName)

  override def receive: Receive = {
    case review: Review =>
      Await.ready(mongoCollection.insertOne(review).toFuture(), Duration.Inf).onComplete {
        case Success(_) =>
          println("Success:" + review.toString)
        case Failure(exception) =>
          println("Failure: " + exception.toString)
      }
      println("closing mongo connection")
      sender() ! review
  }
}