package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectomartini.data.repository.UserProfile
import com.example.proyectomartini.ui.theme.DoradoMartini
import com.example.proyectomartini.utils.PerfilCompleto

@Composable
fun RecordatorioPerfil(perfil: UserProfile, abrir: (String) -> Unit) {
    val pendientes = PerfilCompleto.pendientes(perfil)
    if (pendientes.isEmpty()) return

    val porcentaje = PerfilCompleto.porcentaje(perfil)
    SeccionMartini("Completa tu perfil") {
        Text("$porcentaje % completado")
        LinearProgressIndicator(
            progress = { porcentaje / 100f },
            modifier = Modifier.fillMaxWidth(),
            color = DoradoMartini
        )
        Text("Por completar: " + pendientes.joinToString { it.etiqueta })
        Button(
            onClick = { abrir(pendientes.first().name) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
        ) { Text("Agregar ${pendientes.first().etiqueta.lowercase()}") }
    }
}
