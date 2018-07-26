package com.intersys.mdc.demo.reviews.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.http.scaladsl.server.Directives._
import com.intersys.mdc.demo.reviews.config.Context
import com.intersys.mdc.demo.reviews.model.Review
import com.intersys.mdc.demo.reviews.service.streaming.Streaming

case object GatewayController extends Context {
  import Review.JsonSupport._

  def sendToStream(review: Review): StandardRoute = {
    Streaming.ref ! review.toMongoReview
    complete(StatusCodes.OK)
  }

  val addRoute: Route = path("add") {
    get {
      parameters(
        'reviewerName.as[String],
        'reviewerText.as[String],
        'overall.as[Int],
        'summary.as[String],
        'asin.as[String],
        'reviewerId.as[String]
      ).as(Review.apply) { review =>
        sendToStream(review)
      }
    } ~ post {
      entity(as[Review]) { review =>
        sendToStream(review)
      }
    }
  }

  val route: Route = pathPrefix("review") {
    addRoute
  }
}
