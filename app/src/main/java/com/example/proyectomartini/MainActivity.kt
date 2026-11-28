package com.example.proyectomartini

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.proyectomartini.ui.screens.ESCALA_LECTURA
import com.example.proyectomartini.ui.screens.ajustesLectura
import com.example.proyectomartini.ui.navigation.MartiniNavigation
import com.example.proyectomartini.ui.theme.ProyectoMartiniTheme

class MainActivity : ComponentActivity() {
    private var destinoAviso by mutableStateOf<String?>(null)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        destinoAviso = intent.getStringExtra("destino_aviso")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        destinoAviso = if (savedInstanceState == null) intent.getStringExtra("destino_aviso")
            else savedInstanceState.getString("destino_aviso")

        androidx.core.view.WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
        setContent {
            val ajustes = remember { ajustesLectura(this@MainActivity) }
            val densidad = LocalDensity.current
            var escala by remember { mutableFloatStateOf(ajustes.getFloat(ESCALA_LECTURA, 1f)) }
            DisposableEffect(ajustes) {
                val escucha = android.content.SharedPreferences.OnSharedPreferenceChangeListener { pref, clave ->
                    if (clave == ESCALA_LECTURA) escala = pref.getFloat(ESCALA_LECTURA, 1f)
                }
                ajustes.registerOnSharedPreferenceChangeListener(escucha)
                onDispose { ajustes.unregisterOnSharedPreferenceChangeListener(escucha) }
            }
            CompositionLocalProvider(LocalDensity provides Density(densidad.density, densidad.fontScale * escala)) {
                ProyectoMartiniTheme {
                    MartiniNavigation(destinoAviso) { destinoAviso = null }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("destino_aviso", destinoAviso)
        super.onSaveInstanceState(outState)
    }
}
