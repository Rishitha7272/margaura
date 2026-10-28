package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Navy,
    onPrimary = Color.White,
    secondary = Amber,
    onSecondary = Navy,
    tertiary = SlateText,
    background = OffWhiteBg,
    onBackground = Navy,
    surface = Color.White,
    onSurface = Navy,
    surfaceVariant = OffWhiteBg,
    onSurfaceVariant = SlateText,
    error = Amber,
    onError = Color.White,
    outline = SoftGreyBorder,
)

// Define dark theme mapping for compatibility, though app requested "Light theme"
private val DarkColorScheme = LightColorScheme // Force light theme for MARGAURA by default

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic colors to enforce brand identity
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> LightColorScheme // Always use brand colors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
