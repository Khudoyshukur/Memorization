package uz.androdev.memorization.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Material 3 color schemes
private val memorizationDarkColorScheme = darkColorScheme(
    primary = memorizationDarkPrimary,
    onPrimary = memorizationDarkOnPrimary,
    primaryContainer = memorizationDarkPrimaryContainer,
    onPrimaryContainer = memorizationDarkOnPrimaryContainer,
    inversePrimary = memorizationDarkPrimaryInverse,
    secondary = memorizationDarkSecondary,
    onSecondary = memorizationDarkOnSecondary,
    secondaryContainer = memorizationDarkSecondaryContainer,
    onSecondaryContainer = memorizationDarkOnSecondaryContainer,
    tertiary = memorizationDarkTertiary,
    onTertiary = memorizationDarkOnTertiary,
    tertiaryContainer = memorizationDarkTertiaryContainer,
    onTertiaryContainer = memorizationDarkOnTertiaryContainer,
    error = memorizationDarkError,
    onError = memorizationDarkOnError,
    errorContainer = memorizationDarkErrorContainer,
    onErrorContainer = memorizationDarkOnErrorContainer,
    background = memorizationDarkBackground,
    onBackground = memorizationDarkOnBackground,
    surface = memorizationDarkSurface,
    onSurface = memorizationDarkOnSurface,
    inverseSurface = memorizationDarkInverseSurface,
    inverseOnSurface = memorizationDarkInverseOnSurface,
    surfaceVariant = memorizationDarkSurfaceVariant,
    onSurfaceVariant = memorizationDarkOnSurfaceVariant,
    outline = memorizationDarkOutline
)

private val memorizationLightColorScheme = lightColorScheme(
    primary = memorizationLightPrimary,
    onPrimary = memorizationLightOnPrimary,
    primaryContainer = memorizationLightPrimaryContainer,
    onPrimaryContainer = memorizationLightOnPrimaryContainer,
    inversePrimary = memorizationLightPrimaryInverse,
    secondary = memorizationLightSecondary,
    onSecondary = memorizationLightOnSecondary,
    secondaryContainer = memorizationLightSecondaryContainer,
    onSecondaryContainer = memorizationLightOnSecondaryContainer,
    tertiary = memorizationLightTertiary,
    onTertiary = memorizationLightOnTertiary,
    tertiaryContainer = memorizationLightTertiaryContainer,
    onTertiaryContainer = memorizationLightOnTertiaryContainer,
    error = memorizationLightError,
    onError = memorizationLightOnError,
    errorContainer = memorizationLightErrorContainer,
    onErrorContainer = memorizationLightOnErrorContainer,
    background = memorizationLightBackground,
    onBackground = memorizationLightOnBackground,
    surface = memorizationLightSurface,
    onSurface = memorizationLightOnSurface,
    inverseSurface = memorizationLightInverseSurface,
    inverseOnSurface = memorizationLightInverseOnSurface,
    surfaceVariant = memorizationLightSurfaceVariant,
    onSurfaceVariant = memorizationLightOnSurfaceVariant,
    outline = memorizationLightOutline
)

@Composable
fun MemorizationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val replyColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> memorizationDarkColorScheme
        else -> memorizationLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = replyColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = replyColorScheme,
        typography = memorizationTypography,
        shapes = shapes,
        content = content
    )
}