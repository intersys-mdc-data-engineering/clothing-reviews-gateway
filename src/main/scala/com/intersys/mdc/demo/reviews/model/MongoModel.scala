package com.intersys.mdc.demo.reviews.model

import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry

trait MongoModel {
  def _id: ObjectId
}

object MongoModel {
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[MongoReview]), DEFAULT_CODEC_REGISTRY)
}
