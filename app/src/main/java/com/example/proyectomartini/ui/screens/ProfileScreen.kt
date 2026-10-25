
package com.example.proyectomartini.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.proyectomartini.data.repository.ProfileRepository
import com.example.proyectomartini.ui.theme.*

@Composable
fun ProfileScreen(
    onRegisterClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val repository = remember(context) {
        ProfileRepository(context)
    }

    var perfil by remember {
        mutableStateOf(repository.obtenerPerfil())
    }

    var actualizacion by remember {
        mutableIntStateOf(0)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                perfil = repository.obtenerPerfil()
                actualizacion++
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val foto = remember(perfil.foto, actualizacion) {
        if (perfil.foto.isNotEmpty()) {
            try {
                BitmapFactory.decodeFile(perfil.foto)
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
            .background(NieveBrillante)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdePino)
                .padding(24.dp)
        ) {
            Text(
                text = "Mi perfil",
                color = Blanco,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(85.dp),
                    shape = CircleShape,
                    color = GrisSuave
                ) {
                    if (foto != null) {
                        Image(
                            bitmap = foto,
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil sin fotografía",
                            modifier = Modifier.padding(18.dp),
                            tint = VerdePino
                        )
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = perfil.nombre.ifBlank {
                            "Usuario Master Martini"
                        },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdePino
                    )

                    Text(
                        text = perfil.tipoNegocio.ifBlank {
                            "Tipo de negocio"
                        },
                        color = GrisTexto
                    )

                    TextButton(
                        onClick = onRegisterClick
                    ) {
                        Text("Crear cuenta")
                    }

                    OutlinedButton(
                        onClick = onEditProfileClick
                    ) {
                        Text("Editar perfil")
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Mis intereses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdePino
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (perfil.intereses.isBlank()) {
                Text(
                    text = "Aún no has registrado intereses",
                    color = GrisTexto
                )
            } else {
                Text(
                    text = perfil.intereses,
                    color = VerdePino,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Información de contacto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdePino
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Blanco
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Correo electrónico",
                        fontWeight = FontWeight.Bold,
                        color = VerdePino
                    )

                    Text(
                        text = perfil.correo.ifBlank {
                            "Sin correo registrado"
                        },
                        color = GrisTexto
                    )

                    HorizontalDivider()

                    Text(
                        text = "Teléfono",
                        fontWeight = FontWeight.Bold,
                        color = VerdePino
                    )

                    Text(
                        text = perfil.telefono.ifBlank {
                            "Sin teléfono registrado"
                        },
                        color = GrisTexto
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Blanco
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "12",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdePino
                    )

                    Text(
                        text = "Recetas guardadas",
                        color = GrisTexto
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
