/*
 * Copyright (C) 2016 Fabrizio
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

package com.colofabrix.scala

/**
  * Mathematical constants and functions
  */
package object math {

  /** The absolute value where a smaller, floating point number is considered zero */
  val FP_PRECISION = 1E-9

  /** Implicit approximate comparison between two Doubles - necessary for numerical computations */
  implicit class DoubleWithAlmostEquals( val d: Double ) extends AnyVal {
    def ~==( d2: Double ) = ( d - d2 ).abs < FP_PRECISION
  }

}
