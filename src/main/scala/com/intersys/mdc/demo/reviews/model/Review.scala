package com.intersys.mdc.demo.reviews.model

import org.mongodb.scala.bson.ObjectId

case class Review(
                   _id: ObjectId,
                   id: Int,
                   clothingId: Int,
                   reviewerAge: Int,
                   title: String,
                   review: String,
                   rating: Int,
                   recommended: Boolean,
                   positiveFeedbackCount: Int,
                   division: String,
                   department: String,
                   `class`: String
                 ) extends MongoModel

object Review {
  def apply(
             id: Int,
             clothingId: Int,
             reviewerAge: Int,
             title: String,
             review: String,
             rating: Int,
             recommended: Boolean,
             positiveFeedback: Int,
             division: String,
             department: String,
             `class`: String): Review =
    Review(
      new ObjectId(),
      id,
      clothingId,
      reviewerAge,
      title,
      review,
      rating,
      recommended,
      positiveFeedback,
      division,
      department,
      `class`)
}