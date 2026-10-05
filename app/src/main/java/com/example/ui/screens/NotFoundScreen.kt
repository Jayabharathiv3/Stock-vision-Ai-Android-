package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SearchOff
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
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StockVisionViewModel

@Composable
fun NotFoundScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(90.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.SearchOff,
        contentDescription = "Not Found",
        tint = NeonCyan,
        modifier = Modifier.size(48.dp)
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "404 - Asset Not Found",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.ExtraBold,
      color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "The requested ticker symbol or financial page does not exist on the StockVision AI telemetry cluster.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
      onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
    ) {
      Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF00363F))
      Spacer(modifier = Modifier.width(8.dp))
      Text("Back to Dashboard", color = Color(0xFF00363F), fontWeight = FontWeight.Bold)
    }
  }
}
