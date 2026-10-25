package com.example.proyectomartini.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.notifications.ReminderScheduler
import com.example.proyectomartini.viewmodel.EditProfileViewModel
import java.io.File

private val VerdeMartini = Color(0xFF174D3D)
private val FondoMartini = Color(0xFFF7F8F5)

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {},
    editViewModel: EditProfileViewModel = viewModel()
) {
    val estado by editViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var mensajeFoto by remember {
        mutableStateOf<String?>(null)
    }

    var mensajeRecordatorio by remember {
        mutableStateOf<String?>(null)
    }

    fun guardarImagen(bitmap: Bitmap) {
        try {
            val archivo = File(
                context.filesDir,
                "foto_perfil.png"
            )

            archivo.outputStream().use { salida ->
                bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    salida
                )
            }

            editViewModel.cambiarFoto(archivo.absolutePath)
            mensajeFoto = "Foto actualizada correctamente"
        } catch (e: Exception) {
            Log.e(
                "EditProfileScreen",
                "Error al guardar imagen",
                e
            )
            mensajeFoto = "No se pudo guardar la imagen"
        }
    }

    val galeria = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { entrada ->
                    BitmapFactory.decodeStream(entrada)?.let { bitmap ->
                        guardarImagen(bitmap)
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "EditProfileScreen",
                    "Error al seleccionar imagen",
                    e
                )
                mensajeFoto = "No se pudo seleccionar la imagen"
            }
        }
    }

    val camara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            guardarImagen(it)
        }
    }

    val permisoNotificaciones = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permitido ->
        if (permitido) {
            ReminderScheduler.programar(
                context = context,
                titulo = "Master Martini",
                mensaje = "¡Revisa nuestros cursos y recetas!"
            )
            mensajeRecordatorio = "Recordatorio programado"
        } else {
            mensajeRecordatorio = "Permiso de notificaciones denegado"
        }
    }

    val foto = remember(estado.foto, mensajeFoto) {
        if (estado.foto.isNotEmpty()) {
            try {
                BitmapFactory.decodeFile(estado.foto)
                    ?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoMartini)
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeMartini)
                .padding(20.dp)
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "← Volver",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Editar perfil",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Actualiza tu información personal",
                fontSize = 14.sp,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (foto != null) {
                    Image(
                        bitmap = foto,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Surface(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        color = Color(0xFFE1E8E2)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sin foto",
                                color = VerdeMartini
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            galeria.launch("image/*")
                        }
                    ) {
                        Text("Galería")
                    }

                    OutlinedButton(
                        onClick = {
                            camara.launch(null)
                        }
                    ) {
                        Text("Cámara")
                    }
                }

                mensajeFoto?.let { mensaje ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mensaje,
                        color = VerdeMartini,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Información personal",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeMartini
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoPerfil(
                titulo = "Nombre completo",
                valor = estado.nombre,
                error = estado.nombreError,
                onValueChange = editViewModel::cambiarNombre
            )

            CampoPerfil(
                titulo = "Correo electrónico",
                valor = estado.correo,
                error = estado.correoError,
                tipoTeclado = KeyboardType.Email,
                onValueChange = editViewModel::cambiarCorreo
            )

            CampoPerfil(
                titulo = "Teléfono",
                valor = estado.telefono,
                error = estado.telefonoError,
                tipoTeclado = KeyboardType.Phone,
                onValueChange = editViewModel::cambiarTelefono
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Información de negocio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeMartini
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoPerfil(
                titulo = "Tipo de negocio",
                valor = estado.tipoNegocio,
                error = estado.negocioError,
                onValueChange = editViewModel::cambiarNegocio
            )

            CampoPerfil(
                titulo = "Intereses",
                valor = estado.intereses,
                error = estado.interesesError,
                onValueChange = editViewModel::cambiarIntereses
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    editViewModel.guardarPerfil()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeMartini
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Guardar cambios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (estado.guardadoExitoso) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Perfil actualizado correctamente",
                    color = VerdeMartini,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Recordatorios",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeMartini
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recibe recordatorios sobre cursos, recetas y contenidos de Master Martini.",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    if (
                        Build.VERSION.SDK_INT >= 33 &&
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permisoNotificaciones.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    } else {
                        ReminderScheduler.programar(
                            context = context,
                            titulo = "Master Martini",
                            mensaje = "¡Revisa nuestros cursos y recetas!"
                        )
                        mensajeRecordatorio = "Recordatorio programado"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Probar recordatorio")
            }

            mensajeRecordatorio?.let { mensaje ->
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mensaje,
                    color = VerdeMartini,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CampoPerfil(
    titulo: String,
    valor: String,
    error: String?,
    onValueChange: (String) -> Unit,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = {
            Text(titulo)
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado
        ),
        singleLine = true,
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        shape = RoundedCornerShape(12.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))
}
