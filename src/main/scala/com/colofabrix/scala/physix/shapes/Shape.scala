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

package com.colofabrix.scala.physix.shapes

import com.colofabrix.scala.math.Vect
import com.colofabrix.scala.physix.Collision
import com.colofabrix.scala.utils.EasyEquatable

/**
  * Represents a geometric closed shape on a geometric space
  */
trait Shape extends EasyEquatable[Shape] {

  /** The surface area of the Shape */
  def area: Double

  /**
    * Returns collision information if two Shapes collide.
    *
    * The left disjunction contains the collision information if
    * there is a collision, the right disjunction if there is no
    * collision.
    */
  def collision(that: Shape): Collision = Collision.check(this, that)

  /** Moves a shape of the given vector. */
  def move(v: Vect): Shape

  /** Scales the size of the shape. */
  def scale(k: Double): Shape

}
