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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.viewmodel.FavoritosViewModel

@Composable
fun FavoritesScreen(favoritosViewModel: FavoritosViewModel = viewModel()) {

    val favoritos by favoritosViewModel.favoritos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
            .padding(24.dp)
    ) {

        Text(
            text = "Mis favoritos",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePino
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (favoritos.isEmpty()) {

            Spacer(modifier = Modifier.height(100.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = RojoCaoba,
                    modifier = Modifier.size(70.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tus contenidos favoritos aparecerán aquí",
                    color = VerdePino
                )
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoritos, key = { it.titulo }) { f ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Blanco),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = f.tipo,
                                    color = BronceDorado,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = f.titulo,
                                    color = VerdePino,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${f.duracion} • ${f.nivel}",
                                    color = GrisTexto,
                                    fontSize = 13.sp
                                )
                            }

                            IconButton(
                                onClick = { favoritosViewModel.quitar(f.titulo) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Quitar de favoritos",
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