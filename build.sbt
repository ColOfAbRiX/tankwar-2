/*
 * Copyright (C) 2015 Fabrizio Colonna
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

import de.johoop.cpd4sbt.CopyPasteDetector._
import sbt.Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform._

// Projecty Definition
lazy val root = (project in file(".")).
 settings(
    name := "TankWar",
    version := "0.2.0",
    scalaVersion := "2.11.7",
    mainClass in Compile := Some("com.colofabrix.scala.TankWarMain")
  )

// Dependencies
libraryDependencies ++= Seq(
  "org.lwjgl.lwjgl" % "lwjgl-platform" % "2.9.0" classifier "natives-windows" classifier "natives-linux" classifier "natives-osx",
  "slick-util" % "slick-util" % "1.0.0" from "http://slick.ninjacave.com/slick-util.jar",
  "org.uncommons.watchmaker" % "watchmaker-framework" % "0.7.1",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.uncommons" % "uncommons-maths" % "1.2",
  "org.lwjgl.lwjgl" % "lwjgl_util" % "2.9.0"
)

// Scala compiler options
//   Ref: https://tpolecat.github.io/2014/04/11/scalac-flags.html
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
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused-import",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code"
)

// SBT > 0.13.6 way of specifiying JVM options
//   Ref: http://stackoverflow.com/questions/3868863/how-to-specify-jvm-maximum-heap-size-xmx-for-running-an-application-with-run
val buildSettings = Defaults.defaultSettings ++ Seq(
  javaOptions += s""
)

// Native libraries extraction - LWJGL has some native libraries provided as JAR files that I have to extract
compile in Compile <<= (compile in Compile).dependsOn(Def.task {
  val r = "^(\\w+).*".r
  val r(os) = System.getProperty( "os.name" )

  val jars = ( update in Compile ).value
    .select( configurationFilter( "compile" ) )
    .filter( _.name.contains( os.toLowerCase ) )

  jars foreach { jar =>
    println( s"[info] Processing '$jar' and saving to '${unmanagedBase.value}'" )
    IO.unzip(  jar, unmanagedBase.value )
  }

  Seq.empty[File]
})

// META-INF discarding
assemblyMergeStrategy in assembly := {
  case PathList( "META-INF", xs@_* ) => MergeStrategy.discard
  case x => MergeStrategy.first
}

// Code Style

SbtScalariform.scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(SpaceInsideParentheses, true)
    .setPreference(SpacesWithinPatternBinders, true)
    .setPreference(SpacesAroundMultiImports, true)
    .setPreference(DanglingCloseParenthesis, Force)
    .setPreference(CompactControlReadability, true)
)

// Code Quality

scapegoatRunAlways := false

/*
wartremoverErrors ++= Seq(
  //Wart.Any
  //Wart.AsInstanceOf,
  //Wart.IsInstanceOf,
  Wart.Any2StringAdd,
  Wart.EitherProjectionPartial,
  Wart.ListOps,
  Wart.OptionPartial,
  Wart.Product,
  Wart.Serializable
  // For a more purely functional approach
  //Wart.NonUnitStatements,
  //Wart.Null,
  //Wart.Return,
  //Wart.Throw
  //Wart.Var
)

wartremoverWarnings ++= Seq(
  Wart.Enumeration,
  Wart.ExplicitImplicitTypes,
  Wart.FinalCaseClass
)
*/

coverageMinimum := 75

coverageFailOnMinimum := true

cpdSettings