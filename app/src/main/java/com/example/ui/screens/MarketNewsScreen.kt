package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketNews
import com.example.model.NewsSentiment
import com.example.ui.theme.*
import com.example.viewmodel.StockVisionViewModel

@Composable
fun MarketNewsScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  val newsList by viewModel.marketNews.collectAsState()
  var selectedSentiment by remember { mutableStateOf<NewsSentiment?>(null) }

  val filteredNews = remember(newsList, selectedSentiment) {
    if (selectedSentiment == null) newsList
    else newsList.filter { it.sentiment == selectedSentiment }
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
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Newspaper, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Market News & Sentiment",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
        Text(
          text = "Real-time market sentiment intelligence & news catalysts",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Sentiment Filter Chips
    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
          FilterChip(
            selected = selectedSentiment == null,
            onClick = { selectedSentiment = null },
            label = { Text("All News (${newsList.size})") }
          )
        }
        item {
          FilterChip(
            selected = selectedSentiment == NewsSentiment.POSITIVE,
            onClick = { selectedSentiment = NewsSentiment.POSITIVE },
            label = { Text("Positive Sentiment") },
            leadingIcon = { Icon(Icons.Default.TrendingUp, contentDescription = null, tint = BullishGreen, modifier = Modifier.size(16.dp)) }
          )
        }
        item {
          FilterChip(
            selected = selectedSentiment == NewsSentiment.NEUTRAL,
            onClick = { selectedSentiment = NewsSentiment.NEUTRAL },
            label = { Text("Neutral") },
            leadingIcon = { Icon(Icons.Default.TrendingFlat, contentDescription = null, tint = NeutralAmber, modifier = Modifier.size(16.dp)) }
          )
        }
        item {
          FilterChip(
            selected = selectedSentiment == NewsSentiment.NEGATIVE,
            onClick = { selectedSentiment = NewsSentiment.NEGATIVE },
            label = { Text("Cautious / Negative") },
            leadingIcon = { Icon(Icons.Default.TrendingDown, contentDescription = null, tint = BearishRed, modifier = Modifier.size(16.dp)) }
          )
        }
      }
    }

    // News Items
    items(filteredNews, key = { it.id }) { news ->
      NewsItemCard(
        news = news,
        onStockClick = { symbol -> viewModel.openStockDetails(symbol) }
      )
    }

    // Educational Mock Data Notice
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color.Black.copy(alpha = 0.2f))
          .padding(12.dp)
      ) {
        Text(
          text = "Notice: Market news feed is running in Demo Data mode until a dedicated external Live News API is connected in Settings.",
          style = MaterialTheme.typography.labelSmall,
          color = DarkTextMuted,
          fontSize = 11.sp
        )
      }
    }
  }
}

@Composable
private fun NewsItemCard(
  news: MarketNews,
  onStockClick: (String) -> Unit
) {
  val (sentimentColor, sentimentIcon) = when (news.sentiment) {
    NewsSentiment.POSITIVE -> Pair(BullishGreen, Icons.Default.TrendingUp)
    NewsSentiment.NEUTRAL -> Pair(NeutralAmber, Icons.Default.TrendingFlat)
    NewsSentiment.NEGATIVE -> Pair(BearishRed, Icons.Default.TrendingDown)
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Related Stock Chip
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = NeonCyan.copy(alpha = 0.15f),
          modifier = Modifier.clickable { onStockClick(news.relatedStock) }
        ) {
          Text(
            text = news.relatedStock,
            color = NeonCyan,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        // Sentiment Badge
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(sentimentColor.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(imageVector = sentimentIcon, contentDescription = null, tint = sentimentColor, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = news.sentiment.name,
            color = sentimentColor,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = news.headline,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = news.summary,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = news.source,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = news.date,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
