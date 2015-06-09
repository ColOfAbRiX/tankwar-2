package com.colofabrix.scala.neuralnetwork.activationfunctions

import com.colofabrix.scala.neuralnetwork.abstracts.ActivationFunction

/**s
  * Linear activation function
  *
  * Maps the input directly to the output
  */
class Linear extends ActivationFunction {

  override val UFID = "df8d5f6e-0523-4f60-90da-a9222ac8647a"

  override def apply(input: Double): Double = input

  override def toString = "Linear"

}