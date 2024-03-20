package com.rockthejvm.reviewboard.http.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint

import com.rockthejvm.reviewboard.http.endpoints.CompanyEndpoints
import com.rockthejvm.reviewboard.domain.data.Company
import com.rockthejvm.reviewboard.services.CompanyService
import com.rockthejvm.reviewboard.services.PaymentService
import com.stripe.model.PaymentIntentSearchResult

class CompanyController private (service: CompanyService, paymentService: PaymentService)
    extends BaseController
    with CompanyEndpoints {
  val create: ServerEndpoint[Any, Task] = createEndpoint
    .serverLogic { req =>
      val program = for {
        company    <- service.create(req)
        newCompany <- service.activate(company.id)
      } yield newCompany

      program.either
    }

  val getAll: ServerEndpoint[Any, Task] = getAllEndpoint
    .serverLogic(_ => service.getAll.either)

  val getById: ServerEndpoint[Any, Task] = getByIdEndpoint
    .serverLogic(id => service.getById(id.toLong).either)

  val premium: ServerEndpoint[Any, Task] = createPremium
    .serverLogic { req =>
      val program = for {
        company <- service.create(req)
        session <- paymentService
          .createCheckoutSession(company.id)
          .someOrFail(new RuntimeException("Cannot create payment session"))
      } yield session.getUrl()

      program.either
    }

  val webhook: ServerEndpoint[Any, Task] = webhookPremium
    .serverLogic { (signature, payload) =>
      paymentService
        .handleWebhookEvent(signature, payload, string => service.activate(string.toLong))
        .unit
        .either
    }

  override val routes: List[ServerEndpoint[Any, Task]] =
    List(premium, webhook, getAll, getById, create)
}

object CompanyController {
  def makeZIO = for {
    service        <- ZIO.service[CompanyService]
    paymentService <- ZIO.service[PaymentService]
  } yield new CompanyController(service, paymentService)
}
