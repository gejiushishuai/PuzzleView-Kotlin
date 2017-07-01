package com.xiaopo.flying.puzzleview_kotlin

import android.graphics.PointF

/**
 * @author wupanjie
 */
class Line private constructor(
    val direction: Int,
    val start: PointF,
    val end: PointF,
    var upperLine: Line?,
    var lowerLine: Line?,
    var attachStartLine: Line?,
    var attachEndLine: Line?) {

  private constructor(builder: Line.Builder) :
      this(builder.direction,
          builder.start,
          builder.end,
          builder.upperLine,
          builder.lowerLine,
          builder.attachStartLine,
          builder.attachEndLine)

  companion object {
    // 线的方向
    val HORIZONTAL = 0
    val VERTICAL = 1

    fun line(init: Builder.() -> Unit) = Line.Builder(
        init).build()
  }

  class Builder private constructor() {
    constructor(init: Builder.() -> Unit) : this() {
      init()
    }

    var direction: Int = Line.HORIZONTAL
    lateinit var start: PointF
    lateinit var end: PointF
    var upperLine: Line? = null
    var lowerLine: Line? = null
    var attachStartLine: Line? = null
    var attachEndLine: Line? = null

    fun direction(init: Builder.() -> Int) = apply { direction = init() }
    fun start(init: Builder.() -> PointF) = apply { start = init() }
    fun end(init: Builder.() -> PointF) = apply { end = init() }
    fun upperLine(init: Builder.() -> Line) = apply { upperLine = init() }
    fun lowerLine(init: Builder.() -> Line) = apply { lowerLine = init() }
    fun attachStartLine(init: Builder.() -> Line) = apply { attachStartLine = init() }
    fun attachEndLine(init: Builder.() -> Line) = apply { attachEndLine = init() }

    fun build() = Line(this);
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
