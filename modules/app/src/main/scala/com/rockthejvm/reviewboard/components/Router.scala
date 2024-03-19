package com.rockthejvm.reviewboard.components

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import frontroute.*

import com.rockthejvm.reviewboard.pages.*

object Router {
  val externalUrlBus = EventBus[String]()

  def apply() =
    mainTag(
      onMountCallback(ctx =>
        externalUrlBus.events.foreach(url => dom.window.location.href = url)(ctx.owner)
      ),
      routes(
        div(
          cls := "container-fluid",
          (pathEnd | path("companies")) {
            CompaniesPage()
          },
          path("post") {
            CreateCompanyPage()
          },
          path("company" / long) { companyId =>
            CompanyPage(companyId)
          },
          noneMatched {
            NotFoundPage()
          }
        )
      )
    )
}
