/*
 * Copyright (C) 2015 Freddie Poser
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

package com.colofabrix.scala.gfx.renderers

import com.colofabrix.scala.geometry.shapes.Circle
import com.colofabrix.scala.gfx.Color3D
import com.colofabrix.scala.gfx.abstracts.Renderer
import com.colofabrix.scala.math.Vector2D
import org.lwjgl.opengl.GL11

/**
 * Renders a Circle to the screen
 *
 * @param circle The circle to draw
 * @param color The colour of the circle
 * @param filled True indicated the circle has to be filled
 */
class CircleRenderer( val circle: Circle, color: Color3D = null, val filled: Boolean = false ) extends Renderer {

  /**
   * Draw this to the screen
   */
  def render( ): Unit = {
    val numSegments: Int = Math.max( (circle.radius * 2.0 * Math.PI / 10).toInt, 10 )

    GL11.glPushMatrix( )
    bindColor( color )

    if( filled ) {
      GL11.glTranslated( circle.center.x, circle.center.y, 0 )
      GL11.glBegin( GL11.GL_TRIANGLE_FAN )
    }
    else {
      GL11.glTranslated( 0, 0, 0 )
      GL11.glBegin( GL11.GL_LINE_LOOP )
    }

    for( i <- 0 until numSegments ) {
      val tetha = 2.0 * Math.PI * i.toDouble / numSegments.toDouble
      val point = Vector2D.new_rt( circle.radius, tetha )
      GL11.glVertex2f( point.x.toFloat, point.y.toFloat )
    }

    GL11.glEnd( )
    GL11.glPopMatrix( )
  }

}