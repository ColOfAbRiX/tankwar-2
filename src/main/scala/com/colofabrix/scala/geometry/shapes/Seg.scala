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

package com.colofabrix.scala.geometry.shapes

import com.colofabrix.scala.geometry.abstracts.Shape
import com.colofabrix.scala.gfx.abstracts.Renderer
import com.colofabrix.scala.math.Vect

/**
  * An object that represents a line segment with two endpoints
  *
  * @param v0 The first endpoint of the line segment
  * @param v1 The second endpoint of the line segment
  */
case class Seg( v0: Vect, v1: Vect ) extends Shape {
  val endpoints = v0 :: v1 :: Nil

  /** The segment as vector */
  lazy val vect = v1 - v0

  /** The length of the segment */
  lazy val length = vect.ϑ

  /** True if the Segment is parallel to the Y-axis */
  lazy val isY = v0.x == v1.x

  /** True if the Segment is parallel to the X-axis */
  lazy val isX = v0.y == v1.y

  /**
    * The surface area of the Shape
    */
  def area: Double = 0.0

  /**
    * Shifts a shape on the space
    *
    * Provided a vector, every vertex or equivalent of the shape will be moved following that vector
    *
    * @param where The vector specifying where to move the shape
    * @return A new shape moved of a vector {where}
    */
  override def move( where: Vect ): Seg = Seg( v0 + where, v1 + where )

  /**
    * Compute the distance between a point and a line segment
    *
    * This is a problem of geometry and not directly related to the Shape, but it's something that it is used by many
    * other methods.
    *
    * @see http://geomalgorithms.com/a02-_lines.html
    * @param p Point to check
    * @return A tuple containing 1) the distance vector from the point to the boundary and 2) the edge or the point from which the distance is calculated
    */
  @inline
  override def distance( p: Vect ): ( Vect, Vect ) = {
    val v = this.v1 - this.v0
    val w = p - this.v0
    val c1 = v x w
    val c2 = v x v

    if ( c1 <= 0.0 ) {
      return ( this.v0 - p, this.v0 )
    }
    else if ( c2 <= c1 ) {
      return ( this.v1 - p, this.v1 )
    }

    val pb = this.v0 + v * ( c1 / c2 )
    ( pb - p, pb )
  }

  /**
    * Compute the distance between two line segments
    *
    * See http://geomalgorithms.com/a07-_distance.html
    *
    * @param s1 The second segment to check
    * @return A tuple containing 1) the distance vector from the point to the perimeter and 2) the edge or the point from which the distance is calculated
    */
  @inline
  override def distance( s1: Seg ): ( Vect, Vect ) = {
    iPoint( s1 ) match {
      case None ⇒
        // If two segments don't intersects, the distance is always from the end points
        val distances = distance( s1.v0 ) :: distance( s1.v1 ) :: Nil
        distances.minBy( _._1.ρ )

      case _ ⇒ ( Vect.zero, Vect.zero )
    }
  }

  /**
    * An object responsible to renderer the class where this trait is applied
    *
    * @return A renderer that can draw the object where it's applied
    */
  override def renderer: Renderer = ???

  /**
    * Determines if a point is inside or on the boundary the shape
    *
    * @param p The point to be checked
    * @return True if the point is inside the shape or on its boundary
    */
  override def contains( p: Vect ): Boolean = distance( p )._1 == Vect.zero

  /**
    * Determines if a shape is inside or on the boundary the current shape
    *
    * @param s The shape to be checked
    * @return True if the given shape is inside the shape or on its boundary
    */
  override def contains( s: Shape ): Boolean = intersects( s )

  /**
    * Determines if a line segment touches in any way this shape
    *
    * @param s The line segment to check
    * @return True if the line intersects the shape
    */
  override def intersects( s: Seg ): Boolean = iPoint( s ) match {
    case None ⇒ false
    case Some( _ ) ⇒ true
  }

  /**
    * Determines if a shape touches in any way this shape
    *
    * @param that The shape to be checked
    * @return True if the point is inside the shape
    */
  override def intersects( that: Shape ): Boolean = that match {
    case s: Seg ⇒ throw new IllegalArgumentException
    case c: Circle ⇒ ???
    case p: Polygon ⇒
      // For a ConvexPolygon the || will ensure a faster execution
      endpoints.exists( p.contains ) ||
        p.edgesIterator.exists {
          case p0 +: p1 +: Nil ⇒ intersects( Seg( p0, p1 ) )
        }
  }

  /**
    * Determines if two line segments intersects and where they do so
    *
    * Ref: http://geomalgorithms.com/a05-_intersect-1.html
    *
    * @param s The second segment to check
    * @return An option to indicate if the two segments intersects. If they do a tuple containing 1) The intersection point and 2) and Option with a possible ending intersegtion point
    */
  @inline
  def iPoint( s: Seg ): Option[( Vect, Option[Vect] )] = {
    import Math._

    import com.colofabrix.scala.math.VectConversions._

    val v = this.vect
    val u = s.vect
    val w = s.v0 - this.v0
    val D = u ^ v

    // Test if  they are parallel (includes either being a point)
    if ( abs( D ) <= Double.MinPositiveValue ) {
      if ( ( u ^ w ) != 0.0 || ( v ^ w ) != 0.0 ) {
        // S1 and S2 are parallel
        return None
      }

      val du = u x u
      val dv = v x v

      // They are collinear or degenerate check if they are degenerate points

      if ( du == 0.0 && dv == 0.0 ) {
        // Both segments are points
        if ( s.v0.x != this.v0.x ) {
          // They are distinct  points
          return None
        }
        return Some( ( s.v0, None ) )
      }

      if ( du == 0.0 ) {
        // S1 is a single point
        if ( !contains( s.v0 ) ) {
          // But is not in S2
          return None
        }
        return Some( ( s.v0, None ) )
      }

      if ( dv == 0.0 ) {
        // S2 a single point
        if ( !s.contains( this.v0 ) ) {
          // But is not in S1
          return None
        }
        return Some( ( this.v0, None ) )
      }

      // They are collinear segments - get overlap (or not)

      // Endpoints of S1 in eqn for S2
      var t0 = 0.0
      var t1 = 0.0
      val w2 = s.v1 - this.v0

      if ( v.x != 0.0 ) {
        t0 = w.x / v.x
        t1 = w2.x / v.x
      }
      else {
        t0 = w.y / v.y
        t1 = w2.y / v.y
      }

      if ( t0 > t1 ) {
        // Must have t0 smaller than t1, swap if not
        val tmp = t0
        t0 = t1
        t1 = tmp
      }

      if ( t0 > 1.0 || t1 < 0.0 ) {
        // NO overlap
        return None
      }

      t0 = max( 0.0, t0 )
      t1 = min( 1.0, t1 )

      if ( t0 == t1 ) {
        // Intersect is a point
        return Some( ( this.v0 + t0 * v, None ) )
      }

      // They overlap in a valid subsegment
      return Some( ( this.v0 + t0 * v, Some( this.v0 + t1 * v ) ) )
    }

    // The segments are skew and may intersect in a point.

    // Get the intersect parameter for S1
    val sI = ( v ^ w ) / D
    if ( sI < 0.0 || sI > 1.0 ) {
      // No intersect with S1
      return None
    }

    // Get the intersect parameter for S2
    val tI = ( u ^ w ) / D
    if ( tI < 0.0 || tI > 1.0 ) {
      // No intersect with S2
      return None
    }

    return Some( ( s.v0 + sI * u, None ) )
  }

  override def toString = s"Seg(${v0.x}, ${v0.y} -> ${v1.x}, ${v1.y})"
}

object Seg {
  /**
    * Creates a segment using a sequence of [[Vect]].
    *
    * @param vertices A [[Seq]] of exactly 2 Vects
    * @return A new instance of Seg
    */
  def apply( vertices: Seq[Vect] ): Seg = vertices match {
    case v0 +: v1 +: Nil ⇒ Seg( v0, v1 )
    case _ ⇒ throw new IllegalArgumentException( "A segment is specified from only and only 2 vertices" )
  }
}