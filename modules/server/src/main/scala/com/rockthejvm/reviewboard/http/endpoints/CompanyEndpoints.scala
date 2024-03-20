package com.rockthejvm.reviewboard.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import sttp.tapir.EndpointIO.annotations.jsonbody

import com.rockthejvm.reviewboard.http.requests.*
import com.rockthejvm.reviewboard.domain.data.*

trait CompanyEndpoints {
  // POST /companies { CreateCompanyRequest } -> { Company }
  val createEndpoint =
    endpoint
      .tag("Companies")
      .name("create")
      .description("Create a listing for a company")
      .in("companies")
      .post
      .in(jsonBody[CreateCompanyRequest])
      .out(jsonBody[Company])

  // GET /companies -> { List[Company] }
  val getAllEndpoint =
    endpoint
      .tag("Companies")
      .name("get all")
      .description("get all company listings")
      .in("companies")
      .get
      .out(jsonBody[List[Company]])

  // GET /companies/3 -> { Company } or nothing
  // GET /companies/rock-the-jvm -> { Company } or nothing
  val getByIdEndpoint =
    endpoint
      .tag("Companies")
      .name("create")
      .description("Get a company by its id")
      .in("companies" / path[String]("id"))
      .get
      .out(jsonBody[Option[Company]])
}
