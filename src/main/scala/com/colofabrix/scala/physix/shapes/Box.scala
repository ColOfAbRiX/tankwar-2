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

import com.colofabrix.scala.math.Vect
import com.colofabrix.scala.math.VectUtils._

/**
  * Axis Aligned Bounding Box
  */
class Box protected (
    val bottomLeft: Vect,
    val topRight: Vect
) extends Shape {

  /** The vertices of the Box */
  val vertices = Seq[Vect](
    bottomLeft,
    (bottomLeft.x, topRight.y),
    topRight,
    (topRight.x, bottomLeft.y)
  )

  /** Edges of the Box, built from the vertices. Edges are {Vect} from one vertex to its adjacent one */
  val edges: Seq[(Vect, Vect)] = Seq(
    (vertices(0), vertices(1)),
    (vertices(1), vertices(2)),
    (vertices(2), vertices(3)),
    (vertices(3), vertices(0))
  )

  /** Height of the rectangle */
  val height: Double = topRight.y - bottomLeft.y

  /** Width of the rectangle */
  val width: Double = topRight.x - bottomLeft.x

  require(width > 0.0, "Box width must be positive.")
  require(width > 0.0, "Box height must be positive.")

  override val area: Double = width * height

  /** Center of the Box */
  val center: Vect = bottomLeft + (width / 2.0, height / 2.0)

  /** The vertex that is closest to the origin of the axes. */
  lazy val origin: Vect = vertices.minBy(_.ρ)

  /** The vertex that is farthest to the origin of the axes. */
  lazy val opposite: Vect = vertices.maxBy(_.ρ)

  /** Rectangle top-left-most point, in any quadrant of the plane */
  val topLeft: Vect = (bottomLeft.x, topRight.y)

  /** Rectangle bottom-right-most point, in any quadrant of the plane */
  val bottomRight: Vect = (topRight.x, bottomLeft.y)

  /** Rectangle top-most Y */
  val top: Double = topRight.y

  /** Rectangle bottom-most Y */
  val bottom: Double = bottomLeft.y

  /** Rectangle left-most X */
  val left: Double = bottomLeft.x

  /** Rectangle right-most X */
  val right: Double = topRight.x

  override def move(where: Vect): Box = Box(bottomLeft + where, topRight + where)

  override def scale(k: Double): Box = Box(this.center, this.width * k, this.height * k)

  override def toString = s"Box($bottomLeft -> $topRight)"

  override def idFields: Seq[Any] = vertices

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Box]
}

object Box {

  /** Constructor that uses width, height and centers the Box at a specific point */
  def apply(center: Vect, width: Double, height: Double): Box = {
    Box(
      (center.x - width / 2.0, center.y - height / 2.0),
      (center.x + width / 2.0, center.y + height / 2.0)
    )
  }

  /** Constructor that uses width, height and starts the box at the origin of the axis. */
  def apply(width: Double, height: Double): Box = Box(Vect.zero, (width, height))

  /** Creates a new Box using any two opposite vertices */
  def apply(p0: Vect, p1: Vect): Box = {
    val topX = Math.max(p0.x, p1.x)
    val topY = Math.max(p0.y, p1.y)

    val bottomX = Math.min(p0.x, p1.x)
    val bottomY = Math.min(p0.y, p1.y)

    return new Box((bottomX, bottomY), (topX, topY))
  }
}
