package com.intersys.mdc.demo.reviews.controller

import akka.http.scaladsl.model.{HttpHeader, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._
import com.intersys.mdc.demo.reviews.config.Context
import com.intersys.mdc.demo.reviews.model.Review
import com.intersys.mdc.demo.reviews.service.streaming.Streaming

case object GatewayController extends Context {
  import Review.JsonSupport._

  private val corsResponseHeaders: List[HttpHeader] = List(
    `Access-Control-Allow-Origin`.*,
    `Access-Control-Allow-Methods`(POST, GET),
    `Access-Control-Allow-Headers`("Origin", "Accept", "Content-Type", "X-Requested-With", "X-CSRF-Token")
  )

  def sendToStream(review: Review): Route = {
    Streaming.ref ! review.toMongoReview
    complete(HttpResponse(StatusCodes.OK).withHeaders(corsResponseHeaders))
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
