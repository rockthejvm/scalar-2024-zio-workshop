package com.rockthejvm.reviewboard.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import sttp.tapir.EndpointIO.annotations.jsonbody

import com.rockthejvm.reviewboard.http.requests.*
import com.rockthejvm.reviewboard.domain.data.*

trait CompanyEndpoints extends BaseEndpoint {
  // POST /companies { CreateCompanyRequest } -> { Company }
  val createEndpoint =
    baseEndpoint
      .tag("Companies")
      .name("create")
      .description("Create a listing for a company")
      .in("companies")
      .post
      .in(jsonBody[CreateCompanyRequest])
      .out(jsonBody[Company])

  // GET /companies -> { List[Company] }
  val getAllEndpoint =
    baseEndpoint
      .tag("Companies")
      .name("get all")
      .description("get all company listings")
      .in("companies")
      .get
      .out(jsonBody[List[Company]])

  // GET /companies/3 -> { Company } or nothing
  // GET /companies/rock-the-jvm -> { Company } or nothing
  val getByIdEndpoint =
    baseEndpoint
      .tag("Companies")
      .name("create")
      .description("Get a company by its id")
      .in("companies" / path[String]("id"))
      .get
      .out(jsonBody[Option[Company]])

  // premium Stripe endpoints
  // POST /premium
  val createPremium =
    baseEndpoint
      .tag("Companies")
      .name("create premium")
      .description("PREMIUM listing of company")
      .in("premium")
      .post
      .in(jsonBody[CreateCompanyRequest])
      .out(stringBody) // TODO

  val webhookPremium =
    baseEndpoint
      .tag("Companies")
      .name("webhook")
      .description("The stuff Stripe calls back to me")
      .in("premium" / "webhook")
      .post
      .in(header[String]("Stripe-Signature"))
      .in(stringBody)

  /*
    call an endpoint (mine)
      insert a new company (tentatively) into the database
      create a Stripe checkout session -> URL
      return that URL
    frontend reroutes to that URL (checkout.stripe.com/....)
    enter CC details & pay
    Stripe calls another endpoint (mine) = webhook
      validate the purchase
   */
}
