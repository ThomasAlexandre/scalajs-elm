package se.ta

import scala.concurrent.duration._
import scala.scalajs.js.JSApp

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import org.scalajs.dom.document

object SampleApp1 extends JSApp {

  def viewTick(tick: Long) =
    <.div(
      <.p(s"Tick: $tick seconds")
    )

  val Tick =
    ReactComponentB[Long]("Tick")
      .render_P(tick => viewTick(tick))
      .build

  def main(): Unit = {

    println("Starting counting  10 ticks")

    Observable.interval(1 second)
      .doOnNext(x => ReactDOM.render(Tick(x), document.body))
      .take(10)
      .dump("0")
      .subscribe()
  }
}
