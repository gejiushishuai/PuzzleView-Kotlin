package com.xiaopo.flying.puzzleview_kotlin

import android.graphics.PointF
import android.provider.MediaStore.Audio.Radio
import android.R.attr.x
import android.R.attr.y
import android.graphics.Matrix
import android.graphics.RectF


/**
 * @author wupanjie
 */

val A: PointF = PointF()
val B: PointF = PointF()
val C: PointF = PointF()
val D: PointF = PointF()
val AB: PointF = PointF()
val AM: PointF = PointF()
val BC: PointF = PointF()
val BM: PointF = PointF()
val CD: PointF = PointF()
val CM: PointF = PointF()
val DA: PointF = PointF()
val DM: PointF = PointF()

val matrixValues = FloatArray(9)
val tempMatrix = Matrix()

fun Line.contains(x: Float, y: Float, extra: Float): Boolean {
  if (direction == Line.Companion.VERTICAL) {
    A.x = start.x - extra;
    A.y = start.y;
    B.x = start.x + extra;
    B.y = start.y;
    C.x = end.x + extra;
    C.y = end.y;
    D.x = end.x - extra;
    D.y = end.y;
  } else {
    A.x = start.x;
    A.y = start.y - extra;
    B.x = end.x;
    B.y = end.y - extra;
    C.x = end.x;
    C.y = end.y + extra;
    D.x = start.x;
    D.y = start.y + extra;
  }

  AB.x = B.x - A.x;
  AB.y = B.y - A.y;

  AM.x = x - A.x;
  AM.y = y - A.y;

  BC.x = C.x - B.x;
  BC.y = C.y - B.y;

  BM.x = x - B.x;
  BM.y = y - B.y;

  CD.x = D.x - C.x;
  CD.y = D.y - C.y;

  CM.x = x - C.x;
  CM.y = y - C.y;

  DA.x = A.x - D.x;
  DA.y = A.y - D.y;

  DM.x = x - D.x;
  DM.y = y - D.y;

  return AB x AM > 0 &&
      BC x BM > 0 &&
      CD x CM > 0 &&
      DA x DM > 0
}

fun Area.contains(x: Float, y: Float): Boolean {
  AB.x = rightTop.x - leftTop.x;
  AB.y = rightTop.y - leftTop.y;

  AM.x = x - leftTop.x;
  AM.y = y - leftTop.y;

  BC.x = rightBottom.x - rightTop.x;
  BC.y = rightBottom.y - rightTop.y;

  BM.x = x - rightTop.x;
  BM.y = y - rightTop.y;

  CD.x = leftBottom.x - rightBottom.x;
  CD.y = leftBottom.y - rightBottom.y;

  CM.x = x - rightBottom.x;
  CM.y = y - rightBottom.y;

  DA.x = leftTop.x - leftBottom.x;
  DA.y = leftTop.y - leftBottom.y;

  DM.x = x - leftBottom.x;
  DM.y = y - leftBottom.y;

  return AB x AM > 0 &&
      BC x BM > 0 &&
      CD x CM > 0 &&
      DA x DM > 0
}

infix fun PointF.intersectsWith(pair: Pair<Line, Line>) = apply {
  intersectsWith(pair.first, pair.second)
}

fun PointF.intersectsWith(lineOne: Line, lineTwo: Line) {
  if (lineOne parallelWith lineTwo) {
    set(0f, 0f)
    return
  }

  if (lineOne.isHorizontal and lineTwo.isVertical) {
    set(lineTwo.start.x, lineOne.start.y)
    return
  }

  if (lineOne.isVertical and lineTwo.isHorizontal) {
    set(lineOne.start.x, lineTwo.start.y)
    return
  }

  if (lineOne.isHorizontal and !lineTwo.isVertical) {
    y = lineOne.start.y
    x = (y - lineTwo.verticalIntercept) / lineTwo.slope
    return
  }

  if (lineOne.isVertical and !lineTwo.isHorizontal) {
    x = lineOne.start.x
    y = lineTwo.slope * x + lineTwo.verticalIntercept
    return
  }

  if (lineTwo.isHorizontal and !lineOne.isVertical) {
    y = lineTwo.start.y
    x = (y - lineOne.verticalIntercept) / lineOne.slope
    return
  }

  if (lineTwo.isVertical and !lineOne.isHorizontal) {
    x = lineTwo.start.x
    y = lineOne.slope * x + lineOne.verticalIntercept
    return
  }

  x = (lineTwo.verticalIntercept - lineOne.verticalIntercept) / (lineTwo.slope - lineOne.slope)
  y = x * lineOne.slope + lineOne.verticalIntercept
}

infix fun PointF.x(point: PointF) = x * point.y - y * point.x

infix fun PointF.verticalTo(that: PointF)
    = Line(direction = Line.VERTICAL, start = this, end = that)

infix fun PointF.horizontalTo(that: PointF)
    = Line(direction = Line.HORIZONTAL, start = this, end = that)

fun PointF.between(start: PointF, end: PointF, direction: Int, radio: Float) = apply {
  val deltaY = Math.abs(start.y - end.y)
  val deltaX = Math.abs(start.x - end.x)
  val maxY = Math.max(start.y, end.y)
  val minY = Math.min(start.y, end.y)
  val maxX = Math.max(start.x, end.x)
  val minX = Math.min(start.x, end.x)

  when (direction) {
    Line.HORIZONTAL -> {
      x = minX + deltaX * radio;
      if (start.y < end.y) {
        y = minY + radio * deltaY;
      } else {
        y = maxY - radio * deltaY;
      }
    }

    Line.VERTICAL -> {
      y = minY + deltaY * radio;
      if (start.x < end.x) {
        x = minX + radio * deltaX;
      } else {
        x = maxX - radio * deltaX;
      }
    }
  }
}

fun FloatArray.mappedWith(matrix: Matrix, drawablePoints: FloatArray) = apply {
  matrix.mapPoints(this, drawablePoints)
}

fun RectF.mappedWith(matrix: Matrix, drawableBounds: RectF) = apply {
  matrix.mapRect(this, drawableBounds)
}
