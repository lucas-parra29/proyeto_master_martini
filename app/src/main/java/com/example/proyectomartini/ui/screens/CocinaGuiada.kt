package com.example.proyectomartini.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.utils.CostosReceta

internal data class TiempoReceta(
    val preparacion: Int, val coccion: Int, val reposo: Int,
    val temporizadores: Map<Int, Int>, val temperatura: Int? = null
)

private fun tiempo(preparacion: Int, coccion: Int, reposo: Int, temperatura: Int?, vararg pasos: Pair<Int, Int>) =
    TiempoReceta(preparacion, coccion, reposo, pasos.toMap(), temperatura)

internal val tiemposRecetas = mapOf(
    "mousse" to tiempo(30, 0, 255, null, 2 to 15, 6 to 240),
    "brownies" to tiempo(20, 25, 30, 180, 6 to 25, 7 to 30),
    "trufas" to tiempo(25, 5, 65, null, 3 to 2, 4 to 60),
    "torta-vainilla" to tiempo(30, 40, 45, 180, 4 to 40, 5 to 45),
    "cheesecake" to tiempo(25, 60, 300, 160, 5 to 60, 6 to 300),
    "cupcakes" to tiempo(25, 22, 25, 180, 5 to 22, 6 to 25),
    "helado-vainilla" to tiempo(15, 0, 365, null, 5 to 360, 6 to 5),
    "helado-chocolate" to tiempo(15, 0, 360, null, 5 to 360),
    "sorbete-frutilla" to tiempo(15, 5, 240, null, 1 to 5, 5 to 40),
    "rollos-canela" to tiempo(35, 25, 120, 180, 3 to 75, 5 to 45, 6 to 25),
    "brioche" to tiempo(35, 35, 360, 180, 4 to 270, 5 to 90, 6 to 35),
    "bollos-leche" to tiempo(30, 20, 105, 180, 3 to 60, 5 to 45, 6 to 20),
    "milhojas" to tiempo(35, 25, 65, 200, 2 to 20, 5 to 45, 7 to 20),
    "palmeritas" to tiempo(15, 18, 0, 200, 6 to 18),
    "tartaletas-hojaldre" to tiempo(20, 20, 0, 200, 6 to 20),
    "galletas-mantequilla" to tiempo(25, 15, 30, 180, 4 to 30, 6 to 15),
    "galletas-chips" to tiempo(20, 13, 15, 180, 6 to 13, 7 to 15),
    "galletas-avena" to tiempo(20, 16, 15, 180, 5 to 16, 6 to 15),
    "empanada-pino" to tiempo(45, 55, 60, 200, 2 to 20, 3 to 60, 8 to 35),
    "empanada-queso" to tiempo(30, 25, 20, 200, 2 to 20, 6 to 25),
    "empanada-pollo" to tiempo(30, 50, 20, 200, 3 to 20, 6 to 25),
    "pan-amasado" to tiempo(25, 25, 70, 200, 3 to 55, 6 to 15, 7 to 25),
    "focaccia" to tiempo(20, 25, 105, 220, 2 to 60, 4 to 45, 6 to 25),
    "pan-integral" to tiempo(25, 40, 105, 200, 3 to 60, 5 to 45, 6 to 40)
)

internal fun formatoTiempoCocina(minutos: Int): String {
    val h = minutos / 60
    val m = minutos % 60
    return when {
        h == 0 -> "$m min"
        m == 0 -> "$h h"
        else -> "$h h $m min"
    }
}

internal fun textoPorciones(cantidad: Int) = if (cantidad == 1) "1 porción" else "$cantidad porciones"

@Composable
internal fun TiemposDeReceta(tiempo: TiempoReceta) {
    SeccionMartini("Tiempos aproximados") {
        Text("Preparación · ${formatoTiempoCocina(tiempo.preparacion)}")
        Text("Cocción · ${if (tiempo.coccion == 0) "Sin cocción" else formatoTiempoCocina(tiempo.coccion)}")
        if (tiempo.reposo > 0) Text("Reposo y enfriado · ${formatoTiempoCocina(tiempo.reposo)}")
        Text("Total · ${formatoTiempoCocina(tiempo.preparacion + tiempo.coccion + tiempo.reposo)}",
            style = MaterialTheme.typography.titleMedium, color = VerdePino)
    }
}

private tailrec fun Context.actividad(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.actividad()
    else -> null
}

