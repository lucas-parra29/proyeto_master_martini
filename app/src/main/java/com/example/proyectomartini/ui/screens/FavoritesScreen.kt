package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.proyectomartini.ui.theme.*

@Composable
fun FavoritesScreen() {

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

        Spacer(modifier = Modifier.height(120.dp))

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
    }
}