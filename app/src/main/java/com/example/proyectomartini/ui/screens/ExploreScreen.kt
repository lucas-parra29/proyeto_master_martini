package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectomartini.ui.theme.*

data class ContenidoDemo(
    val titulo: String,
    val tipo: String,
    val duracion: String,
    val nivel: String
)

@Composable
fun ExploreScreen() {

    val contenidos = listOf(
        ContenidoDemo(
            "Torta Mousse de Chocolate y Avellanas",
            "RECETA",
            "60 min",
            "Intermedio"
        ),
        ContenidoDemo(
            "Técnica de Helado Artesanal de Pistacho",
            "VIDEO",
            "12 min",
            "Básico"
        ),
        ContenidoDemo(
            "Milhojas de Crema y Frutos Rojos",
            "RECETA",
            "45 min",
            "Intermedio"
        ),
        ContenidoDemo(
            "Elaboración de Panettone Tradicional",
            "VIDEO",
            "18 min",
            "Avanzado"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdePino)
                .padding(20.dp)
        ) {

            Column {

                Text(
                    text = "Explorar",
                    color = Blanco,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Inspiración para cada creación",
                    color = Blanco.copy(alpha = 0.85f)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar contenido...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                readOnly = true,
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FilterChip(
                    selected = true,
                    onClick = {},
                    label = {
                        Text("Todas")
                    }
                )

                FilterChip(
                    selected = false,
                    onClick = {},
                    label = {
                        Text("Pastelería")
                    }
                )

                FilterChip(
                    selected = false,
                    onClick = {},
                    label = {
                        Text("Heladería")
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(contenidos) { contenido ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Blanco
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(85.dp)
                                    .background(
                                        color = BronceDorado.copy(alpha = 0.18f),
                                        shape = RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = contenido.tipo,
                                    color = VerdePino,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(14.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = contenido.tipo,
                                    color = BronceDorado,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = contenido.titulo,
                                    color = VerdePino,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(
                                    modifier = Modifier.height(5.dp)
                                )

                                Text(
                                    text = "${contenido.duracion} • ${contenido.nivel}",
                                    color = GrisTexto,
                                    fontSize = 13.sp
                                )
                            }

                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorito",
                                    tint = RojoCaoba
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}