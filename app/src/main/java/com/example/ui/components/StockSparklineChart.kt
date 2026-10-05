package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen

@Composable
fun StockSparklineChart(
  data: List<Double>,
  isPositive: Boolean,
  modifier: Modifier = Modifier,
  strokeWidth: Float = 4f,
  showGradientFill: Boolean = true
) {
  if (data.size < 2) return

  val lineColor = if (isPositive) BullishGreen else BearishRed
  val fillColor = if (isPositive) BullishGreen.copy(alpha = 0.25f) else BearishRed.copy(alpha = 0.25f)

  Canvas(modifier = modifier.fillMaxSize()) {
    val width = size.width
    val height = size.height

    val minVal = data.minOrNull() ?: 0.0
    val maxVal = data.maxOrNull() ?: 1.0
    val range = (maxVal - minVal).coerceAtLeast(0.001)

    val stepX = width / (data.size - 1)
    val points = data.mapIndexed { index, value ->
      val x = index * stepX
      val normalizedY = ((value - minVal) / range).toFloat()
      val y = height - (normalizedY * (height * 0.8f)) - (height * 0.1f)
      Pair(x, y)
    }

    val strokePath = Path()
    val fillPath = Path()

    strokePath.moveTo(points.first().first, points.first().second)
    fillPath.moveTo(points.first().first, height)
    fillPath.lineTo(points.first().first, points.first().second)

    for (i in 0 until points.size - 1) {
      val p0 = points[i]
      val p1 = points[i + 1]
      val controlX = (p0.first + p1.first) / 2f
      strokePath.cubicTo(controlX, p0.second, controlX, p1.second, p1.first, p1.second)
      fillPath.cubicTo(controlX, p0.second, controlX, p1.second, p1.first, p1.second)
    }

    fillPath.lineTo(points.last().first, height)
    fillPath.close()

    if (showGradientFill) {
      drawPath(
        path = fillPath,
        brush = Brush.verticalGradient(
          colors = listOf(fillColor, Color.Transparent),
          startY = 0f,
          endY = height
        )
      )
    }

    drawPath(
      path = strokePath,
      color = lineColor,
      style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
  }
}
