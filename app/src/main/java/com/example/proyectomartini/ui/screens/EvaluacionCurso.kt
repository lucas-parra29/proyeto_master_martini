package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

internal data class PreguntaCurso(val pregunta: String, val opciones: List<String>, val correcta: Int)

private fun preguntasCurso(categoria: String): List<PreguntaCurso> {
    val tecnica = when (categoria) {
        "Chocolatería" -> PreguntaCurso("¿Qué ayuda a obtener una emulsión de chocolate uniforme?",
            listOf("Agregar agua fría de golpe", "Integrar la crema y el chocolate gradualmente", "Hervir la mezcla hasta espesar"), 1)
        "Pastelería" -> PreguntaCurso("¿Cómo preparas un lote de vitrina repetible?",
            listOf("Pesando y anotando cantidades", "Cambiando proporciones en cada tanda", "Probando el montaje sin pesar"), 0)
        "Heladería" -> PreguntaCurso("¿Qué conviene registrar al probar una formulación?",
            listOf("Solo el color del envase", "Solo el tamaño de la pala", "La textura y el resultado tras conservarlo en frío"), 2)
        else -> PreguntaCurso("¿Qué haces si el primer lote tiene un rendimiento distinto?",
            listOf("Vendes sin ajustar", "Registras el rendimiento y pruebas un ajuste pequeño", "Cambias todos los ingredientes a la vez"), 1)
    }
    return listOf(tecnica, PreguntaCurso("¿Qué debes revisar antes de sustituir un insumo?",
        listOf("Solo el nombre de la marca", "Solo la apariencia", "La ficha técnica, los alérgenos y el uso recomendado"), 2))
}

@Composable
internal fun EvaluacionCurso(categoria: String, cancelar: () -> Unit, aprobar: (List<Int>) -> Unit) {
    val preguntas = remember(categoria) { preguntasCurso(categoria) }
    val respuestas = remember { mutableStateMapOf<Int, Int>() }
    var intentado by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = cancelar,
        title = { Text("Evaluación final") },
        text = {
            Column(Modifier.heightIn(max = 400.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                preguntas.forEachIndexed { indice, pregunta ->
                    Text(pregunta.pregunta, style = MaterialTheme.typography.titleSmall)
                    pregunta.opciones.forEachIndexed { opcion, texto ->
                        FilterChip(
                            selected = respuestas[indice] == opcion,
                            onClick = { respuestas[indice] = opcion; intentado = false },
                            label = { Text(texto) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        )
                    }
                }
                if (intentado) Text("Revisa tus respuestas y vuelve a intentar.", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            Button(
                enabled = respuestas.size == preguntas.size,
                onClick = {
                    if (preguntas.indices.all { preguntas[it].correcta == respuestas[it] }) {
                        aprobar(preguntas.indices.map { respuestas.getValue(it) })
                    }
                    else intentado = true
                },
                modifier = Modifier.heightIn(min = 48.dp)
            ) { Text("Revisar respuestas") }
        },
        dismissButton = { TextButton(onClick = cancelar, modifier = Modifier.heightIn(min = 48.dp)) { Text("Después") } }
    )
}
