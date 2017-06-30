package com.xiaopo.flying.library

import android.graphics.PointF

/**
 * @author wupanjie
 */
interface Line {
    companion object {
        // 线的方向
        val HORIZONTAL = 0
        val VERTICAL = 1
        fun create(init: Line.() -> Line) = apply {
        }
    }

    val direction: Int
        get
    var start: PointF
    var end: PointF
    var upperLine: Line
    var lowerLine: Line
    var attachStartLine: Line
    var attachEndLine: Line

}
