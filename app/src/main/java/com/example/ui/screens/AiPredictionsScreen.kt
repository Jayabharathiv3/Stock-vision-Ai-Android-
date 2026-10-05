package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PredictionHorizon
import com.example.ui.components.AiPredictionCard
import com.example.ui.theme.*
import com.example.viewmodel.StockVisionViewModel

@Composable
fun AiPredictionsScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  val stocks by viewModel.stocks.collectAsState()
  val selectedSymbol by viewModel.selectedStockSymbol.collectAsState()
  val prediction by viewModel.currentPrediction.collectAsState()
  val selectedHorizon by viewModel.predictionHorizon.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Screen Title
    item {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Psychology, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "AI Market Intelligence",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
        Text(
          text = "Neural probability distribution models and deep indicator synthesis",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Stock Selection Carousel
    item {
      Column {
        Text(
          text = "Select Stock to Forecast:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          stocks.forEach { stock ->
            val isSelected = stock.symbol == selectedSymbol
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(
                  if (isSelected) NeonCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
                .border(
                  width = 1.dp,
                  color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                  shape = RoundedCornerShape(14.dp)
                )
                .clickable { viewModel.openPredictionsFor(stock.symbol) }
                .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = stock.symbol,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.onSurface,
                  fontSize = 13.sp
                )
                Text(
                  text = stock.formattedPrice,
                  style = MaterialTheme.typography.labelSmall,
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // Main AI Prediction Card
    item {
      if (prediction != null) {
        AiPredictionCard(
          prediction = prediction!!,
          selectedHorizon = selectedHorizon,
          onHorizonSelect = { horizon -> viewModel.setPredictionHorizon(horizon) }
        )
      } else {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
          CircularProgressIndicator(color = NeonCyan)
        }
      }
    }

    // AI Synthesized Market Insights Section
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Insights, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "StockVision AI Deep Synthesis",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "StockVision AI analyzed the latest market indicators, historical price action, order book liquidity, and multitimeframe moving averages for $selectedSymbol:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          InsightFactorRow(
            title = "Positive Momentum & Accumulation",
            desc = "Institutional buy walls detected on lower timeframes with rising volume.",
            isPositive = true
          )
          InsightFactorRow(
            title = "RSI Strength & Dynamic Banding",
            desc = "Relative strength indicator comfortably above midline with positive convergence.",
            isPositive = true
          )
          InsightFactorRow(
            title = "Moving Average Alignment (EMA 50 / SMA 20)",
            desc = "Key exponential moving average support acting as a spring for upward momentum.",
            isPositive = true
          )
          InsightFactorRow(
            title = "Macro & Sector Sentiment Corroboration",
            desc = "Broad sector metrics show resilience against benchmark macroeconomic volatility.",
            isPositive = true
          )
        }
      }
    }

    // Multi-Horizon Projection Summary Table
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Multi-Horizon Forecast Matrix",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(12.dp))

          HorizonRow("Next Day (1D)", "+1.85%", "89% Confidence", BullishGreen)
          Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
          HorizonRow("7 Days (1W)", "+4.68%", "87% Confidence", BullishGreen)
          Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
          HorizonRow("30 Days (1M)", "+11.20%", "81% Confidence", BullishGreen)
        }
      }
    }
  }
}

@Composable
private fun InsightFactorRow(title: String, desc: String, isPositive: Boolean) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.Top
  ) {
    Icon(
      imageVector = if (isPositive) Icons.Default.CheckCircle else Icons.Default.Warning,
      contentDescription = null,
      tint = if (isPositive) BullishGreen else NeutralAmber,
      modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
      Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun HorizonRow(period: String, move: String, conf: String, color: Color) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = period, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(text = move, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
      Spacer(modifier = Modifier.width(10.dp))
      Text(text = conf, style = MaterialTheme.typography.labelSmall, color = NeonCyan)
    }
  }
}
