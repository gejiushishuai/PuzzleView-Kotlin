package com.xiaopo.flying.puzzleview_kotlin

import android.graphics.PointF

/**
 * TODO (support padding)
 * @author wupanjie
 */
class Area private constructor(
    var leftLine: Line,
    var topLine: Line,
    var rightLine: Line,
    var bottomLine: Line,
    var leftTop: PointF,
    var leftBottom: PointF,
    var rightTop: PointF,
    var rightBottom: PointF
) {
  constructor(src: Area) :
      this(src.leftLine,
          src.topLine,
          src.rightLine,
          src.bottomLine,
          src.leftTop,
          src.leftBottom,
          src.rightTop,
          src.rightBottom)

  constructor(builder: Area.Builder) :
      this(builder.leftLine,
          builder.topLine,
          builder.rightLine,
          builder.bottomLine,
          builder.leftTop,
          builder.leftBottom,
          builder.rightTop,
          builder.rightBottom)

  companion object {
    fun area(init: Area.Builder.() -> Unit) = Area.Builder(
        init).build()

    val comparator = Comparator { lhs: Area, rhs: Area ->
      if (lhs.top < rhs.top) {
        return@Comparator -1
      } else if (lhs.top == rhs.top) {
        if (lhs.left < rhs.left) {
          return@Comparator -1;
        } else {
          return@Comparator 1;
        }
      } else {
        return@Comparator 1;
      }
    }
  }

  class Builder private constructor() {
    constructor(init: Area.Builder.() -> Unit) : this() {
      init()
    }

    lateinit var leftLine: Line
    lateinit var topLine: Line
    lateinit var rightLine: Line
    lateinit var bottomLine: Line
    lateinit var leftTop: PointF
    lateinit var leftBottom: PointF
    lateinit var rightTop: PointF
    lateinit var rightBottom: PointF

    fun leftLine(init: Area.Builder.() -> Line) = apply { leftLine = init() }
    fun topLine(init: Area.Builder.() -> Line) = apply { topLine = init() }
    fun rightLine(init: Area.Builder.() -> Line) = apply { rightLine = init() }
    fun bottomLine(init: Area.Builder.() -> Line) = apply { bottomLine = init() }

    fun leftTop(init: Area.Builder.() -> PointF) = apply { leftTop = init() }
    fun leftBottom(init: Area.Builder.() -> PointF) = apply { leftBottom = init() }
    fun rightTop(init: Area.Builder.() -> PointF) = apply { rightTop = init() }
    fun rightBottom(init: Area.Builder.() -> PointF) = apply { rightBottom = init() }

    fun build() = Area(this)
  }
}

val Area.left
  get() = Math.min(leftTop.x, leftBottom.x)

val Area.top
  get() = Math.min(leftTop.y, rightTop.y)

val Area.right
  get() = Math.max(rightTop.x, rightBottom.x)

val Area.bottom
  get() = Math.max(leftBottom.y, rightBottom.y)

val Area.centerX
  get() = (left + right) / 2

val Area.centerY
  get() = (top + bottom) / 2

val Area.width
  get() = right - left

val Area.height
  get() = bottom - top

val Area.centerPoint
  get() = PointF(centerX, centerY)

infix fun Area.contains(line: Line)
    = line == leftLine || line == topLine || line == rightLine || line == bottomLine

infix fun Area.cutBy(line: Line) : List<Area> {
  val spiltAreas = ArrayList<Area>(2)
  when(line.direction){
    Line.HORIZONTAL -> {
      val one = Area(this)
      one.bottomLine = line
      one.leftBottom = line.start
      one.rightBottom = line.end
      spiltAreas.add(one)

      val two = Area(this)
      two.topLine = line
      two.leftTop = line.start
      two.rightTop = line.end
      spiltAreas.add(two)
    }

    Line.VERTICAL -> {
      val one = Area(this)
      one.rightLine = line
      one.rightTop = line.start
      one.rightBottom = line.end
      spiltAreas.add(one)

      val two = Area(this)
      two.leftLine = line
      two.leftTop = line.start
      two.leftBottom = line.end
      spiltAreas.add(two)
    }
  }

  return spiltAreas
}
