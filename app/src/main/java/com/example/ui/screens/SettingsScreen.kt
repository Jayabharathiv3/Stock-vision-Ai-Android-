package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BearishRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RoyalPurple
import com.example.viewmodel.StockVisionViewModel

@Composable
fun SettingsScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  val userSession by viewModel.userSession.collectAsState()
  val isDarkTheme by viewModel.isDarkTheme.collectAsState()
  val isDemoMode by viewModel.isDemoMode.collectAsState()
  val apiUrl by viewModel.apiUrl.collectAsState()
  val backendStatus by viewModel.backendStatus.collectAsState()

  var inputApiUrl by remember(apiUrl) { mutableStateOf(apiUrl) }
  var priceAlertsEnabled by remember { mutableStateOf(true) }
  var aiDigestEnabled by remember { mutableStateOf(true) }
  var testResult by remember { mutableStateOf<String?>(null) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Header
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Settings, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Platform Settings",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Configure API endpoints, themes & alert preferences",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // User Profile Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(RoyalPurple.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = userSession?.name ?: "Guest Trader",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = userSession?.email ?: "guest@stockvision.ai",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = NeonCyan.copy(alpha = 0.15f)
            ) {
              Text(
                text = userSession?.accountTier ?: "Educational & Demo Tier",
                color = NeonCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }
    }

    // Appearance & Theme Section
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Appearance & Interface",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Dark / Light Mode",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = if (isDarkTheme) "Fintech Dark Mode (Active)" else "Clean Light Mode (Active)",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Switch(
              checked = isDarkTheme,
              onCheckedChange = { viewModel.toggleTheme() },
              colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
            )
          }
        }
      }
    }

    // Backend API Configuration (Section 14 & 15)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CloudSync, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Backend API Integration",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Connect StockVision AI frontend to an external REST backend (NEXT_PUBLIC_API_URL):",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = inputApiUrl,
            onValueChange = { inputApiUrl = it },
            label = { Text("Backend API Base URL") },
            placeholder = { Text("https://your-stockvision-backend.com/") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = {
                viewModel.updateApiUrl(inputApiUrl)
                testResult = "Saved API endpoint: $inputApiUrl"
              },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
              Text("Save URL", color = Color(0xFF00363F), fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = {
                viewModel.refreshAll()
                testResult = "Connection tested. Backend status: $backendStatus"
              },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Test Ping")
            }
          }

          testResult?.let { msg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = msg, style = MaterialTheme.typography.bodySmall, color = NeonCyan)
          }

          Spacer(modifier = Modifier.height(14.dp))
          Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
          Spacer(modifier = Modifier.height(14.dp))

          // Demo Mode Switch
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Demo Data Mode",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = if (isDemoMode) "Using offline realistic stock & AI forecasts" else "Querying live backend API",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = isDemoMode,
              onCheckedChange = { viewModel.toggleDemoMode(it) },
              colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
            )
          }
        }
      }
    }

    // Notification Preferences
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Notifications & AI Alerts",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "Daily Neural Prediction Digest", style = MaterialTheme.typography.bodyMedium)
            Switch(checked = aiDigestEnabled, onCheckedChange = { aiDigestEnabled = it })
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "Price Target & Volatility Alerts", style = MaterialTheme.typography.bodyMedium)
            Switch(checked = priceAlertsEnabled, onCheckedChange = { priceAlertsEnabled = it })
          }
        }
      }
    }

    // Logout & Disclaimer
    item {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        OutlinedButton(
          onClick = { viewModel.logout() },
          colors = ButtonDefaults.outlinedButtonColors(contentColor = BearishRed),
          border = androidx.compose.foundation.BorderStroke(1.dp, BearishRed.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
          Icon(Icons.Default.Logout, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Sign Out Session")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "StockVision AI v4.2 • Academic & Financial Research Platform\nAll predictions are educational and not investment advice.",
          style = MaterialTheme.typography.labelSmall,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
          fontSize = 10.sp
        )
      }
    }
  }
}
