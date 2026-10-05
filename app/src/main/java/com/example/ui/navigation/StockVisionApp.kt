package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StockVisionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockVisionApp(
  viewModel: StockVisionViewModel
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val isDarkTheme by viewModel.isDarkTheme.collectAsState()
  val userSession by viewModel.userSession.collectAsState()
  val notifications by viewModel.notifications.collectAsState()

  var showNotificationsDialog by remember { mutableStateOf(false) }

  // Handle hardware / gesture back navigation
  BackHandler(enabled = currentScreen != AppScreen.DASHBOARD && currentScreen != AppScreen.LANDING) {
    if (!viewModel.popBack()) {
      viewModel.navigateTo(AppScreen.DASHBOARD)
    }
  }

  StockVisionTheme(darkTheme = isDarkTheme) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
      val isWideScreen = maxWidth > 600.dp

      val showChrome = currentScreen != AppScreen.LANDING &&
          currentScreen != AppScreen.LOGIN &&
          currentScreen != AppScreen.REGISTER

      Scaffold(
        topBar = {
          if (showChrome) {
            TopAppBar(
              title = {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.DASHBOARD) }
                ) {
                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .background(RoyalPurple.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.AutoAwesome,
                      contentDescription = null,
                      tint = NeonCyan,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "StockVision AI",
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.ExtraBold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = "See the Market. Understand the Future.",
                      style = MaterialTheme.typography.labelSmall,
                      fontSize = 9.sp,
                      color = NeonCyan
                    )
                  }
                }
              },
              actions = {
                // Theme toggle button
                IconButton(onClick = { viewModel.toggleTheme() }) {
                  Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = NeonCyan
                  )
                }

                // Notifications Bell with Badge
                IconButton(onClick = { showNotificationsDialog = true }) {
                  BadgedBox(
                    badge = {
                      if (notifications.isNotEmpty()) {
                        Badge(containerColor = NeonCyan, contentColor = Color(0xFF00363F)) {
                          Text("${notifications.size}")
                        }
                      }
                    }
                  ) {
                    Icon(
                      imageVector = Icons.Outlined.Notifications,
                      contentDescription = "Notifications",
                      tint = MaterialTheme.colorScheme.onSurface
                    )
                  }
                }

                // Profile Avatar / Settings Shortcut
                IconButton(onClick = { viewModel.navigateTo(AppScreen.SETTINGS) }) {
                  Box(
                    modifier = Modifier
                      .size(32.dp)
                      .clip(CircleShape)
                      .background(RoyalPurple.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = (userSession?.name?.take(1) ?: "U"),
                      fontWeight = FontWeight.Bold,
                      color = NeonCyan,
                      fontSize = 14.sp
                    )
                  }
                }
              },
              colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                titleContentColor = MaterialTheme.colorScheme.onSurface
              )
            )
          }
        },
        bottomBar = {
          if (showChrome && !isWideScreen) {
            NavigationBar(
              containerColor = MaterialTheme.colorScheme.surface,
              contentColor = MaterialTheme.colorScheme.onSurface,
              windowInsets = WindowInsets.navigationBars
            ) {
              val navItems = listOf(
                Triple(AppScreen.DASHBOARD, "Dashboard", Icons.Default.Dashboard to Icons.Outlined.Dashboard),
                Triple(AppScreen.PREDICTIONS, "AI Predict", Icons.Default.Psychology to Icons.Outlined.Psychology),
                Triple(AppScreen.PORTFOLIO, "Portfolio", Icons.Default.AccountBalanceWallet to Icons.Outlined.AccountBalanceWallet),
                Triple(AppScreen.COMPARE, "Compare", Icons.Default.CompareArrows to Icons.Outlined.CompareArrows),
                Triple(AppScreen.NEWS, "News", Icons.Default.Newspaper to Icons.Outlined.Newspaper)
              )

              navItems.forEach { (screen, label, icons) ->
                val isSelected = currentScreen == screen
                NavigationBarItem(
                  selected = isSelected,
                  onClick = { viewModel.navigateTo(screen) },
                  icon = {
                    Icon(
                      imageVector = if (isSelected) icons.first else icons.second,
                      contentDescription = label,
                      tint = if (isSelected) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  },
                  label = {
                    Text(
                      text = label,
                      fontSize = 10.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSelected) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = RoyalPurple.copy(alpha = 0.25f)
                  )
                )
              }
            }
          }
        }
      ) { innerPadding ->
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          // Navigation Rail on tablet / wide layouts
          if (showChrome && isWideScreen) {
            NavigationRail(
              containerColor = MaterialTheme.colorScheme.surface,
              header = {
                IconButton(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                  Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan)
                }
              }
            ) {
              val navItems = listOf(
                Triple(AppScreen.DASHBOARD, "Dashboard", Icons.Default.Dashboard),
                Triple(AppScreen.PREDICTIONS, "AI Forecast", Icons.Default.Psychology),
                Triple(AppScreen.PORTFOLIO, "Portfolio", Icons.Default.AccountBalanceWallet),
                Triple(AppScreen.WATCHLIST, "Watchlist", Icons.Default.Bookmark),
                Triple(AppScreen.COMPARE, "Compare", Icons.Default.CompareArrows),
                Triple(AppScreen.NEWS, "News", Icons.Default.Newspaper),
                Triple(AppScreen.SETTINGS, "Settings", Icons.Default.Settings)
              )

              navItems.forEach { (screen, label, icon) ->
                val isSelected = currentScreen == screen
                NavigationRailItem(
                  selected = isSelected,
                  onClick = { viewModel.navigateTo(screen) },
                  icon = { Icon(icon, contentDescription = label) },
                  label = { Text(label, fontSize = 10.sp) },
                  colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    indicatorColor = RoyalPurple.copy(alpha = 0.25f)
                  )
                )
              }
            }
          }

          // Content Pane
          Box(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
              AppScreen.LANDING -> LandingScreen(viewModel = viewModel)
              AppScreen.LOGIN -> AuthScreen(viewModel = viewModel, initialIsRegister = false)
              AppScreen.REGISTER -> AuthScreen(viewModel = viewModel, initialIsRegister = true)
              AppScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
              AppScreen.STOCK_DETAILS -> StockDetailsScreen(viewModel = viewModel)
              AppScreen.PREDICTIONS -> AiPredictionsScreen(viewModel = viewModel)
              AppScreen.COMPARE -> CompareScreen(viewModel = viewModel)
              AppScreen.PORTFOLIO -> PortfolioScreen(viewModel = viewModel)
              AppScreen.WATCHLIST -> WatchlistScreen(viewModel = viewModel)
              AppScreen.NEWS -> MarketNewsScreen(viewModel = viewModel)
              AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
              AppScreen.NOT_FOUND -> NotFoundScreen(viewModel = viewModel)
            }
          }
        }
      }

      // Notifications Dialog
      if (showNotificationsDialog) {
        AlertDialog(
          onDismissRequest = { showNotificationsDialog = false },
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = NeonCyan)
              Spacer(modifier = Modifier.width(8.dp))
              Text("AI Market Alerts", fontWeight = FontWeight.Bold)
            }
          },
          text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              notifications.forEach { notif ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(10.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(notif, style = MaterialTheme.typography.bodySmall)
                }
              }
            }
          },
          confirmButton = {
            TextButton(onClick = { showNotificationsDialog = false }) {
              Text("Close", color = NeonCyan)
            }
          }
        )
      }
    }
  }
}
