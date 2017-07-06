package com.xiaopo.flying.puzzleview_kotlin

import android.graphics.PointF

/**
 * @author wupanjie
 */
class Line(
    val direction: Int,
    val start: PointF,
    val end: PointF,
    var upperLine: Line? = null,
    var lowerLine: Line? = null,
    var attachStartLine: Line? = null,
    var attachEndLine: Line? = null) {

  companion object {
    // 线的方向
    val HORIZONTAL = 0
    val VERTICAL = 1
  }
}

val Line.minX
  get() = Math.min(start.x, end.x)

val Line.maxX
  get() = Math.max(start.x, end.x)

val Line.minY
  get() = Math.min(start.y, end.y)

val Line.maxY
  get() = Math.max(start.y, end.y)

val Line.length: Float
  get() = Math.sqrt(
      Math.pow((end.x - start.x).toDouble(), 2.0) + Math.pow((end.y - start.y).toDouble(),
          2.0)).toFloat()

val Line.isHorizontal: Boolean
  get() = direction == Line.Companion.HORIZONTAL

val Line.isVertical: Boolean
  get() = direction == Line.Companion.VERTICAL

val Line.slope: Float
  get() = when (direction) {
    Line.Companion.HORIZONTAL -> 0f
    Line.Companion.VERTICAL -> Float.POSITIVE_INFINITY
    else -> (start.y - end.y) / (start.x - end.x);
  }

val Line.verticalIntercept: Float
  get() = when (direction) {
    Line.Companion.HORIZONTAL -> start.y
    Line.Companion.VERTICAL -> Float.POSITIVE_INFINITY
    else -> start.y - slope * start.x
  }

infix fun Line.parallelWith(line: Line) = slope == line.slope
