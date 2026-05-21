package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GreenfieldPrimaryContainer,
    onPrimary = GreenfieldOnPrimaryContainer,
    primaryContainer = GreenfieldPrimary,
    onPrimaryContainer = GreenfieldOnPrimary,
    secondary = GreenfieldSecondary,
    onSecondary = GreenfieldOnSecondary,
    secondaryContainer = GreenfieldSecondaryContainer,
    onSecondaryContainer = GreenfieldOnSecondaryContainer,
    tertiary = GreenfieldTertiary,
    onTertiary = GreenfieldOnTertiary,
    tertiaryContainer = GreenfieldTertiaryContainer,
    onTertiaryContainer = GreenfieldOnTertiaryContainer,
    background = GreenfieldOnBackground, // reverse for dark mode
    onBackground = GreenfieldBackground,
    surface = GreenfieldOnBackground,
    onSurface = GreenfieldBackground,
    surfaceVariant = GreenfieldOnSurfaceVariant,
    onSurfaceVariant = GreenfieldSurfaceVariant,
    error = GreenfieldError,
    onError = GreenfieldOnError,
    errorContainer = GreenfieldErrorContainer,
    onErrorContainer = GreenfieldOnErrorContainer,
    outline = GreenfieldOutline,
    outlineVariant = GreenfieldOutlineVariant
)

private val LightColorScheme = lightColorScheme(
    primary = GreenfieldPrimary,
    onPrimary = GreenfieldOnPrimary,
    primaryContainer = GreenfieldPrimaryContainer,
    onPrimaryContainer = GreenfieldOnPrimaryContainer,
    secondary = GreenfieldSecondary,
    onSecondary = GreenfieldOnSecondary,
    secondaryContainer = GreenfieldSecondaryContainer,
    onSecondaryContainer = GreenfieldOnSecondaryContainer,
    tertiary = GreenfieldTertiary,
    onTertiary = GreenfieldOnTertiary,
    tertiaryContainer = GreenfieldTertiaryContainer,
    onTertiaryContainer = GreenfieldOnTertiaryContainer,
    background = GreenfieldBackground,
    onBackground = GreenfieldOnBackground,
    surface = GreenfieldSurface,
    onSurface = GreenfieldOnSurface,
    surfaceVariant = GreenfieldSurfaceVariant,
    onSurfaceVariant = GreenfieldOnSurfaceVariant,
    error = GreenfieldError,
    onError = GreenfieldOnError,
    errorContainer = GreenfieldErrorContainer,
    onErrorContainer = GreenfieldOnErrorContainer,
    outline = GreenfieldOutline,
    outlineVariant = GreenfieldOutlineVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set dynamic color to false by default to ensure brand colors are applied across all devices
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
