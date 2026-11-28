package com.example.proyectomartini.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.R
import com.example.proyectomartini.data.local.FavoritoEntity
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.viewmodel.FavoritosViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

data class ContenidoDemo(
    val titulo: String,
    val tipo: String,
    val duracion: String,
    val nivel: String,
    val categoria: String,
    val recetaId: String? = null,
    val videoId: String? = null,
    val busquedaVideo: String? = null,
    val imagenUrl: String? = null,
    val consultaImagen: String? = null,
    val cursoId: String? = null
)

val categoriasMartini = listOf(
    "Todas",
    "Chocolatería",
    "Pastelería",
    "Heladería",
    "Bollería",
    "Masas de hoja",
    "Galletas",
    "Empanadas",
    "Food Service",
    "Horneo"
)

val fotosRecetas = mapOf(
    "mousse" to "chocolate hazelnut mousse cake",
    "brownies" to "chocolate brownies",
    "trufas" to "chocolate truffles",
    "torta-vainilla" to "vanilla layer cake",
    "cheesecake" to "berry cheesecake",
    "cupcakes" to "vanilla cupcakes",
    "helado-vainilla" to "vanilla ice cream",
    "helado-chocolate" to "chocolate ice cream",
    "sorbete-frutilla" to "strawberry sorbet",
    "rollos-canela" to "cinnamon rolls",
    "brioche" to "brioche bread",
    "bollos-leche" to "milk bread buns",
    "milhojas" to "mille feuille pastry",
    "palmeritas" to "palmier pastry",
    "tartaletas-hojaldre" to "apple puff pastry tart",
    "galletas-mantequilla" to "shortbread cookies",
    "galletas-chips" to "chocolate chip cookies",
    "galletas-avena" to "oatmeal raisin cookies",
    "empanada-pino" to "Chilean beef empanadas",
    "empanada-queso" to "cheese empanadas",
    "empanada-pollo" to "chicken empanadas",
    "pan-amasado" to "Chilean bread",
    "focaccia" to "rosemary focaccia",
    "pan-integral" to "whole wheat bread"
)

private val videosMartini = tutorialesMartini.map { tutorial ->
    val oficial = videosOficiales[tutorial.id]
    ContenidoDemo(tutorial.titulo, "VIDEO", oficial?.let { "${it.duracion} · Canal oficial" } ?: "Cápsula académica",
        "Básico", tutorial.categoria, videoId = tutorial.id)
}

val contenidosMartini: List<ContenidoDemo> =
    recetasMartini.map { receta ->
        ContenidoDemo(
            titulo = receta.titulo,
            tipo = "RECETA",
            duracion = "Preparación paso a paso",
            nivel = receta.nivel,
            categoria = receta.categoria,
            recetaId = receta.id,
            imagenUrl = receta.imagenUrl,
            consultaImagen = fotosRecetas[receta.id]
        )
    } + videosMartini + cursosMartini.map { curso ->
        ContenidoDemo(curso.titulo, "CURSO", "${curso.lecciones.size} lecciones",
            curso.nivel, curso.categoria, cursoId = curso.id)
    }

