/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.12.15
 *
 */

package net.pettip.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
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
    primary = Color(0xFF4783F5),//Color.Red, //test
    inversePrimary = Color(0xFFF54F68),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFF54F68),//Color.Blue,//test
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF1C1B1F),
    background = Color(0xFF1E2124),//Color.Yellow,//test
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF2D3034),//Color.Cyan,//test
    onSurface = Color(0xFFFFFFFF),
    outline = Color(0xFF45484D)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4783F5),//Color.Blue,//test
    inversePrimary = Color(0xFFF54F68),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFF54F68),//Color.Red,//test
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF1C1B1F),
    background = Color(0xFFF6F8FC),//Color.Cyan,//test
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF),//Color.Yellow,//test
    onSurface = Color(0xFF1C1B1F),
    outline = Color(0xFFE3E9F2)
)

val invertedColors: ColorScheme
    @Composable
    get() {
        return MaterialTheme.colorScheme.copy(
            primary = MaterialTheme.colorScheme.onPrimary,
            onPrimary = MaterialTheme.colorScheme.primary,
            secondary = MaterialTheme.colorScheme.onSecondary,
            onSecondary = MaterialTheme.colorScheme.secondary,
            tertiary = MaterialTheme.colorScheme.onTertiary,
            onTertiary = MaterialTheme.colorScheme.tertiary,
            background = MaterialTheme.colorScheme.onBackground,
            onBackground = MaterialTheme.colorScheme.background,
            surface = MaterialTheme.colorScheme.onSurface,
            onSurface = MaterialTheme.colorScheme.surface,
            surfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant,
            onSurfaceVariant = MaterialTheme.colorScheme.surfaceVariant,
            surfaceTint = MaterialTheme.colorScheme.surfaceTint,
            inverseSurface = MaterialTheme.colorScheme.surface,
            inverseOnSurface = MaterialTheme.colorScheme.onSurface,
            error = MaterialTheme.colorScheme.onError,
            onError = MaterialTheme.colorScheme.error,
            errorContainer = MaterialTheme.colorScheme.onErrorContainer,
            onErrorContainer = MaterialTheme.colorScheme.errorContainer,
            outline = MaterialTheme.colorScheme.onSurface,
            outlineVariant = MaterialTheme.colorScheme.onSurfaceVariant,
            scrim = MaterialTheme.colorScheme.inverseSurface
        )
    }

@Composable
fun APPTheme(
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