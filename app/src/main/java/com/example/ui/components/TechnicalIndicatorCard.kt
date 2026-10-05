package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Stock
import com.example.ui.theme.*

@Composable
fun TechnicalIndicatorCard(
  stock: Stock,
  modifier: Modifier = Modifier
) {
  val rsi = stock.rsi
  val rsiStatus = when {
    rsi > 70 -> "Overbought"
    rsi < 30 -> "Oversold"
    rsi in 55.0..70.0 -> "Bullish Momentum"
    rsi in 30.0..45.0 -> "Bearish Pressure"
    else -> "Neutral"
  }
  val rsiColor = when {
    rsi > 70 -> BearishRed
    rsi < 30 -> BullishGreen
    rsi in 55.0..70.0 -> BullishGreen
    else -> NeutralAmber
  }

  val sma20 = stock.currentPrice * 0.985
  val ema50 = stock.currentPrice * 0.962
  val bollingerUpper = stock.currentPrice * 1.042
  val bollingerLower = stock.currentPrice * 0.948

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        shape = RoundedCornerShape(20.dp)
      )
      .padding(18.dp)
  ) {
    Column {
      Text(
        text = "Technical Indicators & Signals",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(14.dp))

      // RSI Section with visual bar gauge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "RSI (14 Period)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "%.1f".format(rsi),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(rsiColor.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = rsiStatus,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = rsiColor
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // RSI Visual Gradient Bar
      Canvas(
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp))
      ) {
        val width = size.width
        val height = size.height

        drawRect(
          brush = Brush.horizontalGradient(
            colors = listOf(BullishGreen, NeutralAmber, BearishRed)
          ),
          topLeft = Offset.Zero,
          size = Size(width, height)
        )

        // Marker for current RSI position
        val markerX = (rsi.toFloat() / 100f).coerceIn(0f, 1f) * width
        drawCircle(
          color = Color.White,
          radius = 5.dp.toPx(),
          center = Offset(markerX, height / 2f)
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "0 Oversold", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "50 Neutral", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "100 Overbought", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Spacer(modifier = Modifier.height(16.dp))
      Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 1.dp)
      Spacer(modifier = Modifier.height(14.dp))

      // MACD & Moving Averages Matrix
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(text = "MACD (12,26,9)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "%s%.2f".format(if (stock.macd >= 0) "+" else "", stock.macd),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (stock.macd >= 0) BullishGreen else BearishRed
          )
          Text(
            text = if (stock.macd >= 0) "Signal Line Crossover" else "Bearish Divergence",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = if (stock.macd >= 0) BullishGreen else BearishRed
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(text = "SMA 20 vs EMA 50", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "${stock.currency}%,.1f / ${stock.currency}%,.1f".format(sma20, ema50),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Golden Cross Alignment",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = BullishGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Bollinger Bands Range
      Column {
        Text(text = "Bollinger Bands (20, 2)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Lower: ${stock.currency}%,.1f".format(bollingerLower),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Mid: ${stock.currency}%,.1f".format(sma20),
            style = MaterialTheme.typography.bodySmall,
            color = NeonCyan
          )
          Text(
            text = "Upper: ${stock.currency}%,.1f".format(bollingerUpper),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}
