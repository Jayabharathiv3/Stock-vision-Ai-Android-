package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PortfolioHolding
import com.example.ui.theme.*
import com.example.viewmodel.StockVisionViewModel

@Composable
fun PortfolioScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  val holdings by viewModel.portfolioHoldings.collectAsState()
  var showAddDialog by remember { mutableStateOf(false) }

  val totalInvested = remember(holdings) { holdings.sumOf { it.investedAmount } }
  val totalCurrent = remember(holdings) { holdings.sumOf { it.currentValue } }
  val totalPnl = remember(totalInvested, totalCurrent) { totalCurrent - totalInvested }
  val totalPnlPercent = remember(totalInvested, totalPnl) {
    if (totalInvested > 0) (totalPnl / totalInvested) * 100.0 else 0.0
  }
  val isOverallProfit = totalPnl >= 0
  val pnlColor = if (isOverallProfit) BullishGreen else BearishRed

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Portfolio Intelligence",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onBackground
            )
          }
          Text(
            text = "Track offline Room persistence, allocations & return alpha",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        FloatingActionButton(
          onClick = { showAddDialog = true },
          containerColor = NeonCyan,
          contentColor = Color(0xFF00363F),
          shape = CircleShape,
          modifier = Modifier.size(48.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Holding")
        }
      }
    }

    // Portfolio Summary Card
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(
            androidx.compose.ui.graphics.Brush.linearGradient(
              listOf(DarkSurfaceElevated, DarkSurfaceCard)
            )
          )
          .border(
            1.dp,
            androidx.compose.ui.graphics.Brush.linearGradient(listOf(RoyalPurple, NeonCyan)),
            RoundedCornerShape(24.dp)
          )
          .padding(20.dp)
      ) {
        Column {
          Text(text = "TOTAL PORTFOLIO VALUE", style = MaterialTheme.typography.labelSmall, color = DarkTextSecondary, fontSize = 11.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "₹%,.2f".format(totalCurrent),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )

          Spacer(modifier = Modifier.height(14.dp))
          Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Total Investment", style = MaterialTheme.typography.labelSmall, color = DarkTextMuted)
              Text(
                text = "₹%,.2f".format(totalInvested),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(text = "Overall Gain / Loss", style = MaterialTheme.typography.labelSmall, color = DarkTextMuted)
              Text(
                text = "%s₹%,.2f (%s%.2f%%)".format(
                  if (isOverallProfit) "+" else "",
                  totalPnl,
                  if (isOverallProfit) "+" else "",
                  totalPnlPercent
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = pnlColor
              )
            }
          }
        }
      }
    }

    // Allocation Breakdown Donut Section
    if (holdings.isNotEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = "Asset Distribution Breakdown",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(14.dp))

            val colors = listOf(NeonCyan, RoyalPurple, BullishGreen, GlowPurple, NeutralAmber)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Donut Chart
              Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                  val strokeWidth = 12.dp.toPx()
                  val radius = (size.minDimension - strokeWidth) / 2f
                  var startAngle = -90f

                  holdings.forEachIndexed { idx, holding ->
                    val fraction = if (totalCurrent > 0) (holding.currentValue / totalCurrent).toFloat() else 0f
                    val sweep = fraction * 360f
                    val color = colors[idx % colors.size]

                    drawArc(
                      color = color,
                      startAngle = startAngle,
                      sweepAngle = sweep,
                      useCenter = false,
                      style = Stroke(width = strokeWidth)
                    )
                    startAngle += sweep
                  }
                }
                Text(
                  text = "${holdings.size} Assets",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.width(16.dp))

              // Legend
              Column(modifier = Modifier.weight(1f)) {
                holdings.forEachIndexed { idx, holding ->
                  val color = colors[idx % colors.size]
                  val percent = if (totalCurrent > 0) (holding.currentValue / totalCurrent) * 100.0 else 0.0
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                  ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = holding.symbol, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "%.1f%%".format(percent), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                }
              }
            }
          }
        }
      }
    }

    // Holdings List
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Holdings (${holdings.size})",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    if (holdings.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Savings, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "No holdings in portfolio yet", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Tap the + button to record your first stock holding.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    } else {
      items(holdings, key = { it.id }) { holding ->
        HoldingItemCard(
          holding = holding,
          onDelete = { viewModel.removePortfolioHolding(holding.id) },
          onStockClick = { viewModel.openStockDetails(holding.symbol) }
        )
      }
    }
  }

  // Add Stock Dialog
  if (showAddDialog) {
    var symbolInput by remember { mutableStateOf("TCS") }
    var quantityInput by remember { mutableStateOf("10") }
    var priceInput by remember { mutableStateOf("3800.0") }

    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("Add Stock to Portfolio") },
      text = {
        Column {
          OutlinedTextField(
            value = symbolInput,
            onValueChange = { symbolInput = it.uppercase() },
            label = { Text("Stock Symbol (e.g. TCS, INFY, NVDA)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = quantityInput,
            onValueChange = { quantityInput = it },
            label = { Text("Quantity") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = priceInput,
            onValueChange = { priceInput = it },
            label = { Text("Purchase Price") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val q = quantityInput.toIntOrNull() ?: 1
            val p = priceInput.toDoubleOrNull() ?: 100.0
            viewModel.addPortfolioHolding(symbolInput, q, p)
            showAddDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
        ) {
          Text("Add Holding", color = Color(0xFF00363F), fontWeight = FontWeight.Bold)
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
private fun HoldingItemCard(
  holding: PortfolioHolding,
  onDelete: () -> Unit,
  onStockClick: () -> Unit
) {
  val isProfit = holding.isProfitable
  val color = if (isProfit) BullishGreen else BearishRed

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
      .clickable(onClick = onStockClick)
      .padding(16.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = holding.symbol, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${holding.quantity} shares",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Text(text = holding.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        IconButton(onClick = onDelete) {
          Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = BearishRed.copy(alpha = 0.8f))
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "Current Value", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "${holding.currency}%,.2f".format(holding.currentValue),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
          )
        }

        Column {
          Text(text = "Avg Buy Price", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "${holding.currency}%,.2f".format(holding.purchasePrice),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(text = "P&L Return", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "%s${holding.currency}%,.2f (%s%.2f%%)".format(
              if (isProfit) "+" else "",
              holding.totalProfitLoss,
              if (isProfit) "+" else "",
              holding.profitLossPercent
            ),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
          )
        }
      }
    }
  }
}
