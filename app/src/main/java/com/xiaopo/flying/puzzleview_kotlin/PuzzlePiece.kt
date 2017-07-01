package com.xiaopo.flying.puzzleview_kotlin

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.animation.DecelerateInterpolator
import kotlin.reflect.KProperty

/**
 * @author wupanjie
 */
class PuzzlePiece(drawable: Drawable, val area: Area, val matrix: Matrix) {
  private val previousMatrix = Matrix()
  private val tempMatrix = Matrix()

  var drawable = drawable
    set(value) {
      field = value
      drawableBounds.set(0, 0, value.intrinsicWidth, value.intrinsicHeight)
      drawablePoints = floatArrayOf(0f, 0f, width, 0f, width, height, 0f, height)
    }

  val drawableBounds = Rect(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
  var drawablePoints = FloatArray(8)
  val mappedDrawableBounds = RectF()
    get() = field.mappedWith(matrix, RectF(drawableBounds))
  val mappedDrawablePoints = FloatArray(8)
    get() = field.mappedWith(matrix, drawablePoints)
//  val mappedDrawablePoints by StateDelegate()

  private var previousMoveX = 0f
  private var previousMoveY = 0f
  private val centerPoint = PointF()
  private val mappedCenterPoint = PointF()

  private val animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)

  val width
    get() = drawable.intrinsicWidth.toFloat()

  val height
    get() = drawable.intrinsicHeight.toFloat()

  val scale
    get() = MatrixUtils.getMatrixScale(matrix)

  val angle
    get() = MatrixUtils.getMatrixAngle(matrix)


  init {
    animator.interpolator = DecelerateInterpolator()
    animator.duration = 300
  }

  fun draw(canvas: Canvas, alpha: Int = 0, needClip: Boolean = false) {
    canvas.save()

    if (needClip) {
      canvas.clipPath(area.createPath())
    }

    canvas.concat(matrix)
    drawable.bounds = drawableBounds
    drawable.alpha = alpha
    drawable.draw(canvas)

    canvas.restore()
  }

  fun contains(line: Line) = area.contains(line)
  fun contains(x: Float, y: Float) = area.contains(x, y)

}

