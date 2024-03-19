package com.rockthejvm.reviewboard.pages

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object NotFoundPage {
  def apply() =
    div(
      cls := "simple-titled-page",
      h1("Oops!"),
      h2("This page can't be found."),
      div("You lost, friend?")
    )
}
