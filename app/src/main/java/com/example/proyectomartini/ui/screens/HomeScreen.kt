
package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onFavoritesClick: () -> Unit = {},
    onRecipesClick: () -> Unit = {},
    onVideosClick: () -> Unit = {},
    onCoursesClick: () -> Unit = {}
) {
    val options = listOf(
        HomeOption(
            "Recetas",
            "Ideas para cada ocasión",
            Icons.Default.Menu,
            VerdePino
        ),
        HomeOption(
            "Videos",
            "Técnicas y consejos",
            Icons.Default.PlayArrow,
            BronceDorado
        ),
        HomeOption(
            "Cursos",
            "Aprende con expertos",
            Icons.Default.Star,
            VerdeBosque
        ),
        HomeOption(
            "Favoritos",
            "Tu contenido guardado",
            Icons.Default.Favorite,
            RojoCaoba
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

            Spacer(Modifier.weight(1f))

            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = VerdePino
                )
            }
        }

        Spacer(Modifier.height(12.dp))

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

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            placeholder = {
                Text("Buscar recetas, técnicas o cursos...")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Explora Master Martini",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePino
        )

        Spacer(Modifier.height(14.dp))

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
                            "Recetas" -> onRecipesClick()
                            "Videos" -> onVideosClick()
                            "Cursos" -> onCoursesClick()
                            "Favoritos" -> onFavoritesClick()
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement =
                            Arrangement.SpaceBetween
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
                                color = Color.White.copy(
                                    alpha = 0.85f
                                ),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            onClick = onExploreClick
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
