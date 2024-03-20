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
    tags: List[String] = List()
) derives JsonCodec {
  def toCompany: Company = Company(
    -1, // the DB will allocate this
    Company.makeSlug(name),
    name,
    url,
    location,
    country,
    industry,
    image,
    tags
  )
}
