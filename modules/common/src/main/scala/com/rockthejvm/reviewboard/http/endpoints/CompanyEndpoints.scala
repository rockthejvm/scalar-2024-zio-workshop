package com.rockthejvm.reviewboard.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class derivation package

import sttp.tapir.EndpointIO.annotations.jsonbody
import com.rockthejvm.reviewboard.domain.data.*
import com.rockthejvm.reviewboard.http.requests.CreateCompanyRequest
import sttp.model.MediaType
import sttp.model.Header

trait CompanyEndpoints extends BaseEndpoint {
  val createEndpoint =
    baseEndpoint
      .tag("companies")
      .name("create")
      .description("create a listing for a company")
      .in("companies")
      .post
      .in(jsonBody[CreateCompanyRequest])
      .out(jsonBody[Company])

  val getAllEndpoint =
    baseEndpoint
      .tag("companies")
      .name("getAll")
      .description("get all company listings")
      .in("companies")
      .get
      .out(jsonBody[List[Company]])

  val getByIdEndpoint =
    baseEndpoint
      .tag("companies")
      .name("getById")
      .description("get company by its id (or maybe by slug?)")
      .in("companies" / path[String]("id"))
      .get
      .out(jsonBody[Option[Company]])

  // stripe endpoints
  val createPremiumEndpoint =
    baseEndpoint
      .name("add company (promoted)")
      .description("Add premium company (paid via Stripe)")
      .in("paid" / "company")
      .post
      .in(jsonBody[CreateCompanyRequest])
      .out(jsonBody[String]) // this is the Stripe checkout URL

  // webhook - will be called automatically by Stripe
  val webhookEndpoint =
    baseEndpoint
      .name("add company webhook")
      .description("Confirm the purchase of a promoted post")
      .in("paid" / "webhook")
      .post
      .in(header[String]("Stripe-Signature"))
      .in(stringBody)
}
