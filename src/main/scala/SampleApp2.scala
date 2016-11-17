package se.ta

import scala.concurrent.duration._
import scala.scalajs.js.JSApp

import cats.Applicative
import cats.implicits._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import monix.reactive.subjects.BehaviorSubject
import org.scalajs.dom
import org.scalajs.dom.{MouseEvent, UIEvent, document}

object SampleApp2 extends JSApp {

  implicit val app = new Applicative[Observable] {
    override def pure[A](x: A): Observable[A] = Observable(x)

    override def ap[A, B](ff: Observable[(A) => B])(fa: Observable[A]): Observable[B] =
      ff.combineLatest(fa).map { case (f, a) => f(a) }
  }

  lazy val time: Observable[Long] = Observable.interval(20.millis)  // one increment every 20ms

  lazy val position: Observable[(Double, Double)] = {
    val sub = BehaviorSubject(0d -> 0d)
    dom.window.onmousemove = (ev: MouseEvent) => sub.onNext(ev.pageX -> ev.pageY)
    sub.whileBusyDropEvents
  }

  lazy val size: Observable[(Double, Double)] = {
    val sub = BehaviorSubject(dom.window.innerWidth -> dom.window.innerHeight)
    dom.window.onresize = (ev: UIEvent) => sub.onNext(dom.window.innerWidth -> dom.window.innerHeight)
    sub.whileBusyDropEvents
  }

  def view(mousePosition: (Double, Double), time: Long, windowSize: (Double, Double)) = {
    val aspectRatio = 1.43
    <.div(
      <.p(s"Mouse position; x: ${mousePosition._1}, y: ${mousePosition._2}"),
      <.p(s"Window size; width: ${windowSize._1}, height: ${windowSize._2}"),
      <.img(
        ^.src := "scala_lego.jpg",
        ^.width := 143 + time * aspectRatio,
        ^.height := 100 + time
      )
    )
  }

  def main(): Unit = {
    println("Composition between mouse position and time")

    // Applicative functor will merge the independent signals in parallel
    (position |@| time |@| size).map(view)
      .doOnNext(x => ReactDOM.render(x, document.body))
      .dump("0")  // for debug
      .take(300)  // Will stop after 300 updates of either mouse positions, time tick, or window resize
      .subscribe()
  }
}
