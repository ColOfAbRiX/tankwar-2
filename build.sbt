/*
 * Copyright (C) 2018 Fabrizio Colonna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
/*
 * Tankwar SBT Build File
 * Ref: http://www.scala-sbt.org/0.13.0/docs/Examples/Quick-Configuration-Examples.html
 */

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform._
import de.johoop.cpd4sbt.CopyPasteDetector._
import sbt.File
import sbt.Keys._
import wartremover.WartRemover.autoImport._

// Project Definition

name := "TankWar"
version := "2.0.0"
scalaVersion := "2.12.4"
mainClass in Compile := Some("com.colofabrix.scala.tankwar.TankWar")
fork := true

// Dependencies
resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  // Scalaz
  "org.scalaz" %% "scalaz-core" % "7.2.20",
  "org.scalaz" %% "scalaz-effect" % "7.2.20",

  // Genetic Algorithms (A fork of the Watchmaker framework is included in another file)
  //"org.uncommons" % "uncommons-maths" % "1.2",

  // Linear Algebra
  //"org.scalanlp" %% "breeze" % "latest.integration",

  // Neural network and deep learning
  //"org.deeplearning4j" % "deeplearning4j-core" % "latest.integration"

  // LWJGL OpenGL libraries
  "org.lwjgl.lwjgl" % "lwjgl-platform" % "latest.integration" classifier "natives-windows" classifier "natives-linux" classifier "natives-osx",
  "slick-util" % "slick-util" % "1.0.0" from "http://slick.ninjacave.com/slick-util.jar",
  "org.lwjgl.lwjgl" % "lwjgl_util" % "latest.integration",

  // Charts
  //"com.github.wookietreiber" %% "scala-chart" % "latest.integration",

  // Logging
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",

  // Utilities
  "com.typesafe" % "config" % "1.3.2",
  //"org.scala-lang.modules" %% "scala-pickling" % "latest.integration",
  //"com.github.scopt" %% "scopt" % "latest.integration",

  // Testing
  "org.scalatest" % "scalatest_2.11" % "latest.integration" % "test"
  //"com.storm-enroute" %% "scalameter" % "latest.integration"
)

// Scala compiler options.
// See: https://tpolecat.github.io/2014/04/11/scalac-flags.html

scalacOptions ++= Seq(
  "-Xmax-classfile-name", "72",
  "-encoding", "UTF-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  //"-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-language:implicitConversions",
  "-language:existentials",
  "-language:higherKinds",
  "-Ywarn-value-discard",
  "-Ywarn-unused-import",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code"
)

// JVM Options.
// See: http://blog.sokolenko.me/2014/11/javavm-options-production.html
javaOptions ++= Seq(
  "-server",
  "-Xverify:none",
  // Memory settings
  "-XX:InitialHeapSize=64M",
  "-XX:MaxHeapSize=128M",
  "-XX:NewRatio=1",
  "-XX:MetaspaceSize=4M",
  "-XX:MaxMetaspaceSize=16M",
  "-XX:SurvivorRatio=5",
  "-XX:CompressedClassSpaceSize=2M",
  "-XX:+UseCompressedOops",
  "-XX:+UseCompressedClassPointers",
  // Other settings
  s"-Djava.library.path=${unmanagedBase.value}",
  "-Dfile.encoding=UTF-8"
)

// Scaladoc configuration

target in (Compile, compile) in doc := baseDirectory.value / "docs"

// Native libraries extraction - LWJGL has some native libraries provided as JAR files that I have to extract

compile in Compile <<= (compile in Compile).dependsOn(Def.task {
  val r = "^(\\w+).*".r
  val r(os) = System.getProperty( "os.name" )

  val jars = ( update in Compile ).value
    .select( configurationFilter( "compile" ) )
    .filter( _.name.contains( os.toLowerCase ) )

  jars foreach { jar =>
    println( s"[info] Processing '${jar.getName}' and saving to '${unmanagedBase.value}'" )
    IO.unzip(  jar, unmanagedBase.value )
  }

  Seq.empty[File]
})

// Scalameter

testFrameworks += new TestFramework( "org.scalameter.ScalaMeterFramework" )

logBuffered := false
parallelExecution in Test := false

// Code Style

// UTF-8 support is broken?
/*
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
(compile in Compile) <<= (compile in Compile) dependsOn compileScalastyle
*/

scalafmtConfig := (resourceDirectory in Compile).value / "scalafmt.conf"

scalastyleConfig := file( s"${sourceDirectory.value}/main/resources/scalastyle-config.xml" )

SbtScalariform.scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(SpacesAroundMultiImports, true)
    .setPreference(DanglingCloseParenthesis, Force)
    .setPreference(CompactControlReadability, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
)

// Code Quality

wartremoverExcluded ++= (sourceDirectory.value ** "*old*" ** "*.scala").get
wartremoverErrors in (Compile, compile) ++= Seq(
  Wart.AsInstanceOf,
  Wart.EitherProjectionPartial,
  //Wart.IsInstanceOf,
  Wart.Nothing,
  Wart.Null,
  Wart.OptionPartial,
  Wart.Product,
  Wart.Serializable
)
wartremoverWarnings in (Compile, compile) ++= Seq(
  Wart.EitherProjectionPartial,
  Wart.Enumeration,
  Wart.FinalCaseClass,
  Wart.JavaConversions,
  Wart.MutableDataStructures,
  Wart.Option2Iterable,
  Wart.Serializable,
  Wart.ToString,
  Wart.TryPartial,
  Wart.Var
)

coverageMinimum := 75
coverageFailOnMinimum := true

com.markatta.sbttaglist.TagListPlugin.tagListSettings
