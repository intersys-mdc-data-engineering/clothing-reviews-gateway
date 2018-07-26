package com.intersys.mdc.demo.reviews.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Review(reviewerName: String, reviewText: String, overall: Int, summary: String, asin: String, reviewerID: String) {
  def toMongoReview: MongoReview =
    MongoReview(reviewerName, reviewText, overall, summary, asin, reviewerID)
}

object Review {
  object JsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val reviewFormat: RootJsonFormat[Review] = jsonFormat6(Review.apply)
  }
}