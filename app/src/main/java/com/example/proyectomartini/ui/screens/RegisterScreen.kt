package com.example.proyectomartini.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import com.example.proyectomartini.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.data.remote.PerfilRemoto
import com.example.proyectomartini.ui.theme.NieveBrillante
import com.example.proyectomartini.utils.PerfilCompleto
import com.example.proyectomartini.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    registerViewModel: RegisterViewModel = viewModel()
) {
    val state by registerViewModel.uiState.collectAsState()
    val perfil = state.perfil
    val scroll = rememberScrollState()
    val volver = {
        if (!state.cargando) {
            if (state.paso > 0 && !state.terminado) registerViewModel.volver() else onBackClick()
        }
    }
    BackHandler(onBack = volver)
    LaunchedEffect(state.paso, state.terminado) { scroll.scrollTo(0) }
    fun editar(cambio: (PerfilRemoto) -> PerfilRemoto) =
        registerViewModel.modificar { it.copy(perfil = cambio(it.perfil)) }

    Box(Modifier.fillMaxSize().background(Marfil).imePadding(), contentAlignment = Alignment.TopCenter) {
    Column(
        Modifier.widthIn(max = 510.dp).fillMaxWidth().fillMaxHeight().verticalScroll(scroll).padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            PortadaAcceso(compacta = true)
            AccionCircular(Icons.Default.ArrowBack, "Volver", volver, Modifier.align(Alignment.CenterStart),
                fondo = Papel, tinta = VerdePino)
        }
        Text("Crear una cuenta", Modifier.fillMaxWidth(), style = MaterialTheme.typography.headlineMedium,
            color = VerdePino, textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold)
        Text("Únete a la comunidad Master Martini.", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        if (state.terminado) {
            Text("Tu cuenta está lista. Inicia sesión para acceder a tus recomendaciones.")
            Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                Text("Ir a iniciar sesión")
            }
            return@Column
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("Tus datos", "Intereses").forEachIndexed { indice, nombre ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Box(Modifier.size(38.dp).background(if (indice <= state.paso) VerdePino else LineaCrema, CircleShape),
                        contentAlignment = Alignment.Center) {
                        if (indice < state.paso) Icon(Icons.Default.Check, "Etapa completada", tint = Blanco)
                        else Text("${indice + 1}", color = if (indice == state.paso) Blanco else VerdePino, fontWeight = FontWeight.Bold)
                    }
                    Text(nombre, style = MaterialTheme.typography.labelLarge, fontWeight = if (indice == state.paso) FontWeight.Bold else FontWeight.Medium)
                }
            }
        }
        when (state.paso) {
            0 -> {
                Text("Tus datos", style = MaterialTheme.typography.titleLarge)
                CampoRegistro("Nombre completo", perfil.nombre, state.errores["nombre"], limite = 200) { value -> editar { it.copy(nombre = value) } }
                CampoRegistro("Correo electrónico", perfil.correo, state.errores["correo"], KeyboardType.Email, 254) { value -> editar { it.copy(correo = value) } }
                CampoRegistro("Contraseña", state.password, state.errores["password"], KeyboardType.Password, 72) { value -> registerViewModel.modificar { it.copy(password = value) } }
                CampoRegistro("Confirmar contraseña", state.confirmacion, state.errores["confirmacion"], KeyboardType.Password, 72) { value -> registerViewModel.modificar { it.copy(confirmacion = value) } }
            }
            1 -> {
                Text("¿Qué te interesa aprender?", style = MaterialTheme.typography.titleLarge)
                Text("Elige los temas que te interesan. También puedes agregarlos luego desde tu perfil.")
                val seleccion = PerfilCompleto.intereses(perfil.intereses)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    PerfilCompleto.catalogo.chunked(if (LocalDensity.current.fontScale > 1.15f) 1 else 2).forEach { fila ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            fila.forEach { opcion ->
                                FilterChip(
                                    selected = opcion in seleccion,
                                    onClick = {
                                        val nuevos = if (opcion in seleccion) seleccion - opcion else seleccion + opcion
                                        editar { it.copy(intereses = nuevos.joinToString(", ")) }
                                    },
                                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = VerdePino, selectedLabelColor = Blanco,
                                        selectedLeadingIconColor = Blanco),
                                    leadingIcon = {
                                        val icono = when {
                                            opcion in seleccion -> Icons.Default.Check
                                            opcion == "Heladería" -> Icons.Default.Icecream
                                            opcion == "Pastelería" -> Icons.Default.Cake
                                            opcion in setOf("Panadería", "Bollería", "Horneo") -> Icons.Default.BakeryDining
                                            else -> Icons.Default.Restaurant
                                        }
                                        Icon(icono, null, Modifier.size(18.dp))
                                    },
                                    label = { Text(opcion) }
                                )
                            }
                            if (fila.size == 1 && LocalDensity.current.fontScale <= 1.15f) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        state.mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = registerViewModel::continuar,
            enabled = !state.cargando,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) {
            Text(if (state.cargando) "Creando cuenta…" else if (state.paso == 1) "Crear cuenta" else "Continuar")
        }
        if (state.paso == 1) TextButton(onClick = registerViewModel::omitirIntereses,
            enabled = !state.cargando, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text("Omitir intereses y crear cuenta")
        }
    }
    }
}

@Composable
private fun CampoRegistro(
    titulo: String,
    valor: String,
    error: String? = null,
    teclado: KeyboardType = KeyboardType.Text,
    limite: Int = 200,
    editar: (String) -> Unit
) {
    val icono = when (teclado) {
        KeyboardType.Email -> Icons.Default.Email
        KeyboardType.Password -> Icons.Default.Lock
        else -> Icons.Default.Person
    }
    CampoAcceso(titulo, valor, error, icono, teclado) { if (it.length <= limite) editar(it) }
}
