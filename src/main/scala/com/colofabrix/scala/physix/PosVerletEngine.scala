/*
 * Copyright (C) 2017 Fabrizio
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

import com.colofabrix.scala.geometry.Shape
import com.colofabrix.scala.math.Vect
import com.colofabrix.scala.math.VectUtils._
import com.colofabrix.scala.tankwar.Configuration.{ Simulation => SimConfig }
import com.typesafe.scalalogging.LazyLogging

/**
  * Störmer–Verlet integration, position version
  *
  * External references:
  *  - https://en.wikipedia.org/wiki/Verlet_integration
  *  - https://www.gamedev.net/resources/_/technical/math-and-physics/a-verlet-based-approach-for-2d-game-physics-r2714
  *  - http://stackoverflow.com/tags/verlet-integration/info
  *  - http://lonesock.net/article/verlet.html
  *  - http://gafferongames.com/game-physics/integration-basics/
  *
  * @param body         Reference to the object
  * @param lastPosition The position of the previous step, used by the Verlet integrator
  */
class PosVerletEngine private(
  val body: RigidBody, private val lastPosition: Vect
) extends Physix with LazyLogging {

  override def integrate(extForces: Vect = Vect.zero): PosVerletEngine = {
    // Total forces acting on the object (internal + external)
    val forces = body.internalForce + extForces
    // Acceleration
    val acc = forces.map(_ / body.mass)
    // Position
    val pos = 2.0 * body.position - lastPosition + acc * Math.pow(SimConfig.timeStep, 2.0)
    // Velocity, calculated as half step behind the current
    val vel = (pos - body.position) / SimConfig.timeStep

    return new PosVerletEngine(body.update(pos, vel, 0.0, 0.0), body.position)
  }

  override def collision(colliding: Seq[RigidBody]): PosVerletEngine = {
    return this
  }

  override def borders(constraints: Seq[Shape]): Physix = ???
}

object PosVerletEngine {
  def apply(physicalObject: RigidBody, extForces: Vect = Vect.zero): PosVerletEngine = {
    // Position Verlet needs the last 2 positions to calculate the next one
    val forces = physicalObject.internalForce + extForces
    val acc = forces.map(_ / physicalObject.mass)

    // Position at the step before initialization
    val lastPos = physicalObject.position -
      physicalObject.velocity * SimConfig.timeStep -
      0.5 * acc * Math.pow(SimConfig.timeStep, 2.0)

    return new PosVerletEngine(physicalObject, lastPos)
  }
}
