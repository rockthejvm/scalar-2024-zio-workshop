package com.rockthejvm.reviewboard.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class CreateCompanyRequest(
    name: String,
    url: String,
    location: Option[String] = None,
    country: Option[String] = None,
    industry: Option[String] = None,
    image: Option[String] = None,
    tags: List[String] = List()
) derives JsonCodec
