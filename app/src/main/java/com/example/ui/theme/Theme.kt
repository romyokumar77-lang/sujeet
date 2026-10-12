package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = SaffronLight,
  onPrimary = RoyalNavy,
  primaryContainer = SaffronDark,
  onPrimaryContainer = Color.White,
  secondary = RoyalIndigoMuted,
  onSecondary = Color.White,
  secondaryContainer = DarkSurfaceVariant,
  onSecondaryContainer = SaffronLight,
  tertiary = TerracottaRose,
  background = DarkBackground,
  onBackground = TextPrimaryLight,
  surface = DarkSurface,
  onSurface = TextPrimaryLight,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = TextSecondaryLight
)

private val LightColorScheme = lightColorScheme(
  primary = SaffronPrimary,
  onPrimary = Color.White,
  primaryContainer = SaffronLight,
  onPrimaryContainer = SaffronDark,
  secondary = RoyalNavy,
  onSecondary = Color.White,
  secondaryContainer = IvorySurfaceVariant,
  onSecondaryContainer = RoyalNavy,
  tertiary = TerracottaRose,
  background = IvoryBackground,
  onBackground = TextPrimaryDark,
  surface = IvorySurface,
  onSurface = TextPrimaryDark,
  surfaceVariant = IvorySurfaceVariant,
  onSurfaceVariant = TextSecondaryDark
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
