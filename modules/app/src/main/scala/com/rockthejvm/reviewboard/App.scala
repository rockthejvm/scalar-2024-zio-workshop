package com.rockthejvm.reviewboard

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import scala.util.Try
import com.raquo.airstream.ownership.OneTimeOwner

import com.rockthejvm.reviewboard.components.*
import frontroute.LinkHandler

object App {

  val app = div(
    Header(),
    Router(),
    Footer()
  ).amend(LinkHandler.bind) // for internal links

  def main(args: Array[String]): Unit = {
    val containerNode = dom.document.querySelector("#app")
    render(
      containerNode,
      app
    )
  }
}
