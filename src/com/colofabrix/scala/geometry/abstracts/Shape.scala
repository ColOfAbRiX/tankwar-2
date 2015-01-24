package com.colofabrix.scala.geometry.abstracts

import com.colofabrix.scala.geometry.Vector2D

/**
 * Represents a graphical shape
 *
 * TODO: Unit test this class
 */
trait Shape {
  /**
   * Determines if a point is inside or on the boundary the shape
   *
   * @param p The point to be checked
   * @return True if the point is inside the shape
   */
  def overlaps( p: Vector2D ): Boolean

  /**
   * Determines if a line touches in any way this shape
   *
   * @param p0 The first point that defines the line
   * @param p1 The second point that defines the line
   * @return True if the point is inside the shape
   */
  def overlaps( p0: Vector2D, p1: Vector2D ): Boolean

  /**
   * Determines if a shape touches in any way this shape
   *
   * @param that The shape to be checked
   * @return True if the point is inside the shape
   */
  def overlaps( that: Shape ): Boolean

  /**
   * Compute the distance between a point and the edges of the polygon
   *
   * @param p Point to check
   * @return A distance vector from the point to polygon and the edge or point from which the distance is calculated
   */
  def distance(p: Vector2D): (Vector2D, Vector2D)

  /**
   * Compute the distance between a line and the edges of the polygon
   *
   * @param p0 The first point that defines the line
   * @param p1 The second point that defines the line
   * @return A distance vector from the point to polygon and the edge or point from which the distance is calculated
   */
  def distance(p0: Vector2D, p1: Vector2D): (Vector2D, Vector2D)

  /**
   * Compute the distance between a point and a line segment
   *
   * Implementation of the algorithm: http://geomalgorithms.com/a02-_lines.html
   *
   * @param v0 First end of the segment
   * @param v1 Second end of the segment
   * @param p Point to check
   * @return A distance vector from the point to the segment or one of its ends
   */
  protected def distance(v0: Vector2D, v1: Vector2D, p: Vector2D): Vector2D = {
    val v = v1 - v0
    val w = p - v0
    val c1 = v x w
    val c2 = v x v

    if( c1 <= 0.0 )
      return v0 - p
    else if( c2 <= c1 )
      return v1 - p

    val pb = v0 + v * (c1 / c2)
    pb - p
  }

  /**
   * Moves a shape
   *
   * @param where The vector specifying how to move the shape
   * @return A new shape moved of `where`
   */
  def move(where: Vector2D): Shape

  /**
   * Find a containing box for the current shape.
   *
   * @return A new shape that contains all the current shape
   */
  val container: Shape
}
