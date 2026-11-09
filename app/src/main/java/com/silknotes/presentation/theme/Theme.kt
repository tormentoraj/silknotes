package com.silknotes.presentation.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    primaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFF625B71),
    secondaryContainer = Color(0xFFE8DEF8),
    tertiary = Color(0xFF006874),
    tertiaryContainer = Color(0xFFA6F0FA),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    surface = Color(0xFFFFFBFE),
    surfaceVariant = Color(0xFFE7E0EC),
    background = Color(0xFFFFFBFE),
    onPrimary = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFF21005D),
    onSecondary = Color(0xFFFFFFFF),
    onSecondaryContainer = Color(0xFF1D192B),
    onTertiary = Color(0xFFFFFFFF),
    onTertiaryContainer = Color(0xFF001F25),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    onSurface = Color(0xFF1D1B20),
    onSurfaceVariant = Color(0xFF49454F),
    onBackground = Color(0xFF1D1B20),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    inverseSurface = Color(0xFF343136),
    inverseOnSurface = Color(0xFFF6F0F7),
    scrim = Color(0xFF000000),
    shadow = Color(0xFF000000),
    surfaceTint = Color(0xFF6750A4)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    primaryContainer = Color(0xFF4F378B),
    secondary = Color(0xFFCCC2DC),
    secondaryContainer = Color(0xFF4A4458),
    tertiary = Color(0xFF8BDDDF),
    tertiaryContainer = Color(0xFF00515C),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    surface = Color(0xFF1D1B20),
    surfaceVariant = Color(0xFF49454F),
    background = Color(0xFF1D1B20),
    onPrimary = Color(0xFF381E72),
    onPrimaryContainer = Color(0xFFEADDFF),
    onSecondary = Color(0xFF332D41),
    onSecondaryContainer = Color(0xFFE8DEF8),
    onTertiary = Color(0xFF001F25),
    onTertiaryContainer = Color(0xFFA6F0FA),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    onSurface = Color(0xFFF6F0F7),
    onSurfaceVariant = Color(0xFFCAC4D0),
    onBackground = Color(0xFFF6F0F7),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    inverseSurface = Color(0xFFF6F0F7),
    inverseOnSurface = Color(0xFF343136),
    scrim = Color(0xFF000000),
    shadow = Color(0xFF000000),
    surfaceTint = Color(0xFFD0BCFF)
)

@Composable
fun SilkNotesTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val context = LocalContext.current

    SideEffect {
        (context as? Activity)?.let { activity ->
            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            activity.window.statusBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
            activity.window.navigationBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}