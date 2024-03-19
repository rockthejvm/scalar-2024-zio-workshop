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

object CompaniesPage {

  def apply() =
    sectionTag(
      // TODO load all companies here
      cls := "section-1",
      div(
        cls := "container company-list-hero",
        h1(
          cls := "company-list-title",
          "Rock the JVM Companies Board"
        )
      ),
      div(
        cls := "container",
        div(
          cls := "row jvm-recent-companies-body",
          div(
            cls := "col-lg-12"
            // TODO render companies based on the backend call
          )
        )
      )
    )
}
