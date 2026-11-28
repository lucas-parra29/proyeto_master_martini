package com.example.proyectomartini.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.proyectomartini.notifications.CocinaAlarmas
import kotlinx.coroutines.delay

@Composable
internal fun TemporizadorCocina(id: String, titulo: String, recomendado: Int = 10) {
    val context = LocalContext.current
    val alarmas = remember(context) { CocinaAlarmas(context.applicationContext) }
    var estado by remember(id) { mutableStateOf(alarmas.leer(id)) }
    var restante by remember(id) { mutableLongStateOf(estado.restante()) }
    var entrada by rememberSaveable(id, recomendado) { mutableStateOf(recomendado.toString()) }
    val minutos = entrada.toIntOrNull()?.takeIf { it in 1..1440 }
    var mostrarPermiso by remember { mutableStateOf(false) }
    val permiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permitido ->
        mostrarPermiso = !permitido
    }
    LaunchedEffect(id, estado.fin, estado.pausa) {
        do {
            estado = alarmas.leer(id)
            restante = estado.restante()
            if (estado.fin > 0 && restante == 0L) {
                alarmas.finalizar(id, titulo, estado.fin)
                estado = alarmas.leer(id)
            }
            if (estado.fin > 0) delay(250)
        } while (estado.fin > 0)
    }

    val activo = estado.fin > 0 || estado.pausa > 0
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        if (activo) {
            val segundos = (restante + 999) / 1000
            Text("%d:%02d".format(segundos / 60, segundos % 60), style = MaterialTheme.typography.headlineMedium)
        } else {
            OutlinedTextField(
                value = entrada,
                onValueChange = { if (it.length <= 4 && it.all(Char::isDigit)) entrada = it },
                label = { Text("Minutos") },
                supportingText = { Text("Recomendado · ${formatoTiempoCocina(recomendado)}") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = minutos == null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (estado.terminado) Text("Tiempo cumplido. Revisa tu preparación.")
        BotonEditorial(
            if (estado.fin > 0) "Pausar" else if (estado.pausa > 0) "Continuar" else "Iniciar temporizador",
            {
                if (estado.fin > 0) alarmas.pausar(id, titulo) else {
                    alarmas.iniciar(id, titulo, estado.pausa.takeIf { it > 0 } ?: (minutos ?: return@BotonEditorial) * 60_000L)
                    if (estado.pausa == 0L && Build.VERSION.SDK_INT >= 33 &&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        mostrarPermiso = true
                    }
                }
                estado = alarmas.leer(id)
                restante = estado.restante()
            }, habilitado = activo || minutos != null
        )
        if (mostrarPermiso && activo) {
            TextButton(onClick = { permiso.launch(Manifest.permission.POST_NOTIFICATIONS) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Activar aviso al finalizar") }
        }
        if (activo || estado.terminado) OutlinedButton(
            onClick = {
                alarmas.reiniciar(id, titulo)
                estado = alarmas.leer(id)
                restante = 0
            },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
        ) { Text("Reiniciar") }
    }
}
