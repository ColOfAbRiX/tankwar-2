package com.colofabrix.scala.tankwar.tests

import com.colofabrix.scala.tankwar.geometry._
import org.scalatest.{Matchers, WordSpec}

/**
 * Created by Fabrizio on 06/01/2015.
 */
class UTVector2D extends WordSpec with Matchers {
  import com.colofabrix.scala.tankwar.geometry.Vector2DImplicits._

  val tolerance = 1E-5

  "Apply method" must {

    "Return the correct sequence of coordinates" in {
      val test = Vector2D.fromXY(10, -10)

      test(0) should equal (10.0)
      test(1) should equal (-10.0)
      test(2) should equal (14.14213 +- tolerance)
      test(3) should equal (-Math.PI / 4 +- tolerance)
    }

  }

  "Algebra operators" when {

    "Equality" must {

      val cartesian1 = Vector2D.fromXY(10.000000000000002, 10)
      val cartesian2 = Vector2D.fromXY(10.000000000000002, 10)
      val polar1 = Vector2D.fromRT(14.142135623730951, Math.PI / 4)
      val polar2 = Vector2D.fromRT(14.142135623730951, Math.PI / 4)

      assert( cartesian1 == cartesian2 )
      assert( polar1 == polar2 )
      assert( cartesian1 == polar1 )

    }

    "Are addition/subtraction" must {

        val vec1 = Vector2D.fromXY(10, 20)
        val vec2 = Vector2D.fromXY(30, 40)

        val addition = vec1 + vec2
        val subration = vec1 - vec2

        addition.x should equal (10 + 30)
        addition.y should equal (20 + 40)

        subration.x should equal (10 - 30)
        subration.y should equal (20 - 40)

    }

    "Is scalar product" must {

      val vector = Vector2D.fromXY(15, 25)
      val scalar: Double = 12.345

      val result1 = vector * scalar
      val result2 = scalar * vector   // With implicit
      val result3 = vector / scalar

      result1.x should equal (15 * 12.345 +- tolerance)
      result1.y should equal (25 * 12.345 +- tolerance)

      result2.x should equal (15 * 12.345 +- tolerance)
      result2.y should equal (25 * 12.345 +- tolerance)

      result3.x should equal (15 / 12.345 +- tolerance)
      result3.y should equal (25 / 12.345 +- tolerance)

    }

    "Is inner product" must {

      val vector1 = Vector2D.fromXY(5, 10)
      val vector2 = Vector2D.fromXY(15, 20)

      val result = vector1 x vector2

      result should equal ( 5 * 15 + 10 * 20 )

    }

    "Is vector product" must {

      val vector1 = Vector2D.fromXY(15, 25)
      val vector2 = Vector2D.fromXY(30, 35)

      val result1 = vector1 ^ vector2

      result1.x should equal (15 * 30 + 15 * 35)
      result1.y should equal (25 * 30 + 25 * 35)

    }

  }

  "Geometrical operators" when {

    "Map" must {

      val vector = Vector2D.fromXY(12, 34)
      val map = Vector2D.fromXY(56, 78)
      val projection = vector := map

      projection.x should equal (12 * 56)
      projection.y should equal (34 * 78)

    }

    "Projection" must {

      val vector = Vector2D.fromXY(100, 100)
      val axis = Vector2D.fromXY(10, 0)
      val projection = vector -> axis

      projection.x should equal (100.0 +- tolerance)
      projection.y should equal (0.0)

    }

    "Rotation" must {

      val vector = Vector2D.fromXY(10, 10)
      val rotation1 = vector ¬ Math.PI
      val rotation2 = vector ¬ (6 * Math.PI)

      rotation1.x should equal (-10.0 +- tolerance)
      rotation1.y should equal (-10.0 +- tolerance)

      rotation2.x should equal (10.0 +- tolerance)
      rotation2.y should equal (10.0 +- tolerance)

    }

    "Perpendicular" must {

      val vector = Vector2D.fromXY(10, 10)

      val ccw_perpendicular = vector -|
      val cw_perpendicular = vector |-

      ccw_perpendicular.x should equal (-10.0 +- tolerance)
      ccw_perpendicular.y should equal (10.0 +- tolerance)

      cw_perpendicular.x should equal (10.0 +- tolerance)
      cw_perpendicular.y should equal (-10.0 +- tolerance)

    }

    "Normal" must {

      val vector = Vector2D.fromXY(25, 25)
      val result = vector n

      result.r should equal (1.0)
      result.t should equal (Math.PI / 4 + Math.PI / 2)

    }

    "Unit vector" must {

      val vector = Vector2D.fromXY(78, 78)
      val result = vector v

      result.r should equal (1.0)
      result.t should equal (Math.PI / 4)

    }

  }

}