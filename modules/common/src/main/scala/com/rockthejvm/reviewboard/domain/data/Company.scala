package com.rockthejvm.reviewboard.domain.data

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class Company(
    id: Long,
    slug: String,
    name: String,
    url: String,
    location: Option[String] = None,
    country: Option[String] = None,
    industry: Option[String] = None,
    image: Option[String] = None,
    tags: List[String] = List()
)

object Company {
  given codec: JsonCodec[Company] = DeriveJsonCodec.gen[Company]

  def makeSlug(name: String): String =
    name.toLowerCase.trim
      .replaceAll(" +", " ")
      .replaceAll(" ", "-")

  val dummy = Company(1, "rock-the-jvm", "Rock the JVM", "https://rockthejvm.com")
}
