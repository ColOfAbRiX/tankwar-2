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

package com.colofabrix.scala.tankwar

import scala.annotation.tailrec
import com.colofabrix.scala.gfx.Time.SyncState
import com.colofabrix.scala.gfx.{ Drawing, Keyboard, OpenGL, Time }
import com.colofabrix.scala.tankwar.Configuration.{ Simulation => SimConfig }
import com.colofabrix.scala.tankwar.simulation.World
import com.typesafe.scalalogging.LazyLogging
import org.lwjgl.opengl.Display

/**
  * Simulation manager
  */
object Manager extends LazyLogging {

  sealed
  case class SimulationState(
    syncState: SyncState,
    world: Option[World],
    timeStepMultiplier: Double,
    cycleTimeDelta: Double
  )

  /** Start the simulation. */
  def start(): Unit = {
    val initial_state = SimulationState(
      Time.init(),
      Some(World()),
      SimConfig.timeStepMultiplier,
      0.0
    )

    if( Configuration.Simulation.gxfEnabled ) {
      // Run with graphics
      OpenGL.init(800, 600)
      run_gfx(initial_state)
      OpenGL.destroy()
    }
    else {
      // Quick run without graphics
      run(initial_state)
    }
  }

  @tailrec
  private
  def run(state: SimulationState): SimulationState =
    state.world match {
      case Some(w) => run(state.copy(world = w.step(1.0)))
      case _ => state.copy(world = None)
    }

  @tailrec
  private
  def run_gfx(state: SimulationState): SimulationState = {
    logger.info(s"Manager state: $state")
    
    state.world match {
      case Some(w) => if( !Display.isCloseRequested ) {
        val state2 = manageMouse(state)
        val state3 = manageKeyboard(state2)
        val state4 = manageGraphics(state3)
        val state5 = state4.copy(
          world = w.step(state4.timeStepMultiplier * state4.cycleTimeDelta)
        )
        run_gfx(state5)
      }
      else {
        state.copy(world = None)
      }

      case _ => state.copy(world = None)
    }
  }

  private
  def manageKeyboard(state: SimulationState): SimulationState = {
    import org.lwjgl.input.Keyboard._

    Keyboard.events().foldLeft(state) {
      case (s, Keyboard.KeyPressed(k)) =>
        if( k == KEY_ADD ) {
          // Key "Numpad +": Increase simulation speed
          logger.info("KEY_ADD pressed: increase simulation speed.")
          s.copy(
            timeStepMultiplier = Math.min(10.0, s.timeStepMultiplier * 1.2)
          )
        }
        else if( k == KEY_SUBTRACT ) {
          // Key "Numpad -": Decrease simulation speed
          logger.info("KEY_SUBTRACT pressed: decrease simulation speed.")
          s.copy(
            timeStepMultiplier = Math.max(0.1, s.timeStepMultiplier * 0.8)
          )
        }
        else s

      case (s, _) => s
    }
  }

  private
  def manageMouse(state: SimulationState): SimulationState = state match {
    case _ => state
  }

  private
  def manageGraphics(state: SimulationState): SimulationState = state match {
    case SimulationState(_, Some(w), _, _) =>
      OpenGL.clear()
      for( t <- w.tanks ) {
        Drawing.drawCircle(t.shape.center, t.shape.radius)
      }
      OpenGL.update()
      val (ns, td) = Time.sync(SimConfig.fps).run(state.syncState)

      state.copy(syncState = ns, cycleTimeDelta = td)

    case _ => state
  }

}