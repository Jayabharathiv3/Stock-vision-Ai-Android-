package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketSignal
import com.example.model.PredictionHorizon
import com.example.model.RiskLevel
import com.example.model.StockPrediction
import com.example.ui.theme.*

@Composable
fun AiPredictionCard(
  prediction: StockPrediction,
  selectedHorizon: PredictionHorizon,
  onHorizonSelect: (PredictionHorizon) -> Unit,
  modifier: Modifier = Modifier
) {
  val signalColor = when (prediction.signal) {
    MarketSignal.BULLISH -> BullishGreen
    MarketSignal.BEARISH -> BearishRed
    MarketSignal.NEUTRAL -> NeutralAmber
  }

  val signalIcon = when (prediction.signal) {
    MarketSignal.BULLISH -> Icons.Default.TrendingUp
    MarketSignal.BEARISH -> Icons.Default.TrendingDown
    MarketSignal.NEUTRAL -> Icons.Default.AutoAwesome
  }

  val riskColor = when (prediction.riskLevel) {
    RiskLevel.LOW -> BullishGreen
    RiskLevel.MODERATE -> NeutralAmber
    RiskLevel.HIGH -> BearishRed
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(
        Brush.linearGradient(
          colors = listOf(
            DarkSurfaceCard.copy(alpha = 0.95f),
            DarkSurfaceElevated.copy(alpha = 0.95f)
          )
        )
      )
      .border(
        width = 1.5.dp,
        brush = Brush.linearGradient(
          colors = listOf(
            RoyalPurple.copy(alpha = 0.7f),
            NeonCyan.copy(alpha = 0.7f),
            RoyalPurple.copy(alpha = 0.3f)
          )
        ),
        shape = RoundedCornerShape(24.dp)
      )
      .padding(20.dp)
  ) {
    Column {
      // Header with Horizon Selector
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(RoyalPurple.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "StockVision AI Model v4.2",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Multi-factor Neural Forecast",
              style = MaterialTheme.typography.bodySmall,
              color = NeonCyan,
              fontSize = 11.sp
            )
          }
        }

        // Signal Pill
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = signalColor.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, signalColor.copy(alpha = 0.5f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = signalIcon,
              contentDescription = null,
              tint = signalColor,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = prediction.signal.name,
              color = signalColor,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Prediction Horizon Tabs
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color.Black.copy(alpha = 0.35f))
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        PredictionHorizon.values().forEach { horizon ->
          val isSelected = horizon == selectedHorizon
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(
                if (isSelected) RoyalPurple.copy(alpha = 0.6f) else Color.Transparent
              )
              .clickable { onHorizonSelect(horizon) }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = horizon.label,
              style = MaterialTheme.typography.labelMedium,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) Color.White else DarkTextSecondary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Price Target & Confidence Ring Section
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1.3f)) {
          Text(
            text = "Current: %,.2f".format(prediction.currentPrice),
            style = MaterialTheme.typography.bodyMedium,
            color = DarkTextSecondary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Predicted: %,.2f".format(prediction.predictedPrice),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Expected Move: ",
              style = MaterialTheme.typography.bodySmall,
              color = DarkTextSecondary
            )
            Text(
              text = "%s%.2f%%".format(if (prediction.expectedChangePercent >= 0) "+" else "", prediction.expectedChangePercent),
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = signalColor
            )
          }
        }

        // Circular Confidence Ring
        Box(
          modifier = Modifier.size(88.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val radius = diameter / 2f
            val center = this.center

            // Background circle
            drawCircle(
              color = Color.White.copy(alpha = 0.1f),
              radius = radius,
              style = Stroke(width = strokeWidth)
            )

            // Progress sweep arc
            val sweepAngle = (prediction.confidence / 100f) * 360f
            drawArc(
              brush = Brush.sweepGradient(
                listOf(NeonCyan, RoyalPurple, NeonCyan)
              ),
              startAngle = -90f,
              sweepAngle = sweepAngle,
              useCenter = false,
              style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "${prediction.confidence}%",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            Text(
              text = "CONFIDENCE",
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = NeonCyan,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
      Spacer(modifier = Modifier.height(16.dp))

      // Analysis Grid: Trend, Recommendation, Risk
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(text = "RECOMMENDATION", style = MaterialTheme.typography.labelSmall, color = DarkTextMuted, fontSize = 10.sp)
          Text(
            text = prediction.recommendation,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = signalColor
          )
        }
        Column(modifier = Modifier.weight(1f)) {
          Text(text = "RISK PROFILE", style = MaterialTheme.typography.labelSmall, color = DarkTextMuted, fontSize = 10.sp)
          Text(
            text = "${prediction.riskLevel.name} RISK",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = riskColor
          )
        }
        Column(modifier = Modifier.weight(1f)) {
          Text(text = "MARKET TREND", style = MaterialTheme.typography.labelSmall, color = DarkTextMuted, fontSize = 10.sp)
          Text(
            text = prediction.priceTrend,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 1
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Key Drivers Chips
      Text(
        text = "Key AI Indicators & Catalyst Factors:",
        style = MaterialTheme.typography.labelMedium,
        color = NeonCyan,
        fontWeight = FontWeight.SemiBold
      )
      Spacer(modifier = Modifier.height(8.dp))
      prediction.keyDrivers.take(3).forEach { driver ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(NeonCyan)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = driver,
            style = MaterialTheme.typography.bodySmall,
            color = DarkTextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Legal disclaimer
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.Black.copy(alpha = 0.25f))
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = DarkTextMuted,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = prediction.disclaimer,
          style = MaterialTheme.typography.labelSmall,
          color = DarkTextMuted,
          fontSize = 10.sp,
          lineHeight = 13.sp
        )
      }
    }
  }
}
