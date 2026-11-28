package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.R
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit, onRecoveryClick: () -> Unit = {},
    loginViewModel: LoginViewModel = viewModel()) {
    val state by loginViewModel.uiState.collectAsState()
    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) {
            onLoginSuccess()
            loginViewModel.consumirLoginExitoso()
        }
    }

    Column(Modifier.fillMaxSize().background(Marfil).imePadding().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
        PortadaAcceso()
        Column(Modifier.widthIn(max = 510.dp).fillMaxWidth().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text("Bienvenido a\nMaster Martini", color = VerdePino, textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
            Text("Recetas, videos y cursos para hacer crecer tu negocio.", textAlign = TextAlign.Center,
                color = GrisTexto, modifier = Modifier.padding(bottom = 7.dp))
            CampoAcceso("Correo electrónico", state.correo, state.correoError, Icons.Default.Email,
                KeyboardType.Email, loginViewModel::onCorreoChange)
            CampoAcceso("Contraseña", state.password, state.passwordError, Icons.Default.Lock,
                KeyboardType.Password, loginViewModel::onPasswordChange)
            TextButton(onClick = onRecoveryClick, modifier = Modifier.align(Alignment.End).heightIn(min = 48.dp)) {
                Text("¿Olvidaste tu contraseña?")
            }
            state.loginError?.let { Text(it, color = RojoCaoba) }
            BotonEditorial(if (state.cargando) "Iniciando sesión…" else "Iniciar sesión",
                loginViewModel::iniciarSesion, icono = Icons.Default.ArrowForward, habilitado = !state.cargando)
            OutlinedButton(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth().heightIn(min = 49.dp),
                border = BorderStroke(1.dp, VerdePino.copy(alpha = .45f))) {
                Icon(Icons.Default.PersonAdd, null, Modifier.size(20.dp))
                Spacer(Modifier.width(7.dp)); Text("Crear una cuenta", fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(20.dp))
        BoxWithConstraints(Modifier.widthIn(max = 620.dp).fillMaxWidth()) {
            val alto = (maxWidth * .30f).coerceIn(100.dp, 168.dp)
            Box(Modifier.fillMaxWidth().height(alto).clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))) {
                FotoEditorial("editorial_saludo", R.drawable.portada_chocolate, Modifier.matchParentSize())
                Box(Modifier.matchParentSize().background(
                    Brush.verticalGradient(listOf(Marfil, Marfil.copy(alpha = .12f), Color.Transparent))
                ))
            }
        }
    }
}

@Composable
internal fun PortadaAcceso(compacta: Boolean = false) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val logoAncho = (maxWidth * .46f).coerceIn(112.dp, if (compacta) 143.dp else 178.dp)
        val ala = (maxWidth * .23f).coerceAtMost(120.dp)
        Box(Modifier.fillMaxWidth().height(logoAncho * .74f)) {
            Box(Modifier.align(Alignment.TopStart).size(ala, ala * .56f)
                .background(VerdePino, RoundedCornerShape(bottomEnd = 70.dp)))
            Box(Modifier.align(Alignment.TopEnd).size(ala * .83f, ala * .43f)
                .background(DoradoMartini, RoundedCornerShape(bottomStart = 60.dp)))
            LogoEditorial(Modifier.align(Alignment.Center).size(logoAncho, logoAncho * .64f))
        }
    }
}

@Composable
internal fun CampoAcceso(titulo: String, valor: String, error: String?, icono: ImageVector,
    teclado: KeyboardType, editar: (String) -> Unit) {
    var visible by rememberSaveable(titulo) { mutableStateOf(false) }
    val clave = teclado == KeyboardType.Password
    OutlinedTextField(value = valor, onValueChange = editar, modifier = Modifier.fillMaxWidth(),
        label = { Text(titulo) }, leadingIcon = { Icon(icono, null, tint = VerdePino) },
        trailingIcon = if (clave) {{
            IconButton(onClick = { visible = !visible }) {
                Icon(if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    if (visible) "Ocultar contraseña" else "Mostrar contraseña", tint = GrisTexto)
            }
        }} else null,
        supportingText = error?.let { { Text(it) } }, isError = error != null, singleLine = true,
        shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = teclado),
        visualTransformation = if (clave && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = LineaCrema,
            focusedContainerColor = Papel, unfocusedContainerColor = Papel))
}
