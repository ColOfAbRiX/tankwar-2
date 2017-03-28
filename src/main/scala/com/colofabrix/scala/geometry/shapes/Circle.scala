/*
 * Copyright (C) 2017 Fabrizio Colonna
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

package com.colofabrix.scala.geometry.shapes

import com.colofabrix.scala.geometry.Shape
import com.colofabrix.scala.math.{ DoubleWithAlmostEquals, Vect }

/**
  * Circle shape
  *
  * A Circle is a very convenient shape to check for geometrical properties as
  * it is, generally speaking, very fast to compute them. For this reason it is also marked as a {Container}
  *
  * @param center Center of the circle
  * @param radius Radius of the circle. Must be non-negative
  */
case class Circle(center: Vect, radius: Double) extends Shape {
  // If the radius is 0... it's a point!
  require(radius ~> 0.0, "The circle must have a non-zero radius")

  override lazy val area: Double = Math.PI * Math.pow(radius, 2.0)

  override def move(where: Vect): Shape = new Circle(center + where, radius)
}

object Circle {
  /**
    * Creates a Circle known its area
    *
    * @param center Center of the circle
    * @param area   Area of the circle. Must be non-negative
    *
    * @return A new instance of Circle with an area equals to the specified one
    */
  def fromArea(center: Vect, area: Double) = {
    require(area > 0, "The area specified for a circle must be positive")
    new Circle(center, Math.sqrt(area / Math.PI))
  }
}