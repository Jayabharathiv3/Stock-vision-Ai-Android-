package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonCyan

@Composable
fun StockSearchBar(
  searchQuery: String,
  onQueryChange: (String) -> Unit,
  selectedFilter: String,
  onFilterSelect: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val filters = listOf("All", "NSE / NIFTY", "US Tech", "Financials", "IT Services", "Energy")

  Column(modifier = modifier.fillMaxWidth()) {
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onQueryChange,
      modifier = Modifier.fillMaxWidth(),
      placeholder = {
        Text(
          text = "Search symbol, company (e.g. TCS, AAPL, INFY, NVDA)...",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
      },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Search",
          tint = NeonCyan
        )
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { onQueryChange("") }) {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = "Clear",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(16.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = NeonCyan,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
      )
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      filters.forEach { filter ->
        val isSelected = filter == selectedFilter
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
              if (isSelected) NeonCyan.copy(alpha = 0.2f)
              else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
            .border(
              width = 1.dp,
              color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
              shape = RoundedCornerShape(20.dp)
            )
            .clickable { onFilterSelect(filter) }
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text(
            text = filter,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}
