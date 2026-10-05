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
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CandlestickChart
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChartDataPoint
import com.example.model.Stock
import com.example.ui.components.StockCandleAndLineChart
import com.example.ui.components.TechnicalIndicatorCard
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StockVisionViewModel

@Composable
fun StockDetailsScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  val stock by viewModel.selectedStock.collectAsState()
  val chartPeriod by viewModel.chartPeriod.collectAsState()
  val isCandleMode by viewModel.isCandleMode.collectAsState()
  val chartData by viewModel.chartData.collectAsState()
  val prediction by viewModel.currentPrediction.collectAsState()

  var hoveredPoint by remember { mutableStateOf<ChartDataPoint?>(null) }
  var showAddDialog by remember { mutableStateOf(false) }

  if (stock == null) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      CircularProgressIndicator(color = NeonCyan)
    }
    return
  }

  val currentStock = stock!!
  val isPositive = currentStock.isPositive
  val statusColor = if (isPositive) BullishGreen else BearishRed

  val periods = listOf("1D", "1W", "1M", "3M", "6M", "1Y", "5Y")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Header & Actions
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.popBack() }) {
              Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = currentStock.symbol,
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                  Text(
                    text = currentStock.exchange,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = currentStock.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Row {
            // Watchlist toggle
            IconButton(onClick = { viewModel.toggleWatchlist(currentStock.symbol) }) {
              Icon(
                imageVector = if (currentStock.isWatchlist) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = "Watchlist",
                tint = if (currentStock.isWatchlist) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            // Quick Add to Portfolio
            IconButton(onClick = { showAddDialog = true }) {
              Icon(
                imageVector = Icons.Default.AddBusiness,
                contentDescription = "Add to Portfolio",
                tint = NeonCyan
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Price Display (reacts to hovered scrubber point or shows current stock price)
        val displayPrice = hoveredPoint?.close ?: currentStock.currentPrice
        val displayLabel = hoveredPoint?.label ?: "Current Market Price"

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Column {
            Text(
              text = "${currentStock.currency}%,.2f".format(displayPrice),
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = displayLabel,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(statusColor.copy(alpha = 0.15f))
              .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
              contentDescription = null,
              tint = statusColor,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "%s${currentStock.currency}%,.2f (%s%.2f%%)".format(
                if (isPositive) "+" else "",
                currentStock.change,
                if (isPositive) "+" else "",
                currentStock.changePercent
              ),
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = statusColor
            )
          }
        }
      }
    }

    // Chart Controls: Periods & Line/Candle Switch
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Periods
        Row(
          modifier = Modifier
            .weight(1f)
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          periods.forEach { period ->
            val isSelected = period == chartPeriod
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                  if (isSelected) NeonCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
                .clickable { viewModel.setChartPeriod(period) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = period,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        // Toggle Candlestick vs Line
        IconButton(
          onClick = { viewModel.toggleCandleMode() },
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .size(36.dp)
        ) {
          Icon(
            imageVector = if (isCandleMode) Icons.Outlined.CandlestickChart else Icons.Outlined.ShowChart,
            contentDescription = "Toggle Chart Style",
            tint = NeonCyan,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    // Interactive Chart Canvas
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
          .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
          .padding(14.dp)
      ) {
        StockCandleAndLineChart(
          data = chartData,
          isCandleMode = isCandleMode,
          currency = currentStock.currency,
          modifier = Modifier.fillMaxSize(),
          onPointHover = { hoveredPoint = it }
        )
      }
    }

    // AI Prediction Snapshot Card
    item {
      prediction?.let { pred ->
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
              androidx.compose.ui.graphics.Brush.horizontalGradient(
                listOf(RoyalPurple.copy(alpha = 0.6f), DarkSurfaceCard.copy(alpha = 0.95f))
              )
            )
            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .clickable { viewModel.openPredictionsFor(currentStock.symbol) }
            .padding(16.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "StockVision AI Intelligence",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (pred.signal.name == "BULLISH") BullishGreen.copy(alpha = 0.2f) else BearishRed.copy(alpha = 0.2f)
              ) {
                Text(
                  text = pred.signal.name,
                  color = if (pred.signal.name == "BULLISH") BullishGreen else BearishRed,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(text = "Predicted Target", style = MaterialTheme.typography.labelSmall, color = DarkTextSecondary)
                Text(
                  text = "${currentStock.currency}%,.2f (%s%.2f%%)".format(
                    pred.predictedPrice,
                    if (pred.expectedChangePercent >= 0) "+" else "",
                    pred.expectedChangePercent
                  ),
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(text = "Model Confidence", style = MaterialTheme.typography.labelSmall, color = DarkTextSecondary)
                Text(
                  text = "${pred.confidence}%",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = NeonCyan
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = { viewModel.openPredictionsFor(currentStock.symbol) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
              Text(text = "Open In-Depth AI Analysis →", color = Color(0xFF00363F), fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Technical Indicators Card (RSI, MACD, Moving Averages, Bollinger Bands)
    item {
      TechnicalIndicatorCard(stock = currentStock)
    }

    // Key Fundamentals Table
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Company Fundamentals & Statistics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(14.dp))

          MetricRow("Market Capitalization", currentStock.marketCap, "P/E Ratio", "%.1f".format(currentStock.peRatio))
          Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
          MetricRow("Earnings Per Share (EPS)", "${currentStock.currency}%.2f".format(currentStock.eps), "Dividend Yield", "%.2f%%".format(currentStock.dividendYield))
          Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
          MetricRow("Day Range", "${currentStock.currency}%,.1f - %,.1f".format(currentStock.low, currentStock.high), "52-Week Range", "${currentStock.currency}%,.1f - %,.1f".format(currentStock.low52w, currentStock.high52w))
          Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
          MetricRow("Trading Volume", "%,d".format(currentStock.volume), "Sector", currentStock.sector)
        }
      }
    }

    // Company Description
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "About ${currentStock.name}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = currentStock.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )
        }
      }
    }
  }

  // Add to Portfolio Dialog
  if (showAddDialog) {
    var quantityText by remember { mutableStateOf("10") }
    var priceText by remember { mutableStateOf(currentStock.currentPrice.toString()) }

    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("Add ${currentStock.symbol} to Portfolio") },
      text = {
        Column {
          OutlinedTextField(
            value = quantityText,
            onValueChange = { quantityText = it },
            label = { Text("Quantity") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text("Purchase Price (${currentStock.currency})") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val q = quantityText.toIntOrNull() ?: 1
            val p = priceText.toDoubleOrNull() ?: currentStock.currentPrice
            viewModel.addPortfolioHolding(currentStock.symbol, q, p)
            showAddDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
        ) {
          Text("Save Holding", color = Color(0xFF00363F), fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun MetricRow(label1: String, val1: String, label2: String, val2: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(text = label1, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(text = val1, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
    Column(modifier = Modifier.weight(1f)) {
      Text(text = label2, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(text = val2, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
  }
}
