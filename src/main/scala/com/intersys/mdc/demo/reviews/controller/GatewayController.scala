package com.intersys.mdc.demo.reviews.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.intersys.mdc.demo.reviews.config.Context
import com.intersys.mdc.demo.reviews.model.Review
import com.intersys.mdc.demo.reviews.service.streaming.Streaming

case object GatewayController extends Context {

  val addRoute: Route = path("add") {
    get {
      parameters(
        'id.as[Int],
        'clothingId.as[Int],
        'reviewerAge.as[Int],
        'title.as[String],
        'review.as[String],
        'rating.as[Int],
        'recommended.as[Boolean],
        'positiveFeedbackCount.as[Int],
        'division.as[String],
        'department.as[String],
        'class.as[String]) {
        (id, clothingId, reviewerAge, title, review, rating, recommended, positiveFeedbackCount, division, department, `class`) =>
          val rev = Review(id, clothingId, reviewerAge, title, review, rating, recommended, positiveFeedbackCount, division, department, `class`)
          Streaming.ref ! rev
        complete(StatusCodes.OK)
      }
    }
  }

  val route: Route = pathPrefix("review") {
    addRoute
  }

}
