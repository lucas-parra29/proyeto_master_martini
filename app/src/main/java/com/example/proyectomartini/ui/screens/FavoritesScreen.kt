package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.*
import androidx.compose.material3.FilterChip
import com.example.proyectomartini.R
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.viewmodel.FavoritosViewModel

@Composable
fun FavoritesScreen(
    favoritosViewModel: FavoritosViewModel = viewModel(),
    onRecetaClick: (String) -> Unit = {},
    onVideoClick: (String, String) -> Unit = { _, _ -> },
    onCursoClick: (String) -> Unit = {}
) {

    val favoritos by favoritosViewModel.favoritos.collectAsState()

    var filtro by remember { mutableStateOf("Todos") }
    val visibles = favoritos.filter { filtro == "Todos" || it.tipo == filtro }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Marfil)
            .padding(horizontal = 13.dp)
    ) {

        LogoEditorial(Modifier.align(Alignment.CenterHorizontally).size(127.dp, 62.dp))
        Text("Favoritos", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium, color = VerdePino, fontWeight = FontWeight.ExtraBold)
        Text("Tu contenido guardado", color = GrisTexto)
        Spacer(Modifier.height(7.dp))
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            listOf("Todos" to "Todos","RECETA" to "Recetas","VIDEO" to "Videos","CURSO" to "Cursos").forEach { (id,titulo) -> PildoraEditorial(titulo, filtro == id, { filtro = id }) }
        }
        Text("${visibles.size} guardados",color=VerdePino,fontWeight=FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        if (visibles.isEmpty()) {

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
                items(visibles, key = { it.titulo }) { f ->

                    val contenido = contenidosMartini.find { it.titulo == f.titulo && it.tipo == f.tipo }
                    val curso = cursosMartini.find { it.titulo == f.titulo && f.tipo == "CURSO" }
                    Card(
                        onClick = {
                            curso?.id?.let(onCursoClick)
                            contenido?.recetaId?.let(onRecetaClick)
                            contenido?.videoId?.let { onVideoClick(it, f.titulo) }
                            contenido?.busquedaVideo?.let { onVideoClick("buscar", it) }
                            if(contenido == null && f.tipo == "VIDEO") onVideoClick("antiguo", f.titulo)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Papel),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            if (curso != null) FotoCurso(curso, Modifier.size(88.dp, 76.dp).clip(RoundedCornerShape(12.dp)))
                            else if (contenido != null) PortadaMartini(contenido.categoria, Modifier.size(88.dp, 76.dp), recetaId = contenido.recetaId)
                            if (curso != null || contenido != null) Spacer(Modifier.width(9.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = when (f.tipo) { "RECETA" -> "Receta"; "CURSO" -> "Curso"; else -> "Video" },
                                    color = BronceDorado,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = f.titulo,
                                    color = VerdePino,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${f.duracion} • ${f.nivel}",
                                    color = GrisTexto,
                                    fontSize = 16.sp
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
