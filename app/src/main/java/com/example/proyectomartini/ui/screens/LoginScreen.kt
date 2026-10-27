package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.R
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {

    val state by loginViewModel.uiState.collectAsState()

    LaunchedEffect(state.loginExitoso) {

        if (state.loginExitoso) {

            onLoginSuccess()

            loginViewModel.consumirLoginExitoso()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(45.dp)
        )

        Image(
            painter = painterResource(
                id = R.drawable.master_martini_logo
            ),
            contentDescription = "Master Martini",
            modifier = Modifier
                .width(220.dp)
                .height(110.dp)
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Text(
            text = "Bienvenido",
            color = VerdePino,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Inicia sesión para continuar",
            color = GrisTexto,
            fontSize = 15.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        OutlinedTextField(
            value = state.correo,
            onValueChange = loginViewModel::onCorreoChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Correo electrónico")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            isError = state.correoError != null,
            supportingText = {
                state.correoError?.let {
                    Text(
                        text = it,
                        color = RojoCaoba
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = loginViewModel::onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Contraseña")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation =
                PasswordVisualTransformation(),
            isError = state.passwordError != null,
            supportingText = {
                state.passwordError?.let {
                    Text(
                        text = it,
                        color = RojoCaoba
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )

        if (state.loginError != null) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = state.loginError!!,
                color = RojoCaoba,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {
                loginViewModel.iniciarSesion()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdePino
            )
        ) {

            Text(
                text = "Iniciar sesión",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        TextButton(
            onClick = onRegisterClick
        ) {

            Text(
                text = "¿No tienes cuenta? Crear cuenta",
                color = BronceDorado,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Usuario de demostración",
            color = GrisTexto,
            fontSize = 12.sp
        )

        Text(
            text = "demo@martini.cl / martini123",
            color = VerdePino,
            fontSize = 12.sp
        )
    }
}

