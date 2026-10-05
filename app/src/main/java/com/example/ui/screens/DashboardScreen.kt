package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.model.Stock
import com.example.ui.components.MarketOverviewCard
import com.example.ui.components.StockItemCard
import com.example.ui.components.StockSearchBar
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StockVisionViewModel

@Composable
fun DashboardScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  val indices by viewModel.indices.collectAsState()
  val stocks by viewModel.stocks.collectAsState()
  val marketBreadth by viewModel.marketBreadth.collectAsState()
  val userSession by viewModel.userSession.collectAsState()
  val isDemoMode by viewModel.isDemoMode.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val searchFilter by viewModel.searchFilter.collectAsState()

  val filteredStocks = remember(stocks, searchQuery, searchFilter) {
    stocks.filter { stock ->
      val matchesQuery = stock.symbol.contains(searchQuery, ignoreCase = true) ||
          stock.name.contains(searchQuery, ignoreCase = true) ||
          stock.sector.contains(searchQuery, ignoreCase = true)

      val matchesFilter = when (searchFilter) {
        "All" -> true
        "NSE / NIFTY" -> stock.exchange == "NSE"
        "US Tech" -> stock.exchange == "NASDAQ"
        "Financials" -> stock.sector.contains("Financial", ignoreCase = true)
        "IT Services" -> stock.sector.contains("Technology", ignoreCase = true) || stock.sector.contains("IT", ignoreCase = true)
        "Energy" -> stock.sector.contains("Energy", ignoreCase = true)
        else -> true
      }
      matchesQuery && matchesFilter
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Welcome & Status Banner
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Welcome, ${userSession?.name ?: "Investor"}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Global Market Intelligence & Forecasting",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Market Open status badge
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = BullishGreen.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, BullishGreen.copy(alpha = 0.5f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(BullishGreen)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = marketBreadth.marketStatus,
              color = BullishGreen,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }
        }
      }
    }

    // Demo Mode notice card (if enabled)
    if (isDemoMode) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(RoyalPurple.copy(alpha = 0.15f))
            .border(1.dp, RoyalPurple.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Info, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Demo Data Mode: Realistic market telemetry active",
                style = MaterialTheme.typography.bodySmall,
                color = DarkTextPrimary,
                fontSize = 12.sp
              )
            }
            Text(
              text = "Configure API",
              color = NeonCyan,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.SETTINGS) }
            )
          }
        }
      }
    }

    // Market Overview Section Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Market Overview",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Global Benchmarks",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // 4 Market Overview Cards (Horizontal Scrollable)
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(indices) { indexItem ->
          MarketOverviewCard(
            index = indexItem,
            modifier = Modifier.width(190.dp)
          )
        }
      }
    }

    // Market Summary & Breadth Panel
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
          .padding(16.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Market Breadth & Sentiment",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${marketBreadth.sentimentScore} • ${marketBreadth.sentimentLabel}",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = BullishGreen
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 4-stat grid
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            StatBreadthItem("Advancing", "${marketBreadth.advancing}", BullishGreen)
            StatBreadthItem("Declining", "${marketBreadth.declining}", BearishRed)
            StatBreadthItem("52W Highs", "${marketBreadth.high52Week}", BullishGreen)
            StatBreadthItem("52W Lows", "${marketBreadth.low52Week}", BearishRed)
          }
        }
      }
    }

    // AI Prediction Spotlight Banner
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(
            Brush.horizontalGradient(
              colors = listOf(RoyalPurple.copy(alpha = 0.7f), DarkSurfaceCard.copy(alpha = 0.95f))
            )
          )
          .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
          .clickable { viewModel.openPredictionsFor("TCS") }
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "TOP AI PREDICTION",
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "TCS • Expected Target ₹4,025 (+4.68%)",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Signal: BULLISH • 87% Neural Confidence",
              style = MaterialTheme.typography.bodySmall,
              color = DarkTextSecondary
            )
          }

          Button(
            onClick = { viewModel.openPredictionsFor("TCS") },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
          ) {
            Text(text = "View", color = Color(0xFF00363F), fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Stock Search & Sector Filter Component
    item {
      Column {
        Text(
          text = "Explore & Search Stocks",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        StockSearchBar(
          searchQuery = searchQuery,
          onQueryChange = { viewModel.setSearchQuery(it) },
          selectedFilter = searchFilter,
          onFilterSelect = { viewModel.setSearchFilter(it) }
        )
      }
    }

    // Stocks List
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Market Equities (${filteredStocks.size})",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Tap to view charts & AI analysis",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (filteredStocks.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.SearchOff, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "No stocks matching \"$searchQuery\"", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    } else {
      items(filteredStocks) { stock ->
        StockItemCard(
          stock = stock,
          onStockClick = { symbol -> viewModel.openStockDetails(symbol) },
          onWatchlistToggle = { symbol -> viewModel.toggleWatchlist(symbol) }
        )
      }
    }
  }
}

@Composable
private fun StatBreadthItem(label: String, value: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.ExtraBold,
      color = color
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = DarkTextMuted,
      fontSize = 10.sp
    )
  }
}
