package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Stock
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NeonCyan

@Composable
fun StockItemCard(
  stock: Stock,
  onStockClick: (String) -> Unit,
  onWatchlistToggle: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val isPositive = stock.isPositive
  val statusColor = if (isPositive) BullishGreen else BearishRed

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
        shape = RoundedCornerShape(16.dp)
      )
      .clickable { onStockClick(stock.symbol) }
      .padding(horizontal = 14.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Symbol Avatar
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = stock.symbol.take(2),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Symbol and Name
      Column(modifier = Modifier.weight(1.3f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = stock.symbol,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = stock.exchange,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
              .padding(horizontal = 4.dp, vertical = 1.dp)
          )
        }
        Text(
          text = stock.name,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1
        )
      }

      // Sparkline
      if (stock.sparkline.isNotEmpty()) {
        Box(
          modifier = Modifier
            .width(54.dp)
            .height(28.dp)
            .padding(horizontal = 4.dp)
        ) {
          StockSparklineChart(
            data = stock.sparkline,
            isPositive = isPositive,
            strokeWidth = 2.5f,
            showGradientFill = false
          )
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Price & Change
      Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.weight(1.1f)
      ) {
        Text(
          text = stock.formattedPrice,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "%s%.2f%%".format(if (isPositive) "+" else "", stock.changePercent),
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold,
          color = statusColor
        )
      }

      // Watchlist toggle button
      IconButton(
        onClick = { onWatchlistToggle(stock.symbol) },
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = if (stock.isWatchlist) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
          contentDescription = "Toggle Watchlist",
          tint = if (stock.isWatchlist) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
