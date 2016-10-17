# Scala.js in Elm-style

## Purpose
To illustrate the excellent blog post [Scala.js a la Elm](https://medium.com/@mark_dj/a-little-scala-js-experiment-4a8da7b8ab8c#.so9srvcwf) showing some demos on how to create reactive UIs by composing independent signals (ie observable streams) through an applicative functor (taken from the typelevel cats library).


## Technologies
  * [Scala.js](https://www.scala-js.org)
  * [Scalajs-react components](https://github.com/japgolly/scalajs-react)
  * [Monix Observable](https://monix.io)
  * [Cats Applicative Functors](http://typelevel.org/cats/)

## How to run

```
> sbt
> ~fastOptJS
```

Open the following 2 demos in a web browser:
  
  * index-dev1.html: A simple clock updated every second
  The observable stops after 10 seconds 
  
  * index-dev2.html: A simple display of the composition of a mouse position and window size
  The observable stops after 200 updated values
