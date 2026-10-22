package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.proyectomartini.ui.theme.*

@Composable
fun ProfileScreen(
    onRegisterClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
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

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(18.dp),
                        tint = VerdePino
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column {

                    Text(
                        text = "Valentina Rossi",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdePino
                    )

                    Text(
                        text = "Chef Pastelera",
                        color = GrisTexto
                    )

                    TextButton(
                        onClick = onRegisterClick
                    ) {
                        Text("Crear cuenta")
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                AssistChip(
                    onClick = {},
                    label = {
                        Text("Pastelería")
                    }
                )

                AssistChip(
                    onClick = {},
                    label = {
                        Text("Chocolatería")
                    }
                )

                AssistChip(
                    onClick = {},
                    label = {
                        Text("Heladería")
                    }
                )
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
        }
    }
}