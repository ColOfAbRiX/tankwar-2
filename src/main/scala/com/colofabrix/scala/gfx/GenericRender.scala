/*
 * Copyright (C) 2018 Fabrizio
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

package com.colofabrix.scala.gfx

import scala.annotation.tailrec
import com.colofabrix.scala.math.{ Vect, VectorField, XYVect }
import com.colofabrix.scala.physix.shapes._
import com.colofabrix.scala.tankwar.Configuration.World.{ Arena => ArenaConfig }

/**
  * Gateway to draw a variety of things, from simple to complex.
  */
object GenericRender {
  private val arena = ArenaConfig.asBox

  /** Draw a vector. */
  def draw(v: Vect, tail: Vect): Unit = Drawing.drawVector(v, tail)

  /** Draw the shapes of com.colofabrix.scala.physix.shapes */
  @tailrec
  def draw(s: Shape): Unit = s match {
    case c: Circle => Drawing.drawCircle(c.center, c.radius)

    case b: Box => Drawing.drawPolygon(b.vertices)

    case Segment(p0, p1) => Drawing.drawSegment(p0, p1)

    case l: Line =>
      l.clip(arena) match {
        case Some(s: Segment) => draw(s)
        case None =>
      }
  }

  /** Draw a force field using vectors. */
  def draw(ff: VectorField): Unit = {
    val density = 4 // Number of vectors every 100 pixels

    for {
      i <- 0.0.to(arena.width + 0.01, 100 / density)
      j <- 0.0.to(arena.height + 0.01, 100 / density)
    } {
      val probePoint = XYVect(i, j)
      OpenGL.apply(colour = Some(Colour.DARK_GREY)) {
        GenericRender.draw(ff(probePoint), probePoint)
      }
    }
  }

}