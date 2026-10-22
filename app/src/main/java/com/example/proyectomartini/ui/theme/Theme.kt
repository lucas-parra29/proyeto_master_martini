package com.example.proyectomartini.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MartiniColorScheme = lightColorScheme(
    primary = VerdePino,
    secondary = BronceDorado,
    tertiary = VerdeBosque,

    background = NieveBrillante,
    surface = Blanco,

    error = RojoCaoba,

    onPrimary = Blanco,
    onSecondary = Blanco,
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