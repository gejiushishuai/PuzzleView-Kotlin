package com.xiaopo.flying.puzzleview_kotlin

import android.graphics.PointF
import android.graphics.RectF

/**
 * @author wupanjie
 */
open class PuzzleLayout(val init: PuzzleLayout.() -> Unit) {
  lateinit var outerArea: Area
  val outerLines = ArrayList<Line>()
  val areas = ArrayList<Area>()
  val lines = ArrayList<Line>()
  val areaCount
    get() = areas.size

  operator fun get(i: Int) = areas[i]

  val width
    get() = outerArea.width

  val height
    get() = outerArea.height

  open fun layout() {
    init()
  }

  fun setOuterBounds(bounds: RectF) {
    val A = PointF(bounds.left, bounds.top)
    val B = PointF(bounds.right, bounds.top)
    val C = PointF(bounds.right, bounds.bottom)
    val D = PointF(bounds.left, bounds.bottom)

    val lineLeft = A verticalTo D
    val lineTop = A horizontalTo B
    val lineRight = B verticalTo C
    val lineBottom = D horizontalTo C

    outerLines.clear()
    outerLines.add(lineLeft)
    outerLines.add(lineTop)
    outerLines.add(lineRight)
    outerLines.add(lineBottom)

    outerArea = Area.Companion.area {
      leftLine = lineLeft
      topLine = lineTop
      rightLine = lineRight
      bottomLine = lineBottom

      leftTop = A
      rightTop = B
      rightBottom = C
      leftBottom = D
    }

    areas.clear()
    areas.add(outerArea)
  }

  fun reset() {
    lines.clear()
    areas.clear()
    areas.add(outerArea)
  }

  open fun addLine(position: Int, direction: Int, radio: Float) {
    val line = when (direction) {
      Line.HORIZONTAL -> createHorizontalLine(position, radio)
      Line.VERTICAL -> createVerticalLine(position, radio)
      else -> null
    }

    val area = this[position]
    areas -= area

    line?.let {
      lines.add(line)
      areas += area cutBy line

      areas.sortWith(Area.Companion.comparator)
      lines.updateLineLimit()
    }
  }
}

fun PuzzleLayout.createHorizontalLine(position: Int, radio: Float) = Line.Companion.line {
  val area = this@createHorizontalLine[position]
  direction = Line.HORIZONTAL
  start = PointF(area.left, area.height * radio + area.top)
  end = PointF(area.right, area.height * radio + area.top)

  attachStartLine = area.leftLine
  attachEndLine = area.rightLine

  upperLine = area.bottomLine
  lowerLine = area.topLine
}

fun PuzzleLayout.createVerticalLine(position: Int, radio: Float) = Line.Companion.line {
  val area = this@createVerticalLine[position]
  direction = Line.VERTICAL
  start = PointF(area.width * radio + area.left, area.top)
  end = PointF(area.width * radio + area.left, area.bottom)

  attachStartLine = area.topLine
  attachEndLine = area.bottomLine

  upperLine = area.rightLine
  lowerLine = area.leftLine
}

private fun List<Line>.updateLineLimit() {
  for (i in 0..lastIndex) {
    val line = this[i]
    this.filter {
      it == line
          || it.direction != line.direction
          || it.attachStartLine != line.attachStartLine
          || it.attachEndLine != line.attachEndLine
    }.forEach {
      if (it.direction == Line.Companion.HORIZONTAL) {
        if (it.minY > line.lowerLine?.maxY ?: 0f && it.maxY < line.minY) {
          line.lowerLine = it
        }
        if (it.maxY < line.upperLine?.minY ?: 0f && it.minY > line.maxY) {
          line.upperLine = it
        }
      } else {
        if (it.minX > line.lowerLine?.maxX ?: 0f && it.maxX < line.minX) {
          line.lowerLine = it
        }

        if (it.maxX < line.upperLine?.minX ?: 0f && it.minX > line.maxX) {
          line.upperLine = it
        }
      }
    }
  }
}

//private fun List<Line>.updateUpperLine(line: Line) {
//  filter {
//    it == line
//        || it.direction != line.direction
//        || it.attachStartLine != line.attachStartLine
//        || it.attachEndLine != line.attachEndLine
//  }.forEach {
//      if (it.direction == Line.HORIZONTAL) {
//        if (it.minY > line.lowerLine!!.maxY && it.maxY < line.minY) {
//          line.lowerLine = it
//        }
//        if (it.maxY < line.upperLine!!.minY && it.minY > line.maxY) {
//          line.upperLine = it
//        }
//      } else {
//        if (it.minX > line.lowerLine!!.maxX && it.maxX < line.minX) {
//          line.lowerLine = it
//        }
//
//        if (it.maxX < line.upperLine!!.minX && it.minX > line.maxX) {
//          line.upperLine = it
//        }
//      }
//  }
//}