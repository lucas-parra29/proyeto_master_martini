package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.viewmodel.RegisterViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.automirrored.filled.ArrowBack
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    registerViewModel: RegisterViewModel = viewModel()
) {

    val state by registerViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdePino)
                .padding(16.dp)
        ) {

            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Blanco
                )
            }

            Column {

                Text(
                    text = "Crear cuenta",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blanco
                )

                Text(
                    text = "Únete a Master Martini",
                    color = Blanco.copy(alpha = 0.8f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = "Tus datos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdePino
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.nombre,
                onValueChange = registerViewModel::onNombreChange,
                label = {
                    Text("Nombre completo")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null
                    )
                },
                isError = state.nombreError != null,
                supportingText = {
                    state.nombreError?.let {
                        Text(
                            text = it,
                            color = RojoCaoba
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.correo,
                onValueChange = registerViewModel::onCorreoChange,
                label = {
                    Text("Correo electrónico")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = state.correoError != null,
                supportingText = {
                    state.correoError?.let {
                        Text(
                            text = it,
                            color = RojoCaoba
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.telefono,
                onValueChange = registerViewModel::onTelefonoChange,
                label = {
                    Text("Teléfono")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                isError = state.telefonoError != null,
                supportingText = {
                    state.telefonoError?.let {
                        Text(
                            text = it,
                            color = RojoCaoba
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.tipoNegocio,
                onValueChange = registerViewModel::onTipoNegocioChange,
                label = {
                    Text("Tipo de negocio")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Store,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text("Ej: Pastelería")
                },
                isError = state.tipoNegocioError != null,
                supportingText = {
                    state.tipoNegocioError?.let {
                        Text(
                            text = it,
                            color = RojoCaoba
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = registerViewModel::onPasswordChange,
                label = {
                    Text("Contraseña")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.confirmarPassword,
                onValueChange =
                    registerViewModel::onConfirmarPasswordChange,
                label = {
                    Text("Confirmar contraseña")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                visualTransformation =
                    PasswordVisualTransformation(),
                isError = state.confirmarPasswordError != null,
                supportingText = {
                    state.confirmarPasswordError?.let {
                        Text(
                            text = it,
                            color = RojoCaoba
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    registerViewModel.registrar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BronceDorado
                )
            ) {

                Text(
                    text = "Crear cuenta",
                    fontWeight = FontWeight.Bold
                )
            }

            AnimatedVisibility(
                visible = state.registroExitoso,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {

                Column {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = VerdeBosque.copy(alpha = 0.12f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Registro exitoso",
                                tint = VerdeBosque
                            )

                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )

                            Text(
                                text = "¡Registro completado correctamente!",
                                color = VerdeBosque,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )

        }
    }
}

