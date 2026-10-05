package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.StockItemCard
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.StockVisionViewModel

@Composable
fun WatchlistScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  val stocks by viewModel.stocks.collectAsState()
  val watchlistStocks = remember(stocks) {
    stocks.filter { it.isWatchlist }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Bookmark, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Saved Watchlist (${watchlistStocks.size})",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Tracked high-priority equities stored locally in Room DB",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    if (watchlistStocks.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Your watchlist is empty", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Tap the bookmark icon on any stock card to track it here.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(watchlistStocks) { stock ->
        StockItemCard(
          stock = stock,
          onStockClick = { symbol -> viewModel.openStockDetails(symbol) },
          onWatchlistToggle = { symbol -> viewModel.toggleWatchlist(symbol) }
        )
      }
    }
  }
}
