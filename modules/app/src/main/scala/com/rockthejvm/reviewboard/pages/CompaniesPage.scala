package com.rockthejvm.reviewboard.pages

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import zio.*

import com.rockthejvm.reviewboard.components.*
import com.rockthejvm.reviewboard.common.*
import com.rockthejvm.reviewboard.domain.data.*

import sttp.model.Uri
import sttp.client3.impl.zio.FetchZioBackend
import sttp.tapir.client.sttp.SttpClientInterpreter

import com.rockthejvm.reviewboard.http.endpoints.CompanyEndpoints

object CompaniesPage {

  val companiesBus = EventBus[List[Company]]()

  def getCompaniesNaive() = {
    // Tapir HTTP client, returns ZIO instances, uses JS fetch
    val backend          = FetchZioBackend()
    val interpreter      = SttpClientInterpreter()
    val companyEndpoints = new CompanyEndpoints {} // works if you have it in the common module

    val endpointFunction =
      interpreter.toRequestThrowDecodeFailures(
        companyEndpoints.getAllEndpoint, // endpoint definition
        Some(Uri("localhost:8080"))
        // GET http://localhost:8080/companies
      )

    val request      = endpointFunction(())
    val companiesZIO = backend.send(request).map(_.body).absolve

    // run the ZIO
    Unsafe.unsafe { unsafe =>
      given u: Unsafe = unsafe
      Runtime.default.unsafe.fork(
        companiesZIO
          .tap(list => ZIO.attempt(companiesBus.emit(list)))
      )
    }
  }

  /*
      - server up
      - ~fastOptJS in SBT (project app)
      - npm run start in /modules/app
   */
  def apply() =
    sectionTag(
      // onMountCallback(_ => callBackend(_.companies.getAll(())).emitTo(companiesBus))
      onMountCallback(_ => getCompaniesNaive()),
      cls := "section-1",
      div(
        cls := "container company-list-hero",
        h1(
          cls := "company-list-title",
          "Companies"
        )
      ),
      div(
        cls := "container",
        div(
          cls := "row jvm-recent-companies-body",
          div(
            cls := "col-lg-12",
            children <-- companiesBus.events.map(companies =>
              companies.map(company => CompanyComponents.renderCompany(company))
            )
          )
        )
      )
    )
}