@Composable
fun ExploreScreen(
    favoritosViewModel: FavoritosViewModel = viewModel(),
    onVideoClick: (String, String) -> Unit = { _, _ -> },
    onRecetaClick: (String) -> Unit = {},
    tipoInicial: String = "Todas",
    onCursoClick: (String) -> Unit = {},
    onInicioClick: () -> Unit = {}
) {

    val favoritos by favoritosViewModel
        .titulosFavoritos.collectAsState()

    var busqueda by rememberSaveable(tipoInicial) {
        mutableStateOf("")
    }

    var categoriasSeleccionadas by remember(tipoInicial) {
        mutableStateOf(setOf<String>())
    }

    var nivel by remember(tipoInicial) { mutableStateOf("Todos") }
    val filtrados = contenidosMartini.filter { contenido ->
        val coincideBusqueda =
            contenido.titulo.contains(busqueda, true) ||
                    contenido.categoria.contains(busqueda, true) ||
                    contenido.tipo.contains(busqueda, true) ||
                    videosOficiales[contenido.videoId]?.titulo?.contains(busqueda, true) == true ||
                    recetasMartini.firstOrNull { it.id == contenido.recetaId }?.let { receta ->
                        receta.ingredientes.any { it.contains(busqueda, true) } ||
                            productoSugerido(receta.categoria).contains(busqueda, true)
                    } == true

        val coincideCategoria =
            categoriasSeleccionadas.isEmpty() ||
                    contenido.categoria in categoriasSeleccionadas

        val coincideTipo =
            tipoInicial == "Todas" ||
                    contenido.tipo == tipoInicial

        coincideBusqueda && coincideCategoria && coincideTipo && (nivel == "Todos" || nivel == contenido.nivel)
    }

    val tituloPantalla = when (tipoInicial) {
        "RECETA" -> "Recetas Master Martini"
        "VIDEO" -> "Videos técnicos"
        else -> "Explorar"
    }
    fun abrir(c: ContenidoDemo) {
        when {
            c.cursoId != null -> onCursoClick(c.cursoId)
            c.recetaId != null -> onRecetaClick(c.recetaId)
            c.videoId != null -> onVideoClick(c.videoId, c.titulo)
            c.busquedaVideo != null -> onVideoClick("buscar", c.busquedaVideo)
        }
    }
    val sugerencia = filtrados.firstOrNull().takeIf { busqueda.isBlank() && categoriasSeleccionadas.isEmpty() && nivel == "Todos" }
    var verFiltros by rememberSaveable { mutableStateOf(false) }
    LazyColumn(Modifier.fillMaxSize().background(Marfil), contentPadding = PaddingValues(horizontal = 13.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Box(Modifier.fillMaxWidth().height(65.dp)) {
                LogoEditorial(Modifier.align(Alignment.Center).size(132.dp, 65.dp))
                AccionCircular(Icons.Default.ArrowBack, "Inicio", onInicioClick,
                    Modifier.align(Alignment.CenterStart), fondo = Papel, tinta = VerdePino)
                AccionCircular(Icons.Default.Tune, "Filtrar por dificultad", { verFiltros = !verFiltros },
                    Modifier.align(Alignment.CenterEnd), fondo = Papel, tinta = VerdePino)
            }
            Text(tituloPantalla, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = VerdePino)
            Text(when (tipoInicial) {
                "RECETA" -> "Preparaciones paso a paso para tu negocio"
                "VIDEO" -> "Demostraciones, consejos y técnicas culinarias"
                else -> "Encuentra recetas, videos y cursos"
            }, color = GrisTexto, style = MaterialTheme.typography.bodyMedium)
        }
        item {
            OutlinedTextField(value = busqueda, onValueChange = { busqueda = it },
                placeholder = { Text("Busca por nombre o ingrediente") }, singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Papel, unfocusedContainerColor = Papel,
                    unfocusedBorderColor = LineaCrema))
        }
        item {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                categoriasMartini.forEach { c ->
                    PildoraEditorial(c, if (c == "Todas") categoriasSeleccionadas.isEmpty() else c in categoriasSeleccionadas, {
                        categoriasSeleccionadas = when {
                            c == "Todas" -> emptySet()
                            c in categoriasSeleccionadas -> categoriasSeleccionadas - c
                            else -> categoriasSeleccionadas + c
                        }
                    })
                }
            }
            if (verFiltros && tipoInicial != "VIDEO") {
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    listOf("Todos", "Básico", "Intermedio", "Avanzado").forEach {
                        PildoraEditorial(it, nivel == it, { nivel = it }, dorada = true)
                    }
                }
            } else if (verFiltros) {
                Text("Los videos de esta colección son de nivel básico.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        if (sugerencia != null && tipoInicial != "Todas") item {
            Surface(onClick = { abrir(sugerencia) }, shape = RoundedCornerShape(17.dp), color = ChocolateMartini) {
                Box(Modifier.fillMaxWidth().height(145.dp)) {
                    ImagenCatalogo(sugerencia, Modifier.matchParentSize())
                    Box(Modifier.matchParentSize().background(Brush.horizontalGradient(
                        .45f to Color(0xE909261A), 1f to Color.Transparent)))
                    Column(Modifier.align(Alignment.CenterStart).fillMaxWidth(.63f).padding(12.dp)) {
                        EtiquetaMartini(if (tipoInicial == "VIDEO") "Técnica destacada" else "Receta destacada")
                        Text(sugerencia.titulo, color = Blanco, fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(if (tipoInicial == "VIDEO") Icons.Default.PlayArrow else Icons.Default.ChevronRight, null, tint = DoradoMartini)
                            Text(if (tipoInicial == "VIDEO") "Ver demostración" else "Ver receta", color = Blanco)
                        }
                    }
                }
            }
        }
        if (filtrados.isEmpty()) item {
            SeccionMartini("Sin resultados") { Text("Prueba otra búsqueda o elige una categoría diferente.") }
        }
        items(filtrados, key = { "${it.tipo}:${it.titulo}" }) { c ->
            Surface(onClick = { abrir(c) }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), color = Papel, shadowElevation = 2.dp,
                border = BorderStroke(1.dp, LineaCrema)) {
                Row(Modifier.padding(7.dp), horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(width = 97.dp, height = 81.dp).clip(RoundedCornerShape(12.dp))) {
                        ImagenCatalogo(c, Modifier.matchParentSize())
                        if (c.tipo == "VIDEO") Icon(Icons.Default.PlayCircle, "Video",
                            Modifier.align(Alignment.Center).size(32.dp), tint = Blanco)
                    }
                    Column(Modifier.weight(1f)) {
                        Text(c.categoria, color = OroOscuro, style = MaterialTheme.typography.labelMedium)
                        Text(c.titulo, color = VerdePino, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("${c.duracion} · ${c.nivel}", color = GrisTexto, style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { favoritosViewModel.alternar(FavoritoEntity(c.titulo, c.tipo, c.duracion, c.nivel)) }) {
                        val guardado = c.titulo in favoritos
                        Icon(if (guardado) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            if (guardado) "Quitar favorito" else "Guardar favorito", tint = RojoCaoba)
                    }
                }
            }
        }
        item { Spacer(Modifier.height(9.dp)) }
    }
}

@Composable
private fun ImagenCatalogo(contenido: ContenidoDemo, modifier: Modifier) {
    val curso = cursosMartini.firstOrNull { it.id == contenido.cursoId }
    when {
        curso != null -> FotoCurso(curso, modifier)
        contenido.recetaId == "brownies" -> FotoEditorial("editorial_brownies", R.drawable.foto_brownies, modifier)
        contenido.videoId == "chocolate" -> FotoEditorial("editorial_chocolate", R.drawable.foto_ganache, modifier)
        else -> PortadaMartini(contenido.categoria, modifier, recetaId = contenido.recetaId)
    }
}
