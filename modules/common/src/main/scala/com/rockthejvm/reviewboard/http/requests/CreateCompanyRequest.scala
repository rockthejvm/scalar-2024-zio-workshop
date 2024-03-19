package com.rockthejvm.reviewboard.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.rockthejvm.reviewboard.domain.data.Company

final case class CreateCompanyRequest(
    name: String,
    url: String,
    location: Option[String] = None,
    country: Option[String] = None,
    industry: Option[String] = None,
    image: Option[String] = None,
    tags: Option[List[String]] = None
) {
  def toCompany(id: Long) =
    Company(
      id,
      Company.makeSlug(name),
      name,
      url,
      location,
      country,
      industry,
      image,
      tags.getOrElse(List())
    )
}

object CreateCompanyRequest {
  given codec: JsonCodec[CreateCompanyRequest] = DeriveJsonCodec.gen[CreateCompanyRequest]
}