@Composable
internal fun CocinaGuiada(receta: RecetaMartini, base: Int, porciones: Int, tiempo: TiempoReceta?,
    paso: Int, cambiarPaso: (Int) -> Unit, listos: Set<Int>, marcar: (Int) -> Unit, volver: () -> Unit) {
    val window = LocalContext.current.actividad()?.window
    var encendida by rememberSaveable { mutableStateOf(false) }
    val scroll = rememberScrollState()
    LaunchedEffect(paso) { scroll.scrollTo(0) }
    DisposableEffect(encendida, window) {
        val flag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        val agregar = encendida && window != null && window.attributes.flags and flag == 0
        if (agregar) window?.addFlags(flag)
        onDispose { if (agregar) window?.clearFlags(flag) }
    }

    Column(Modifier.fillMaxSize().background(Marfil).imePadding()) {
        CabeceraMartini("Modo cocina", receta.titulo, volver)
        Column(Modifier.weight(1f).verticalScroll(scroll).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (paso == receta.pasos.size) {
                Icon(Icons.Default.CheckCircle, null, Modifier.size(68.dp), tint = VerdePino)
                Text("¡Preparación completada!", style = MaterialTheme.typography.headlineLarge, color = VerdePino)
                Text(receta.titulo, style = MaterialTheme.typography.titleLarge)
                Text("${listos.size} de ${receta.pasos.size} pasos completados")
                BotonEditorial("Volver a la receta", volver)
                OutlinedButton(onClick = { cambiarPaso(paso - 1) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text("Revisar último paso")
                }
                return@Column
            }

            val instruccion = receta.pasos[paso]
            Text("Paso ${paso + 1} de ${receta.pasos.size}", style = MaterialTheme.typography.titleMedium)
            AvanceEditorial(listos.size.toFloat() / receta.pasos.size, Modifier.fillMaxWidth())
            Text("${listos.size} pasos completados", style = MaterialTheme.typography.bodyMedium)
            SuperficieEditorial {
                Text(instruccion, Modifier.padding(17.dp), style = MaterialTheme.typography.headlineSmall, color = VerdePino)
            }
            if (tiempo?.temperatura != null && (instruccion.contains("horno", true) || instruccion.contains("hornea", true))) {
                Text("Temperatura · ${tiempo.temperatura} °C", style = MaterialTheme.typography.titleMedium)
            }
            tiempo?.temporizadores?.get(paso + 1)?.let { minutos ->
                SeccionMartini("Tiempo recomendado · ${formatoTiempoCocina(minutos)}") {
                    TemporizadorCocina("${receta.id}/$paso", "${receta.titulo} · Paso ${paso + 1}", minutos)
                }
            }
            val delPaso = ingredientesDelPaso(receta, paso)
            if (delPaso.isNotEmpty()) SeccionMartini("Para este paso") {
                delPaso.forEach { Text(CostosReceta.escalar(it, base, porciones)) }
            }
            var ingredientes by rememberSaveable(paso) { mutableStateOf(false) }
            OutlinedButton(onClick = { ingredientes = !ingredientes },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text(if (ingredientes) "Ocultar ingredientes" else "Ver cantidades para ${textoPorciones(porciones)}")
            }
            if (ingredientes) SeccionMartini("Ingredientes de la receta") {
                receta.ingredientes.forEach { Text(CostosReceta.escalar(it, base, porciones)) }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Mantener la pantalla encendida", Modifier.weight(1f))
                Switch(checked = encendida, onCheckedChange = { encendida = it })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = paso in listos, onCheckedChange = { marcar(paso) })
                Text("Paso terminado")
            }
        }
        if (paso < receta.pasos.size) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                if (paso == receta.pasos.lastIndex && listos.size < receta.pasos.size) {
                    Text("Marca los pasos pendientes para terminar.", style = MaterialTheme.typography.bodyMedium)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { cambiarPaso(paso - 1) }, enabled = paso > 0,
                        modifier = Modifier.weight(1f).heightIn(min = 50.dp)) { Text("Anterior") }
                    Button(onClick = { cambiarPaso(paso + 1) },
                        enabled = paso < receta.pasos.lastIndex || listos.size == receta.pasos.size,
                        modifier = Modifier.weight(1f).heightIn(min = 50.dp)) {
                        Text(if (paso == receta.pasos.lastIndex) "Terminar" else "Siguiente")
                    }
                }
            }
        }
    }
}
