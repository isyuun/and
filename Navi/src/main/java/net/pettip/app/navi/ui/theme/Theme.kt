package net.pettip.app.navi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2D3034),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF969BA1),
    onSecondary = Color(0xFF45494D),
    outline = Color(0xFF45484D),
    outlineVariant = Color(0xFFD5D6D6),
    primaryContainer = Color(0xFF6A7078),
    onPrimaryContainer = Color(0xFF1E2124),
    tertiary = Color(0xFF1E2124),
    onTertiary = Color(0xFF1E2124),
    surface = Color(0x26000000),
    onSurface = Color(0x33000000),
    onSecondaryContainer = Color(0xFF44474B),
    secondaryContainer = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFFFFF),
    onTertiaryContainer = Color(0xFF222222),
    surfaceVariant = Color(0xFF1E2124),
    onSurfaceVariant = Color(0xFF232323)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF222222),
    secondary = Color(0xFF737980),
    onSecondary = Color(0xFFDDDDDD),
    outline = Color(0xFFE3E9F2),
    outlineVariant = Color(0xFF333333),
    primaryContainer = Color(0xFFB5B9BE),
    onPrimaryContainer = Color(0xFFF6F8FC),
    tertiary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFF1F4FB),
    surface = Color(0x264E6085),
    onSurface = Color(0x334E6085),
    onSecondaryContainer = Color(0xFFE3E9F2),
    secondaryContainer = Color(0xFF4783F5),
    tertiaryContainer = Color(0xFF333333),
    onTertiaryContainer = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE3E9F2),
    onSurfaceVariant = Color(0xFFF1F1F1)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}