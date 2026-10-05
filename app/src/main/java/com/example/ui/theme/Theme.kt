package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = NeonCyan,
  onPrimary = Color(0xFF00363F),
  primaryContainer = DeepCyan,
  onPrimaryContainer = Color(0xFFC7F3FF),
  secondary = RoyalPurple,
  onSecondary = Color.White,
  secondaryContainer = DarkPurple,
  onSecondaryContainer = Color(0xFFE9D8FD),
  tertiary = GlowPurple,
  onTertiary = Color.White,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorder,
  outlineVariant = Color(0xFF1E293B)
)

private val LightColorScheme = lightColorScheme(
  primary = ElectricBlue,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE0F2FE),
  onPrimaryContainer = Color(0xFF0369A1),
  secondary = RoyalPurple,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFEDE9FE),
  onSecondaryContainer = Color(0xFF5B21B6),
  tertiary = GlowPurple,
  onTertiary = Color.White,
  background = LightBackground,
  onBackground = LightTextPrimary,
  surface = LightSurface,
  onSurface = LightTextPrimary,
  surfaceVariant = LightSurfaceElevated,
  onSurfaceVariant = LightTextSecondary,
  outline = LightBorder,
  outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun StockVisionTheme(
  darkTheme: Boolean = true, // Default to sleek fintech dark theme
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
