package com.intersys.mdc.demo.reviews.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Review(reviewerName: String, reviewerText: String, overall: Int, summary: String, asin: String, reviewerId: String) {
  def toMongoReview: MongoReview =
    MongoReview(reviewerName, reviewerText, overall, summary, asin, reviewerId)
}

object Review {
  object JsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val reviewFormat: RootJsonFormat[Review] = jsonFormat6(Review.apply)
  }
}