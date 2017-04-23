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

package com.colofabrix.scala.drawing

import com.colofabrix.scala.geometry.Shape
import com.colofabrix.scala.geometry.shapes.{ Box, Circle, Line }
import com.colofabrix.scala.gfx.Drawing
import com.colofabrix.scala.math.{ DoubleWithAlmostEquals, XYVect }
import com.colofabrix.scala.tankwar.Configuration.World.{ Arena => ArenaConfig }

/**
  * Any object that can be drawn
  */
object ShapeRenderer {
  def draw(s: Shape): Unit = s match {
    case c: Circle => Drawing.drawCircle(c.center, c.radius)

    case b: Box => Drawing.drawPolygon(b.vertices)

    case l: Line =>
      if( (l.normal.ϑ ~== 0.0) || (l.normal.ϑ ~== Math.PI) ) {
        val x0 = (l.drawingNormal * l.distance).x

        val start = XYVect(x0, 0.0)
        val end = XYVect(x0, ArenaConfig.height)

        Drawing.drawSegment(start, end)
      }
      else {
        Drawing.drawSegment(
          l.drawingEquation(0.0),
          l.drawingEquation(ArenaConfig.width)
        )
      }
  }
}