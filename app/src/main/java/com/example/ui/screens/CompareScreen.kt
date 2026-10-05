package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StockSparklineChart
import com.example.ui.theme.*
import com.example.viewmodel.StockVisionViewModel

@Composable
fun CompareScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  val stocks by viewModel.stocks.collectAsState()
  val compareSymbols by viewModel.compareSymbols.collectAsState()

  val selectedStocks = remember(stocks, compareSymbols) {
    stocks.filter { compareSymbols.contains(it.symbol) }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    item {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CompareArrows, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Comparative Asset Analysis",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
        Text(
          text = "Select up to 4 assets to benchmark valuations, volatility, and AI grades",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Stock Selection Toggle Chips
    item {
      Column {
        Text(
          text = "Equities in Comparison (${selectedStocks.size}/4):",
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
            val isSelected = compareSymbols.contains(stock.symbol)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                  if (isSelected) NeonCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
                .border(
                  1.dp,
                  if (isSelected) NeonCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                  RoundedCornerShape(12.dp)
                )
                .clickable { viewModel.toggleCompareSymbol(stock.symbol) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                  text = stock.symbol,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.onSurface,
                  fontSize = 12.sp
                )
              }
            }
          }
        }
      }
    }

    // Mini Performance Comparison Cards
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        selectedStocks.forEach { stock ->
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            modifier = Modifier.width(150.dp)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(text = stock.symbol, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
              Text(text = stock.formattedPrice, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.ExtraBold)
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "%s%.2f%%".format(if (stock.isPositive) "+" else "", stock.changePercent),
                style = MaterialTheme.typography.labelSmall,
                color = if (stock.isPositive) BullishGreen else BearishRed,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(8.dp))
              Box(modifier = Modifier.fillMaxWidth().height(26.dp)) {
                StockSparklineChart(data = stock.sparkline, isPositive = stock.isPositive, strokeWidth = 2.5f, showGradientFill = true)
              }
            }
          }
        }
      }
    }

    // Side-by-Side Comparison Table (Horizontal Scrollable for clean presentation on all devices)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Text(
            text = "Metric Matrix Breakdown",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(14.dp))

          Box(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            Column(modifier = Modifier.width(maxOf(340.dp, (selectedStocks.size * 110 + 130).dp))) {
              // Table Header
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                  .padding(vertical = 8.dp)
              ) {
                Text(text = "Metric", modifier = Modifier.width(130.dp), fontWeight = FontWeight.Bold, color = DarkTextSecondary, fontSize = 12.sp)
                selectedStocks.forEach { stock ->
                  Text(text = stock.symbol, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 12.sp)
                }
              }

              CompareTableRow("Current Price", selectedStocks.map { it.formattedPrice })
              CompareTableRow("Daily Change", selectedStocks.map { "%s%.2f%%".format(if (it.isPositive) "+" else "", it.changePercent) })
              CompareTableRow("Market Cap", selectedStocks.map { it.marketCap })
              CompareTableRow("P/E Ratio", selectedStocks.map { "%.1f".format(it.peRatio) })
              CompareTableRow("EPS", selectedStocks.map { "${it.currency}%.1f".format(it.eps) })
              CompareTableRow("Div. Yield", selectedStocks.map { "%.2f%%".format(it.dividendYield) })
              CompareTableRow("52W High", selectedStocks.map { "${it.currency}%,.0f".format(it.high52w) })
              CompareTableRow("52W Low", selectedStocks.map { "${it.currency}%,.0f".format(it.low52w) })
              CompareTableRow("RSI (14)", selectedStocks.map { "%.1f".format(it.rsi) })
              CompareTableRow("AI Signal", selectedStocks.map { if (it.rsi > 50) "BULLISH" else "NEUTRAL" })
              CompareTableRow("AI Confidence", selectedStocks.map { "${82 + (it.symbol.hashCode() % 9)}%" })
              CompareTableRow("Risk Profile", selectedStocks.map { if (it.peRatio > 40) "HIGH" else "MODERATE" })
            }
          }
        }
      }
    }
  }
}

@Composable
private fun CompareTableRow(metric: String, values: List<String>) {
  Column {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = metric, modifier = Modifier.width(130.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      values.forEach { valStr ->
        Text(
          text = valStr,
          modifier = Modifier.weight(1f),
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp)
  }
}
