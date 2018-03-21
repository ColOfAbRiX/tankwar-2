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

import com.colofabrix.scala.math.VectUtils._
import com.colofabrix.scala.math.{ DoubleWithAlmostEquals, Vect }

/**
  * An infinite line.
  *
  * The line is defined by a normal N and a point Q:
  *   N = (A, B); Q = (x0, y0)
  * then these parameters determine the line equation:
  *   Ax + By + C = 0; C = -(Ax0 + By0)
  * which written in the explicit form is:
  *   y = mx + q; m = -A / B; q = (A / B)x0 + y0
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

  /** Returns true if the line is horizontal */
  val isHorizontal: Boolean = normal.x ==~ 0.0

  /** Returns true if the line is vertical */
  val isVertical: Boolean = normal.y ==~ 0.0

  /** Distance of a point from the line. */
  def distance(v: Vect): Vect = ((v - point) ∙ normal) * normal

  /** Line equation. */
  def equation(x: Double): Vect = (x, m * x + q)

  /** Clip the line into a segment fully contained in a Box. */
  def clip(frame: Box): Option[Segment] = {
    val seg = if (isHorizontal) {
      Segment( (frame.left, point.y), (frame.right, point.y) )
    }
    else if (isVertical) {
      Segment( (point.x, frame.bottom), (point.x, frame.top) )
    }
    else {
      Segment( equation( frame.left ), equation( frame.right ) )
    }
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
  /**
    * Creates a Line given a normal and the distance of the normal from the origin. The line created
    * with this method can only have its norma on the 1st quadrant.
    */
  def apply(normal: Vect, distance: Double): Line = new Line(
    normal.v,
    distance * normal.xyComp(Math.abs(_))
  )

  def apply(normal: Vect, point: Vect): Line = new Line(normal.v, point)
}