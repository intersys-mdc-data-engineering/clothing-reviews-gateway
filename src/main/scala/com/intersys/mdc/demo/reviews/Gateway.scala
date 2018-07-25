package com.intersys.mdc.demo.reviews

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.intersys.mdc.demo.reviews.config.{Context, Settings}
import com.intersys.mdc.demo.reviews.controller.GatewayController

object Gateway extends Context {

  val route: Route = GatewayController.route ~ pathPrefix(Settings.Api.version) {GatewayController.route}

  def main(args: Array[String]): Unit = {
    Http().bindAndHandle(route, Settings.Http.host, Settings.Http.port)
  }
}
