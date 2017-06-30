package com.xiaopo.flying.library

import android.graphics.PointF

/**
 * @author wupanjie
 */
val Line.length: Float
    get() = Math.sqrt(Math.pow((end.x - start.x).toDouble(), 2.0) + Math.pow((end.y - start.y).toDouble(), 2.0))
            .toFloat()

val Line.isHorizontal: Boolean
    get() = direction == Line.HORIZONTAL

val Line.isVertical: Boolean
    get() = direction == Line.VERTICAL

val Line.slope: Float
    get() = when (direction) {
        Line.HORIZONTAL -> 0f
        Line.VERTICAL -> Float.POSITIVE_INFINITY
        else -> (start.y - end.y) / (start.x - end.x);
    }

val Line.verticalIntercept: Float
    get() = when (direction) {
        Line.HORIZONTAL -> start.y
        Line.VERTICAL -> Float.POSITIVE_INFINITY
        else -> start.y - slope * start.x
    }

infix fun Line.parallelWith(line: Line) = slope == line.slope

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