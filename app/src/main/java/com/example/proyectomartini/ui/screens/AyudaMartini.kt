package com.example.proyectomartini.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyectomartini.ui.theme.NieveBrillante

internal const val ESCALA_LECTURA = "escala_lectura"
internal fun ajustesLectura(context: Context) = context.getSharedPreferences("ajustes_martini", Context.MODE_PRIVATE)

@Composable
internal fun AyudaMartini(volver: () -> Unit) {
    val contexto = LocalContext.current
    val ajustes = remember(contexto) { ajustesLectura(contexto) }
    var escala by remember(ajustes) { mutableFloatStateOf(ajustes.getFloat(ESCALA_LECTURA, 1f)) }
    var mensaje by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(NieveBrillante)) {
        CabeceraMartini("Centro de ayuda", "Master Martini Chile", volver)
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SeccionMartini("Tamaño del texto") {
                listOf("Normal" to 1f, "Grande" to 1.15f, "Muy grande" to 1.3f).forEach { (etiqueta, factor) ->
                    FilterChip(selected = escala == factor, onClick = {
                        escala = factor
                        ajustes.edit().putFloat(ESCALA_LECTURA, factor).apply()
                    }, label = { Text(etiqueta) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp))
                }
            }
            SeccionMartini("Cómo usar la aplicación") {
                Text("Para buscar, abre Explorar. Puedes escribir el nombre de una receta, un ingrediente o un curso.")
                Text("Para guardar una receta, un video o un curso, toca el corazón. Lo encontrarás en Favoritos.")
                Text("En Academia puedes completar lecciones. Al terminar un curso, vuelve a Perfil para encontrar su constancia.")
            }
            SeccionMartini("Contacto comercial") {
                Text("Para consultas sobre productos y distribución, usa el sitio oficial de Master Martini Chile.")
                Button(onClick = {
                    val enlace = Intent(Intent.ACTION_VIEW, Uri.parse("https://mastermartini.cl/contacto/"))
                    if (runCatching { contexto.startActivity(enlace) }.isFailure) mensaje = "Visita mastermartini.cl."
                }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Abrir contacto oficial") }
                if (mensaje.isNotBlank()) Text(mensaje)
            }
        }
    }
}
