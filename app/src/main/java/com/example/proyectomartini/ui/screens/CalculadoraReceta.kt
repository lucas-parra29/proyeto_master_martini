package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyectomartini.utils.CostosReceta
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CalculadoraReceta(porciones: Int) {
    var insumos by rememberSaveable { mutableStateOf("") }
    var otros by rememberSaveable { mutableStateOf("") }
    var margen by rememberSaveable { mutableStateOf("30") }
    val formato = remember { NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CL")) }
    val resultado = CostosReceta.calcular(insumos, otros, margen, porciones)
    SeccionMartini("Calculadora de costos") {
        Text("Ingresa el costo del lote de $porciones porciones en pesos chilenos, sin separador de miles.")
        CampoCosto("Total de ingredientes", insumos) { insumos = it }
        CampoCosto("Energía, envases y otros", otros) { otros = it }
        CampoCosto("Margen de venta (%)", margen) { margen = it }
        if (resultado != null) {
            Text("Costo del lote  $ " + formato.format(resultado.lote))
            Text("Costo por porción  $ " + formato.format(resultado.porcion))
            Text("Precio sugerido por porción  $ " + formato.format(resultado.precio),
                style = MaterialTheme.typography.titleMedium)
        } else if (insumos.isNotBlank()) {
            Text("Usa costos positivos o cero y un margen inferior a 100 %.",
                color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun CampoCosto(titulo: String, valor: String, editar: (String) -> Unit) {
    OutlinedTextField(
        value = valor,
        onValueChange = { if (it.length <= 12) editar(it) },
        label = { Text(titulo) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
        singleLine = true
    )
}
