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

package com.colofabrix.scala.physix.worlds

import com.colofabrix.scala.math.VectUtils._
import com.colofabrix.scala.math._
import com.colofabrix.scala.physix.RigidBody
import com.colofabrix.scala.physix.shapes.{ Line, Shape }
import com.colofabrix.scala.tankwar.Configuration.World.Arena
import com.typesafe.scalalogging.LazyLogging

/**
  * World as a slice in the vertical plane of the real world, with gravity and static friction.
  */
final case class WorldSink(bodies: Seq[RigidBody], timeDelta: Double) extends World with LazyLogging {

  logger.trace(s"Initializing WorldVortex World.")
  logger.trace(s"List of bodies: $bodies")
  logger.trace(s"Time delta: $bodies")

  // Create a hill at the center of the arena
  override def forceField: VectorField = (p) => XYVect(
    (Arena.width / 2.0 - p.x) / Arena.width * 10.0,
    (Arena.height / 2.0 - p.y) / Arena.height * 10.0
  )

  // Air friction
  override def friction(body: RigidBody): ScalarField = _ => Math.pow(body.velocity.ρ, 2.0) * 5.0E-4

  // Rectangular arena on the 1st quadrant
  override def walls: Seq[Shape] = Seq(
    // Ceiling
    Line((0.0, -1.0), Arena.height),
    // Left side
    Line((1.0, 0.0), 0.0),
    // Floor
    Line((0.0, 1.0), 0.0),
    // Right side
    Line((-1.0, 0.0), Arena.width)
  )

  override def copy(bodies: Seq[RigidBody] = bodies, timeDelta: Double = timeDelta ): WorldSink = WorldSink(bodies, timeDelta)
}