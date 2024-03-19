package com.rockthejvm.reviewboard.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.rockthejvm.reviewboard.domain.data.Review

final case class CreateReviewRequest(
    companyId: Long,
    management: Int,
    culture: Int,
    salary: Int,
    benefits: Int,
    wouldRecommend: Int,
    review: String
)

object CreateReviewRequest {
  given codec: JsonCodec[CreateReviewRequest] = DeriveJsonCodec.gen[CreateReviewRequest]
  def fromReview(review: Review) = CreateReviewRequest(
    companyId = review.companyId,
    management = review.management,
    culture = review.culture,
    salary = review.salary,
    benefits = review.benefits,
    wouldRecommend = review.wouldRecommend,
    review = review.review
  )
}
