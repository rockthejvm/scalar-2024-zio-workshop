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
      Tutorial()
    )
  }
}

object MyComponent {
  def apply() =
    div(
      p("This is an app"),
      p("Laminar is quite cool")
    )
}

object Tutorial {
  // Airstream

  // 1 - EventStream - read-only stream of events
  val ticks: EventStream[Int]         = EventStream.periodic(1000)
  val timeStatus: EventStream[String] = ticks.map(n => s"$n seconds")

  val timeUpdated =
    div(
      // child
      span("Time since loaded: "),
      // 3 - modifier
      child.text <-- timeStatus
    )

  // 2 - EventBus - writeable stream of events
  val clickBus    = EventBus[Int]()
  val clickEvents = clickBus.events.scanLeft(0)(_ + _) // EventStream[Int]

  val clickUpdated =
    div(
      span("Clicks since loaded: "),
      child.text <-- clickEvents.map(n => s"$n clicks"),
      button(
        `type`    := "button",
        styleAttr := "display: block",
        onClick.mapTo(1) --> clickBus,
        "Click me"
      )
    )

  // 3 - Signal = read-only stream with state ("latest value")
  val queryBus      = EventBus[Unit]()
  val queriedEvents = clickEvents.observe(new OneTimeOwner(() => ()))

  val queryUpdated =
    div(
      span("Clicks since loaded: "),
      child.text <-- queryBus.events.map(_ => s"${queriedEvents.now()} clicks"),
      button(
        `type`    := "button",
        styleAttr := "display: block",
        onClick.mapTo(1) --> clickBus,
        "Click me"
      ),
      button(
        `type`    := "button",
        styleAttr := "display: block",
        onClick.mapToUnit --> queryBus,
        "Show me"
      )
    )

  // 4 - read/write reactive variable = Var
  val countVar = Var[Int](0)

  val clicksUpdated_2 =
    div(
      span("Clicks since loaded: "),
      child.text <-- countVar.signal.map(value => s"$value clicks"),
      button(
        `type`    := "button",
        styleAttr := "display: block",
        // onClick --> countVar.updater((currentValue, event) => currentValue + 1),
        // onClick --> countVar.writer.contramap(event => countVar.now() + 1),
        onClick --> (event => countVar.set(countVar.now() + 1)),
        "Click me"
      )
    )

  def apply() =
    div(
      // 1 - attributes
      styleAttr := "color: red", // <div style="color:red"></div>
      // 2 - children
      "Laminar rocks",
      clicksUpdated_2
    )

}
