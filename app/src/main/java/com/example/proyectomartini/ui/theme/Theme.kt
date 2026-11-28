package com.example.proyectomartini.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MartiniColorScheme = lightColorScheme(
    primary = VerdePino,
    secondary = BronceDorado,
    tertiary = VerdeBosque,

    background = NieveBrillante,
    surface = Blanco,

    error = RojoCaoba,

    onPrimary = Blanco,
    onSecondary = NegroSuave,
    primaryContainer = Color(0xFFDDECE4),
    onPrimaryContainer = VerdePino,
    secondaryContainer = Color(0xFFF6EBC8),
    onSecondaryContainer = NegroSuave,
    surfaceVariant = Color(0xFFE6ECE7),
    onSurfaceVariant = Color(0xFF3B5049),
    outline = Color(0xFF778A82),
    onTertiary = Blanco,

    onBackground = NegroSuave,
    onSurface = NegroSuave,
    onError = Blanco
)

@Composable
fun ProyectoMartiniTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MartiniColorScheme,
        typography = Typography,
        content = content
    )
}