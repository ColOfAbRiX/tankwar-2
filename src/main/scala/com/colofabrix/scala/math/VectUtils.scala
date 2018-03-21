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

package com.colofabrix.scala.math

import java.util.Random

/**
  * Utilities for Vect
  */
object VectUtils {

  /**
    * Enrichment for numeric types against Vect
    */
  implicit class Support[T: Numeric](number: T) {
    private val num = implicitly[Numeric[T]].toDouble(number)

    /** Allows "num * vect" and "vect * num" forms */
    def *(v: Vect): Vect = v * num
  }

  /**
    * Enrichment for Vect to make some Physical calculations easier
    */
  implicit class RichVect[T <: Vect](vector: T) {
    /** Multiplies each component of a vector with the corresponding component of the other vector */
    def **(v: Vect): Vect = XYVect(vector.x * v.x, vector.y * v.y)

    /** Cartesian coordinates as a list */
    def xy = Seq(vector.x, vector.y)

    /** Polar coordinates as a list */
    def rt = Seq(vector.ρ, vector.ϑ)

    /** Mapping of one cartesian component at the time */
    def xyComp(f: Double => Double): Vect = XYVect(f(vector.x), f(vector.y))

    /** Mapping of one cartesian component at the time */
    def xyComp(f: (Double, Int) => Double): Vect = XYVect(f(vector.x, 0), f(vector.y, 1))

    /** Mapping of one cartesian component at the time */
    def rtComp(f: Double => Double): Vect = RTVect(f(vector.ρ), f(vector.ϑ))

    /** Mapping of one cartesian component at the time */
    def rtComp(f: (Double, Int) => Double): Vect = RTVect(f(vector.ρ, 0), f(vector.ϑ, 1))

    /** Randomized the XY components up to their value. */
    def xyRand(scale: Double = 1.0): Vect = vector xyComp { _ * new Random().nextDouble() * scale }

    /** Randomized the RT components up to their value. */
    def rtRand(scale: Double = 1.0): Vect = vector rtComp { _ * new Random().nextDouble() * scale }
  }

  /**
    * Converts a tuple into an XYVect
    */
  implicit def tuple2xyvect(vect: Tuple2[Double, Double]): Vect = XYVect(vect._1, vect._2)

}
