package com.example.bubblelevel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class BubbleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paintTrack = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#e0e0e0")
        strokeWidth = 4f
    }

    private val paintCenterMark = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#4fc3f7")
        strokeWidth = 3f
    }

    private val paintBubble = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#4fc3f7")
    }

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 56f
        textAlign = Paint.Align.CENTER
    }

    private val paintLabel = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#aaaacc")
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }

    var tiltX: Float = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f

        val trackHalfWidth = width / 2f - 60f
        val trackHeight = 64f
        val trackRect = RectF(
            cx - trackHalfWidth,
            cy - trackHeight / 2f,
            cx + trackHalfWidth,
            cy + trackHeight / 2f
        )

        canvas.drawRoundRect(trackRect, 32f, 32f, paintTrack)

        val markHeight = trackHeight * 0.6f
        canvas.drawLine(cx, cy - markHeight / 2f, cx, cy + markHeight / 2f, paintCenterMark)

        val bubbleRadius = trackHeight / 2f - 10f
        val maxShift = trackHalfWidth - bubbleRadius

        val clampedTilt = tiltX.coerceIn(-9.81f, 9.81f)
        val degrees = Math.toDegrees(Math.asin((clampedTilt / 9.81f).toDouble())).toFloat()
        val bubbleX = cx + (-degrees / 90f * maxShift).coerceIn(-maxShift, maxShift)

        val isLevel = Math.abs(degrees) < 2f
        paintBubble.color = if (isLevel) Color.parseColor("#66bb6a") else Color.parseColor("#4fc3f7")

        canvas.drawCircle(bubbleX, cy, bubbleRadius, paintBubble)

        if (isLevel) {
            canvas.drawText("Ровно!", cx, cy + trackHeight / 2f + 70f, paintText)
        } else {
            canvas.drawText(String.format("%.1f°", degrees), cx, cy + trackHeight / 2f + 70f, paintText)
            canvas.drawText("наклон", cx, cy + trackHeight / 2f + 115f, paintLabel)
        }
    }
}