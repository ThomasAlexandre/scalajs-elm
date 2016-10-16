package se.ta

import scala.scalajs.js.JSApp

import cats.Applicative
import cats.implicits._
import japgolly.scalajs.react._
import monix.reactive.Observable
import monix.reactive.subjects.BehaviorSubject
import org.scalajs.dom
import org.scalajs.dom.{MouseEvent, UIEvent, document}
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.concurrent.duration._
import monix.execution.Scheduler.Implicits.global

object SampleApp2 extends JSApp {

  implicit val app = new Applicative[Observable] {
    override def pure[A](x: A): Observable[A] = Observable(x)

    override def ap[A, B](ff: Observable[(A) => B])(fa: Observable[A]): Observable[B] =
      ff.combineLatest(fa).map{ case (f,a) => f(a)}
  }

  lazy val size: Observable[(Double,Double)] = {
    val sub = BehaviorSubject(dom.window.innerWidth -> dom.window.innerHeight)
    dom.window.onresize = (ev: UIEvent) => sub.onNext(dom.window.innerWidth -> dom.window.innerHeight)
    sub.whileBusyDropEvents
  }

  lazy val position: Observable[(Double,Double)] = {
    val sub = BehaviorSubject(0d -> 0d)
    dom.window.onmousemove = (ev: MouseEvent) => sub.onNext(ev.pageX -> ev.pageY)
    sub.whileBusyDropEvents
  }

  def view(size: (Double, Double), mouse: (Double, Double)) =
    <.div(
        <.p(s"Window size; width: ${size._1}, height: ${size._2}"),
        <.p(s"Mouse position; x: ${mouse._1}, y: ${mouse._2}")
    )

  def main(): Unit = {
    println("Composition between mouse position and window size")

    (size |@| position).map(view)  // Applicative functor will merge the two independent signals in parallel
      .doOnNext(x => ReactDOM.render(x, document.body))
      .dump("0")  // for debug
      .take(200)  // Will stop after 200 updates of either mouse position or window resize
      .subscribe()
  }
}
