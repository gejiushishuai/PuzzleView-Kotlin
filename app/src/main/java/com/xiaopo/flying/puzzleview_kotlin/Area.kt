package com.xiaopo.flying.puzzleview_kotlin

import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF

/**
 * TODO (support padding)
 * @author wupanjie
 */
class Area(
    var leftLine: Line,
    var topLine: Line,
    var rightLine: Line,
    var bottomLine: Line,
    var leftTop: PointF = PointF(),
    var leftBottom: PointF = PointF(),
    var rightTop: PointF = PointF(),
    var rightBottom: PointF = PointF()
) {
  private val path = Path()
  private val rect = RectF()

  constructor(src: Area) :
      this(src.leftLine,
          src.topLine,
          src.rightLine,
          src.bottomLine,
          src.leftTop,
          src.leftBottom,
          src.rightTop,
          src.rightBottom)


  companion object {
    val comparator = Comparator { lhs: Area, rhs: Area ->
      if (lhs.top < rhs.top) {
        return@Comparator -1
      } else if (lhs.top == rhs.top) {
        if (lhs.left < rhs.left) {
          return@Comparator -1
        } else {
          return@Comparator 1
        }
      } else {
        return@Comparator 1
      }
    }
  }

  fun createPath(): Path {
    path.reset()
    path.moveTo(leftTop.x, leftTop.y)
    path.lineTo(rightTop.x, rightTop.y)
    path.lineTo(rightBottom.x, rightBottom.y)
    path.lineTo(leftBottom.x, leftBottom.y)
    path.lineTo(leftTop.x, leftTop.y)

    return path
  }

  fun getAreaRect(): RectF {
    rect.set(left, top, right, bottom)
    return rect
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

infix fun Area.cutBy(line: Line): List<Area> {
  val spiltAreas = ArrayList<Area>(2)
  when (line.direction) {
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
