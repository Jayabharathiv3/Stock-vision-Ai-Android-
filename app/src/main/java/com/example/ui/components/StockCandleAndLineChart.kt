package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.model.ChartDataPoint
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NeonCyan

@Composable
fun StockCandleAndLineChart(
  data: List<ChartDataPoint>,
  isCandleMode: Boolean,
  currency: String,
  modifier: Modifier = Modifier,
  onPointHover: (ChartDataPoint?) -> Unit = {}
) {
  if (data.isEmpty()) return

  var selectedIndex by remember { mutableStateOf<Int?>(null) }

  val primaryColor = NeonCyan
  val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

  Box(modifier = modifier) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(data) {
          detectTapGestures(
            onPress = { offset ->
              val stepX = size.width / (data.size - 1).coerceAtLeast(1)
              val idx = (offset.x / stepX).toInt().coerceIn(0, data.size - 1)
              selectedIndex = idx
              onPointHover(data[idx])
              tryAwaitRelease()
              selectedIndex = null
              onPointHover(null)
            }
          )
        }
        .pointerInput(data) {
          detectDragGestures(
            onDragStart = { offset ->
              val stepX = size.width / (data.size - 1).coerceAtLeast(1)
              val idx = (offset.x / stepX).toInt().coerceIn(0, data.size - 1)
              selectedIndex = idx
              onPointHover(data[idx])
            },
            onDragEnd = {
              selectedIndex = null
              onPointHover(null)
            },
            onDragCancel = {
              selectedIndex = null
              onPointHover(null)
            },
            onDrag = { change, _ ->
              val stepX = size.width / (data.size - 1).coerceAtLeast(1)
              val idx = (change.position.x / stepX).toInt().coerceIn(0, data.size - 1)
              selectedIndex = idx
              onPointHover(data[idx])
            }
          )
        }
    ) {
      val width = size.width
      val height = size.height

      // Chart layout: top 72% for price, bottom 22% for volume, 6% gap
      val priceHeight = height * 0.72f
      val volumeTop = height * 0.78f
      val volumeHeight = height * 0.22f

      val allHighs = data.map { it.high }
      val allLows = data.map { it.low }
      val minPrice = allLows.minOrNull() ?: 0.0
      val maxPrice = allHighs.maxOrNull() ?: 1.0
      val priceRange = (maxPrice - minPrice).coerceAtLeast(0.01)

      val maxVolume = data.maxOfOrNull { it.volume } ?: 1L
      val stepX = width / (data.size - 1).coerceAtLeast(1)

      // Draw subtle horizontal grid lines for price
      val gridLines = 4
      for (i in 0..gridLines) {
        val y = priceHeight * (i.toFloat() / gridLines)
        drawLine(
          color = gridColor,
          start = Offset(0f, y),
          end = Offset(width, y),
          strokeWidth = 1.dp.toPx()
        )
      }

      // Draw Volume bars
      data.forEachIndexed { index, point ->
        val x = index * stepX
        val barWidth = (stepX * 0.65f).coerceAtLeast(3f)
        val normalizedVol = (point.volume.toFloat() / maxVolume.toFloat()).coerceIn(0f, 1f)
        val barHeight = normalizedVol * volumeHeight
        val barTop = volumeTop + (volumeHeight - barHeight)
        val volColor = if (point.close >= point.open) BullishGreen.copy(alpha = 0.4f) else BearishRed.copy(alpha = 0.4f)

        drawRect(
          color = volColor,
          topLeft = Offset(x - barWidth / 2f, barTop),
          size = Size(barWidth, barHeight)
        )
      }

      if (isCandleMode) {
        // Draw Candlesticks
        data.forEachIndexed { index, point ->
          val x = index * stepX
          val isBullish = point.close >= point.open
          val candleColor = if (isBullish) BullishGreen else BearishRed

          val highY = priceHeight - (((point.high - minPrice) / priceRange).toFloat() * priceHeight)
          val lowY = priceHeight - (((point.low - minPrice) / priceRange).toFloat() * priceHeight)
          val openY = priceHeight - (((point.open - minPrice) / priceRange).toFloat() * priceHeight)
          val closeY = priceHeight - (((point.close - minPrice) / priceRange).toFloat() * priceHeight)

          // Wick
          drawLine(
            color = candleColor,
            start = Offset(x, highY),
            end = Offset(x, lowY),
            strokeWidth = 2.dp.toPx()
          )

          // Body
          val bodyTop = minOf(openY, closeY)
          val bodyHeight = kotlin.math.abs(openY - closeY).coerceAtLeast(3.dp.toPx())
          val bodyWidth = (stepX * 0.7f).coerceAtLeast(4f).coerceAtMost(22.dp.toPx())

          drawRect(
            color = candleColor,
            topLeft = Offset(x - bodyWidth / 2f, bodyTop),
            size = Size(bodyWidth, bodyHeight)
          )
        }
      } else {
        // Draw Smooth Line Chart
        val points = data.mapIndexed { index, point ->
          val x = index * stepX
          val y = priceHeight - (((point.close - minPrice) / priceRange).toFloat() * priceHeight)
          Pair(x, y)
        }

        val linePath = Path()
        val areaPath = Path()

        linePath.moveTo(points.first().first, points.first().second)
        areaPath.moveTo(points.first().first, priceHeight)
        areaPath.lineTo(points.first().first, points.first().second)

        for (i in 0 until points.size - 1) {
          val p0 = points[i]
          val p1 = points[i + 1]
          val cX = (p0.first + p1.first) / 2f
          linePath.cubicTo(cX, p0.second, cX, p1.second, p1.first, p1.second)
          areaPath.cubicTo(cX, p0.second, cX, p1.second, p1.first, p1.second)
        }

        areaPath.lineTo(points.last().first, priceHeight)
        areaPath.close()

        drawPath(
          path = areaPath,
          brush = Brush.verticalGradient(
            colors = listOf(primaryColor.copy(alpha = 0.35f), Color.Transparent),
            startY = 0f,
            endY = priceHeight
          )
        )

        drawPath(
          path = linePath,
          color = primaryColor,
          style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
      }

      // Draw Scrubber Crosshair if hovered
      selectedIndex?.let { idx ->
        if (idx in data.indices) {
          val point = data[idx]
          val x = idx * stepX
          val y = priceHeight - (((point.close - minPrice) / priceRange).toFloat() * priceHeight)

          // Vertical crosshair
          drawLine(
            color = Color.White.copy(alpha = 0.8f),
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
          )

          // Active pulse dot
          drawCircle(
            color = primaryColor.copy(alpha = 0.3f),
            radius = 10.dp.toPx(),
            center = Offset(x, y)
          )
          drawCircle(
            color = Color.White,
            radius = 5.dp.toPx(),
            center = Offset(x, y)
          )
        }
      }
    }
  }
}
