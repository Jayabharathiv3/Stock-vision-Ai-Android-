package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StockVisionViewModel

@Composable
fun LandingScreen(
  viewModel: StockVisionViewModel,
  modifier: Modifier = Modifier
) {
  var expandedFaqIndex by remember { mutableStateOf<Int?>(null) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 20.dp),
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    // Hero Banner Image
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .clip(RoundedCornerShape(24.dp))
          .border(
            width = 1.dp,
            brush = Brush.linearGradient(listOf(NeonCyan, RoyalPurple)),
            shape = RoundedCornerShape(24.dp)
          )
      ) {
        Image(
          painter = painterResource(id = R.drawable.stockvision_hero_1791175744446),
          contentDescription = "StockVision AI Hero",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        // Dark gradient scrim
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, DarkBackground.copy(alpha = 0.85f)),
                startY = 80f
              )
            )
        )
      }
    }

    // Hero Header & Tagline
    item {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = RoyalPurple.copy(alpha = 0.2f),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "NEXT-GEN FINTECH INTELLIGENCE",
              color = NeonCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "AI-Powered Stock Market Intelligence",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.ExtraBold,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "See the Market. Understand the Future. StockVision AI synthesizes multi-factor telemetry and neural forecasting models for investors, traders, and students.",
          style = MaterialTheme.typography.bodyMedium,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Hero CTA Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Button(
            onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
            modifier = Modifier
              .weight(1f)
              .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
          ) {
            Text(
              text = "Explore Dashboard",
              fontWeight = FontWeight.Bold,
              color = Color(0xFF00363F)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Default.ArrowForward,
              contentDescription = null,
              tint = Color(0xFF00363F),
              modifier = Modifier.size(18.dp)
            )
          }

          OutlinedButton(
            onClick = { viewModel.navigateTo(AppScreen.LOGIN) },
            modifier = Modifier
              .weight(1f)
              .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalPurple)
          ) {
            Text(
              text = "Get Started",
              fontWeight = FontWeight.Bold,
              color = RoyalPurple
            )
          }
        }
      }
    }

    // Live Market Telemetry Stats
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Platform Telemetry",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(14.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            MetricStat(label = "Market Cap Tracked", value = "₹120T+")
            MetricStat(label = "Model Accuracy", value = "87.4%")
            MetricStat(label = "Predictions Made", value = "1.2M+")
          }
        }
      }
    }

    // Key Features Section
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Built for Next-Gen Investors",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        FeatureCard(
          icon = Icons.Default.Psychology,
          title = "Multi-Horizon AI Predictions",
          description = "Next-day, 7-day, and 30-day price target forecasts backed by multi-indicator neural confidence scoring."
        )
        Spacer(modifier = Modifier.height(10.dp))
        FeatureCard(
          icon = Icons.Default.ShowChart,
          title = "Interactive Technical Suite",
          description = "Switch seamlessly between candlestick & line views, touch scrubbers, RSI oscillators, and MACD indicators."
        )
        Spacer(modifier = Modifier.height(10.dp))
        FeatureCard(
          icon = Icons.Default.CompareArrows,
          title = "4-Way Asset Comparison",
          description = "Benchmark P/E ratios, risk grades, and momentum against peer equities side-by-side."
        )
        Spacer(modifier = Modifier.height(10.dp))
        FeatureCard(
          icon = Icons.Default.AccountBalanceWallet,
          title = "Real-Time Portfolio & Watchlist",
          description = "Persist transactions offline with Room DB, track live P&L, and monitor asset distribution."
        )
      }
    }

    // How It Works Section
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
          .padding(18.dp)
      ) {
        Text(
          text = "How StockVision AI Works",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(14.dp))

        StepItem(number = "1", title = "Real-Time Telemetry Ingestion", desc = "Feeds live tick data from NSE, BSE, and US exchanges.")
        StepItem(number = "2", title = "Indicator & Sentiment Synthesis", desc = "Evaluates 40+ indicators including RSI, MACD, and market sentiment.")
        StepItem(number = "3", title = "Neural Confidence Modeling", desc = "Simulates multitimeframe risk-adjusted price distributions.")
        StepItem(number = "4", title = "Actionable Signal Generation", desc = "Delivers Bullish, Bearish, or Neutral recommendations.")
      }
    }

    // FAQ Section
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Frequently Asked Questions",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        val faqs = listOf(
          "How accurate are the AI predictions?" to "Our models deliver 84-91% directional confidence over multi-horizon backtesting, combining momentum, volume, and statistical volatility.",
          "Can I connect my own backend API?" to "Yes! StockVision AI features a modular repository. Enter your NEXT_PUBLIC_API_URL in Settings to stream live proprietary endpoints.",
          "Is this financial advice?" to "No. StockVision AI is an educational intelligence tool designed for students, researchers, and self-directed investors."
        )

        faqs.forEachIndexed { index, (question, answer) ->
          val isExpanded = expandedFaqIndex == index
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
              .clickable { expandedFaqIndex = if (isExpanded) null else index }
              .padding(14.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = question,
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.weight(1f)
                )
                Icon(
                  imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                  contentDescription = null,
                  tint = NeonCyan
                )
              }
              if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = answer,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // Technology Stack Section
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .background(Color.Black.copy(alpha = 0.3f))
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Powered by Modern Engineering",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          TechBadge("Kotlin & Compose")
          TechBadge("Room SQLite")
          TechBadge("Retrofit / REST")
          TechBadge("AI Neural Engine")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "StockVision AI • Final-Year Project Architecture & Commercial SaaS Ready",
          style = MaterialTheme.typography.labelSmall,
          color = DarkTextMuted,
          fontSize = 10.sp
        )
      }
    }
  }
}

@Composable
private fun MetricStat(label: String, value: String) {
  Column {
    Text(
      text = value,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      color = NeonCyan
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = DarkTextSecondary,
      fontSize = 11.sp
    )
  }
}

@Composable
private fun FeatureCard(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  description: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
      .padding(14.dp),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      modifier = Modifier
        .size(42.dp)
        .clip(CircleShape)
        .background(RoyalPurple.copy(alpha = 0.25f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
    }
    Spacer(modifier = Modifier.width(14.dp))
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
private fun StepItem(number: String, title: String, desc: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      modifier = Modifier
        .size(28.dp)
        .clip(CircleShape)
        .background(NeonCyan.copy(alpha = 0.2f)),
      contentAlignment = Alignment.Center
    ) {
      Text(text = number, color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column {
      Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
      Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun TechBadge(name: String) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    modifier = Modifier.padding(2.dp)
  ) {
    Text(
      text = name,
      style = MaterialTheme.typography.labelSmall,
      color = NeonCyan,
      fontSize = 10.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}
