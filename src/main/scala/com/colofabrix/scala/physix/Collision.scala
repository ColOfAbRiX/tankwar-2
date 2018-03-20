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

package com.colofabrix.scala.physix

import com.colofabrix.scala.math.VectUtils._
import com.colofabrix.scala.math._
import com.colofabrix.scala.physix.shapes._

/** Information about a collision between objects  */
trait Collision {
  def flip(): Collision
}

/**
  * When two shapes penetrate each other
  * @param normal Collision normal
  * @param distance Penetration distance
  */
final case class Overlap(
  normal: Vect,
  distance: Double,
  thisShape: Shape,
  thatShape: Shape
) extends Collision {
  override def flip(): Collision = this.copy(
    normal = normal * -1.0,
    thisShape = this.thatShape,
    thatShape = this.thisShape
  )

  override def toString: String = s"Overlap(normal=$normal, penetration=$distance, this=$thisShape, that=$thatShape)"
}

/**
  * When two shapes do not penetrate each other
  * @param distance Distance as vector
  */
final case class Separate(
  distance: Vect,
  thisShape: Shape,
  thatShape: Shape
) extends Collision {
  override def flip(): Collision = this.copy(
    distance = distance * -1.0,
    thisShape = this.thatShape,
    thatShape = this.thisShape
  )

  override def toString: String = s"Separate(distance=$distance, this=$thisShape, that=$thatShape)"
}

/**
  * Implementations of collision detection between shapes.
  */
object Collision {

  /** Detects if two shapes collide */
  def check(thisShape: Shape, thatShape: Shape): Collision = thisShape match {
    case thisCircle: Circle => thatShape match {
      case thatCircle: Circle => checkCircleCircle(thisCircle, thatCircle)
      case thatBox: Box => checkCircleBox(thisCircle, thatBox)
      case thatLine: Line => check(thisCircle, thatLine).flip()
    }
    case thisBox: Box => thatShape match {
      case thatCircle: Circle => check(thatCircle, thisBox).flip()
      case thatBox: Box => checkBoxBox(thisBox, thatBox)
      case thatLine: Line => checkBoxLine(thisBox, thatLine)
    }
    case thisLine: Line => thatShape match {
      case thatCircle: Circle => checkLineCircle(thisLine, thatCircle)
      case thatBox: Box => check(thatBox, thisLine).flip()
      case thatBox: Line => checkLineLine(thisLine, thatBox)
    }
  }

  /**
    * Distance from a Circle to a Box
    */
  private def checkCircleBox(thisCircle: Circle, thatBox: Box): Collision = {
    val c2c = thatBox.center - thisCircle.center
    val b2c = XYVect(
      Math.max(c2c.x, thatBox.width / 2.0),
      Math.max(c2c.y, thatBox.height / 2.0)
    )
    val d = b2c.ρ - thisCircle.radius

    if (d <~ 0.0) {
      Overlap( b2c.n, d, thisCircle, thatBox )
    }
    else {
      Separate( d * b2c.n, thisCircle, thatBox )
    }
  }

  /**
    * Distance from a Circle to a Circle
    */
  private def checkCircleCircle(thisCircle: Circle, thatCircle: Circle): Collision = {
    val c2c = thisCircle.center - thatCircle.center
    val d = c2c.ρ - (thisCircle.radius + thatCircle.radius)

    if (d <~ 0.0) {
      Overlap( c2c.v, d, thisCircle, thatCircle )
    }
    else {
      Separate( d * c2c.v, thisCircle, thatCircle )
    }
  }

  /**
    * Distance from a Line to a Circle
    */
  private def checkLineCircle(thisLine: Line, thatCircle: Circle): Collision = {
    val d2c = thisLine.distance(thatCircle.center)

    if (d2c.ρ <~ thatCircle.radius) {
      Overlap( d2c.v, d2c.ρ, thisLine, thatCircle )
    }
    else {
      Separate( d2c - thatCircle.radius * d2c.n, thisLine, thatCircle )
    }
  }

  private def checkBoxBox(box1: Box, box2: Box): Collision = ???

  private def checkBoxLine(box: Box, line: Line): Collision = ???

  private def checkLineLine(line1: Line, line2: Line): Collision = ???
}
