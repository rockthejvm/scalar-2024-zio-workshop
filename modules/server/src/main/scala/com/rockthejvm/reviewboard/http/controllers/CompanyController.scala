package com.rockthejvm.reviewboard.http.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint

import com.rockthejvm.reviewboard.http.endpoints.CompanyEndpoints
import com.rockthejvm.reviewboard.domain.data.Company
import com.rockthejvm.reviewboard.services.CompanyService

class CompanyController private (service: CompanyService)
    extends BaseController
    with CompanyEndpoints {
  val create: ServerEndpoint[Any, Task] = createEndpoint
    .serverLogic(req => service.create(req).either) // I => Task[Either[E, A]]

  val getAll: ServerEndpoint[Any, Task] = getAllEndpoint
    .serverLogic(_ => service.getAll.either)

  val getById: ServerEndpoint[Any, Task] = getByIdEndpoint
    .serverLogic(id => service.getById(id.toLong).either)

  override val routes: List[ServerEndpoint[Any, Task]] = List(getAll, getById, create)
}

object CompanyController {
  def makeZIO = for {
    service <- ZIO.service[CompanyService]
  } yield new CompanyController(service)
}
