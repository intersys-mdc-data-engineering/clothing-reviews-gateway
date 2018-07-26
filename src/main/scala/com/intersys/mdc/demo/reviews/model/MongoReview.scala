package com.intersys.mdc.demo.reviews.model

import org.mongodb.scala.bson.ObjectId

case class MongoReview(_id: ObjectId, reviewerName: String, reviewText: String, overall: Int, summary: String,
                       asin: String, reviewerID: String, unixReviewTime: Long, reviewTime: String) extends MongoModel

object MongoReview {
  def apply(reviewerName: String, reviewText: String, overall: Int, summary: String, asin: String, reviewerID: String): MongoReview = {
    val unixTime: Long = java.time.Instant.now.getEpochSecond
    MongoReview(new ObjectId(), reviewerName, reviewText, overall, summary, asin, reviewerID, unixTime,
      new java.text.SimpleDateFormat("dd MM, yyyy").format(unixTime * 1000L)
    )
  }
}