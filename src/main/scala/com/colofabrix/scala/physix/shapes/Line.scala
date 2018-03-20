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

package com.colofabrix.scala.physix.shapes

import com.colofabrix.scala.math.{ DoubleWithAlmostEquals, Vect, XYVect }

/**
  * An infinite line.
  */
class Line private (
    val normal: Vect,
    val point: Vect
) extends Shape {

  require(normal != Vect.zero, "A line must be defined with a non-zero normal.")

  /** Parameter "m" of the line equation y = mx + q */
  val m: Double = -(normal.x / normal.y)

  /** Parameter "q" of the line equation y = mx + q */
  val q: Double = -m * point.x + point.y

  /** Distance of a point from the line. */
  def distance(v: Vect): Vect = normal * ((v - point) ∙ normal)

  /** Line equation. */
  def equation(x: Double): Vect = XYVect(x, m * x + q)

  /** Clip the line into a segment fully contained in a Box. */
  def clip(frame: Box): Option[Segment] = {
    val seg = if (normal.y ==~ 0.0)
      // When the line is horizontal
      Segment(
        XYVect(frame.left, point.y),
        XYVect(frame.right, point.y)
      )
    else if (normal.x ==~ 0.0)
      // When the line is vertical
      Segment(
        XYVect(point.x, frame.bottom),
        XYVect(point.x, frame.top)
      )
    else
      // Other cases
      Segment(
        equation(frame.left),
        equation(frame.right)
      )

    seg.clip(frame)
  }

  override val area: Double = 0.0

  override def move(where: Vect): Line = Line(normal, point + where)

  override def scale(k: Double): Shape = this

  override def toString = s"Line((${normal.x}, ${normal.y}) / (${point.x}, ${point.y}))"

  override def idFields: Seq[Any] = Seq(normal, point)

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Line]
}

object Line {
  def apply(normal: Vect, distance: Double): Line = new Line(normal.v, normal * -distance)

  def apply(normal: Vect, point: Vect): Line = new Line(normal.v, point)
}