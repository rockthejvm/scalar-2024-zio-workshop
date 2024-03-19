package com.rockthejvm.reviewboard.components

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import scala.scalajs.js.Date

object Footer {
  def apply() = div(
    cls := "main-footer",
    div(
      "Written in Scala with ❤️ at ",
      a(href := "https://rockthejvm.com", "Rock the JVM")
    ),
    div(s"© ${new Date().getFullYear()} all rights reserved.")
  )
}
