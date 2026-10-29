package com.example.proyectomartini.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.proyectomartini.notifications.ReminderReceiver
import com.example.proyectomartini.notifications.ReminderScheduler
import com.example.proyectomartini.ui.theme.GrisTexto
import com.example.proyectomartini.ui.theme.NieveBrillante
import com.example.proyectomartini.ui.theme.VerdePino

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit = {}
) {

    val context = LocalContext.current

    var mensajeEstado by remember {
        mutableStateOf<String?>(null)
    }

    var accionPendiente by remember {
        mutableStateOf<String?>(null)
    }

    fun enviarPruebaAhora() {

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        ).apply {

            putExtra(
                "titulo",
                "Master Martini"
            )

            putExtra(
                "mensaje",
                "¡Tienes nuevos contenidos para revisar!"
            )
        }

        context.sendBroadcast(intent)

        mensajeEstado =
            "Notificación de prueba enviada"
    }

    fun programarRecordatorio() {

        ReminderScheduler.programar(
            context = context,
            titulo = "Master Martini",
            mensaje = "¡Revisa nuestros cursos y recetas!"
        )

        mensajeEstado =
            "Recordatorio programado para aproximadamente 1 minuto"
    }

    val permisoNotificaciones =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permitido ->

            if (permitido) {

                when (accionPendiente) {

                    "prueba" ->
                        enviarPruebaAhora()

                    "recordatorio" ->
                        programarRecordatorio()
                }

            } else {

                mensajeEstado =
                    "Permiso de notificaciones denegado"
            }

            accionPendiente = null
        }

    fun ejecutarConPermiso(
        accion: String
    ) {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            accionPendiente = accion

            permisoNotificaciones.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )

        } else {

            if (accion == "prueba") {
                enviarPruebaAhora()
            } else {
                programarRecordatorio()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
            .padding(20.dp)
    ) {

        TextButton(
            onClick = onBackClick
        ) {

            Text(
                text = "← Volver a Inicio",
                color = VerdePino
            )
        }

        Spacer(
            Modifier.height(8.dp)
        )

        Icon(
            imageVector =
                Icons.Default.Notifications,
            contentDescription = null,
            tint = VerdePino,
            modifier =
                Modifier.align(
                    Alignment.CenterHorizontally
                )
        )

        Spacer(
            Modifier.height(12.dp)
        )

        Text(
            text = "Notificaciones",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePino,
            modifier =
                Modifier.align(
                    Alignment.CenterHorizontally
                )
        )

        Text(
            text =
                "Configura y prueba los recordatorios de Master Martini.",
            color = GrisTexto,
            modifier = Modifier
                .align(
                    Alignment.CenterHorizontally
                )
                .padding(top = 6.dp)
        )

        Spacer(
            Modifier.height(28.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(18.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Recordatorios",
                    fontSize = 20.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color = VerdePino
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Puedes enviar una notificación de prueba inmediatamente o programar un recordatorio.",
                    color = GrisTexto,
                    fontSize = 14.sp
                )

                Spacer(
                    Modifier.height(18.dp)
                )

                Button(
                    onClick = {
                        ejecutarConPermiso(
                            "prueba"
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    VerdePino
                            ),
                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                ) {

                    Text(
                        "Probar notificación ahora"
                    )
                }

                Spacer(
                    Modifier.height(10.dp)
                )

                OutlinedButton(
                    onClick = {
                        ejecutarConPermiso(
                            "recordatorio"
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                ) {

                    Text(
                        text =
                            "Programar recordatorio (1 min)",
                        color =
                            VerdePino
                    )
                }
            }
        }

        mensajeEstado?.let { mensaje ->

            Spacer(
                Modifier.height(18.dp)
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(
                        12.dp
                    ),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFE3F2E8)
                    )
            ) {

                Text(
                    text = mensaje,
                    color = VerdePino,
                    fontWeight =
                        FontWeight.Bold,
                    modifier =
                        Modifier.padding(
                            16.dp
                        )
                )
            }
        }
    }
}
