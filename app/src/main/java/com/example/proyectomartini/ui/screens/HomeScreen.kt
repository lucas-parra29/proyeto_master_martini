package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.proyectomartini.R
import com.example.proyectomartini.ui.theme.*

data class HomeOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun HomeScreen(
    onExploreClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {}
) {

    val options = listOf(
        HomeOption(
            title = "Recetas",
            subtitle = "Ideas para cada ocasión",
            icon = Icons.Default.Menu,
            color = VerdePino
        ),
        HomeOption(
            title = "Videos",
            subtitle = "Técnicas y consejos",
            icon = Icons.Default.PlayArrow,
            color = BronceDorado
        ),
        HomeOption(
            title = "Cursos",
            subtitle = "Aprende con expertos",
            icon = Icons.Default.Star,
            color = VerdeBosque
        ),
        HomeOption(
            title = "Favoritos",
            subtitle = "Tu contenido guardado",
            icon = Icons.Default.Favorite,
            color = RojoCaoba
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.master_martini_logo
                ),
                contentDescription = "Logo Master Martini",
                modifier = Modifier
                    .width(170.dp)
                    .height(80.dp)
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = VerdePino
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "¡Hola, chef!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePino
        )

        Text(
            text = "Descubre, aprende y crea con el sabor de la tradición.",
            fontSize = 16.sp,
            color = GrisTexto
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Buscar recetas, técnicas o cursos...")
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
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Explora Master Martini",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePino
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(315.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(options) { option ->

                Card(
                    modifier = Modifier.height(145.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = option.color
                    ),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {

                        when (option.title) {

                            "Recetas",
                            "Videos",
                            "Cursos" -> onExploreClick()

                            "Favoritos" -> onFavoritesClick()
                        }
                    }
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Icon(
                            imageVector = option.icon,
                            contentDescription = option.title,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )

                        Column {

                            Text(
                                text = option.title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = option.subtitle,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Ingredientes que inspiran",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdePino
                )

                Text(
                    text = "Descubre técnicas y preparaciones para crear productos increíbles.",
                    color = GrisTexto,
                    fontSize = 14.sp
                )
            }
        }
    }
}