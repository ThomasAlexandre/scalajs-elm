enablePlugins(ScalaJSPlugin)

name:="scalajs-elm"

scalaVersion := "2.11.7" // or any other Scala version >= 2.10.2

scalaJSUseRhino in Global := false

libraryDependencies ++= Seq(
  "io.monix" %%% "monix" % "2.0.4",
  "org.typelevel" %%% "cats" % "0.7.2",
  "com.github.japgolly.scalajs-react" %%% "core" % "0.11.2",
  "org.scala-js" %%% "scalajs-dom" % "0.9.0"
)

skip in packageJSDependencies := false

// React JS itself (Note the filenames, adjust as needed, eg. to remove addons.)
jsDependencies ++= Seq(
  "org.webjars.bower" % "react" % "15.3.2"
    /        "react-with-addons.js"
    minified "react-with-addons.min.js"
    commonJSName "React",

  "org.webjars.bower" % "react" % "15.3.2"
    /         "react-dom.js"
    minified  "react-dom.min.js"
    dependsOn "react-with-addons.js"
    commonJSName "ReactDOM",

  "org.webjars.bower" % "react" % "15.3.2"
    /         "react-dom-server.js"
    minified  "react-dom-server.min.js"
    dependsOn "react-dom.js"
    commonJSName "ReactDOMServer")
