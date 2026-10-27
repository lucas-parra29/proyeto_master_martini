
package com.example.proyectomartini.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    val consultaImagen: String? = null
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

private fun videoMartini(
    titulo: String,
    categoria: String,
    nivel: String,
    id: String? = null,
    busqueda: String? = null
) = ContenidoDemo(
    titulo = titulo,
    tipo = "VIDEO",
    duracion = "YouTube",
    nivel = nivel,
    categoria = categoria,
    videoId = id,
    busquedaVideo = busqueda,
    consultaImagen = if (id == null) busqueda else null
)


private val videosMartini = listOf(


    videoMartini(
        "Brownies de Chocolate - Paulina Cocina",
        "Chocolatería",
        "Básico",
        id = "VglM0C55oZ0"
    ),
    videoMartini(
        "Trufas de Chocolate - Gourmet",
        "Chocolatería",
        "Básico",
        id = "6PtMszToYSA"
    ),


    videoMartini(
        "Cheesecake de Frutos Rojos",
        "Pastelería",
        "Intermedio",
        id = "TZoKuKAdI0w"
    ),
    videoMartini(
        "Decoración de Tortas con Crema Chantilly",
        "Pastelería",
        "Intermedio",
        id = "X_lTNJIcKt8"
    ),


    videoMartini(
        "Helado Artesanal de Pistacho",
        "Heladería",
        "Básico",
        id = "9YKjoJxsvVk"
    ),
    videoMartini(
        "Helado Cremoso de Chocolate",
        "Heladería",
        "Básico",
        id = "Hzd8ZpV2BFc"
    ),


    videoMartini(
        "Elaboración de Panettone Tradicional",
        "Bollería",
        "Avanzado",
        id = "QSk3nrn1Cio"
    ),
    videoMartini(
        "Rollos de Canela - Cecilia Tupac",
        "Bollería",
        "Intermedio",
        id = "IRkzLPdvUfY"
    ),


    videoMartini(
        "Palmeritas de Hojaldre",
        "Masas de hoja",
        "Básico",
        id = "PO7ZR-zPBZY"
    ),
    videoMartini(
        "Masa de Hojaldre Casera",
        "Masas de hoja",
        "Avanzado",
        id = "eLk5of6I3sM"
    ),


    videoMartini(
        "Galletas con Chips de Chocolate",
        "Galletas",
        "Básico",
        id = "rn9tJAfC2J8"
    ),
    videoMartini(
        "Galletas de Mantequilla",
        "Galletas",
        "Básico",
        id = "3LR3puogkiA"
    ),


    videoMartini(
        "Empanadas Chilenas de Pino",
        "Empanadas",
        "Intermedio",
        id = "_vff5vplnQo"
    ),
    videoMartini(
        "Empanadas de Queso Tradicionales",
        "Empanadas",
        "Básico",
        id = "CowXXgEqEg0"
    ),


    videoMartini(
        "Pan Amasado Chileno",
        "Horneo",
        "Básico",
        id = "xJ_GwYzVc7w"
    ),
    videoMartini(
        "Focaccia de Romero",
        "Horneo",
        "Intermedio",
        id = "r1j_wpmwSEw"
    )
)


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
    } + videosMartini

private fun buscarImagenCommons(consulta: String): String? {
    return try {
        val termino = URLEncoder.encode(
            "filetype:bitmap $consulta",
            "UTF-8"
        )

        val api =
            "https://commons.wikimedia.org/w/api.php" +
                    "?action=query" +
                    "&generator=search" +
                    "&gsrsearch=$termino" +
                    "&gsrnamespace=6" +
                    "&gsrlimit=1" +
                    "&prop=imageinfo" +
                    "&iiprop=url" +
                    "&iiurlwidth=500" +
                    "&format=json"

        val conexion = URL(api).openConnection().apply {
            connectTimeout = 6000
            readTimeout = 6000
            setRequestProperty(
                "User-Agent",
                "MasterMartiniEducationalApp/1.0"
            )
        }

        val json = conexion.getInputStream()
            .bufferedReader().use { it.readText() }

        val paginas = JSONObject(json)
            .optJSONObject("query")
            ?.optJSONObject("pages")
            ?: return null

        val claves = paginas.keys()

        if (!claves.hasNext()) return null

        paginas.optJSONObject(claves.next())
            ?.optJSONArray("imageinfo")
            ?.optJSONObject(0)
            ?.optString("thumburl")
            ?.takeIf { it.startsWith("https://") }

    } catch (_: Exception) {
        null
    }
}

private fun descargarImagen(url: String): Bitmap? {
    return try {
        val conexion = URL(url).openConnection().apply {
            connectTimeout = 6000
            readTimeout = 6000
        }

        conexion.getInputStream().use {
            BitmapFactory.decodeStream(it)
        }
    } catch (_: Exception) {
        null
    }
}

@Composable
fun ImagenMartini(
    url: String?,
    modifier: Modifier = Modifier,
    consulta: String? = null
) {
    var bitmap by remember(url, consulta) {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(url, consulta) {
        bitmap = withContext(Dispatchers.IO) {
            val resultadoBusqueda = consulta?.let {
                buscarImagenCommons(it)
            }

            resultadoBusqueda?.let {
                descargarImagen(it)
            } ?: url?.let {
                descargarImagen(it)
            }
        }
    }

    Box(
        modifier = modifier.background(
            BronceDorado.copy(alpha = 0.18f)
        ),
        contentAlignment = Alignment.Center
    ) {
        val imagenActual = bitmap

        if (imagenActual != null) {
            Image(
                bitmap = imagenActual.asImageBitmap(),
                contentDescription = "Imagen del contenido",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = "MARTINI",
                color = VerdePino,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ExploreScreen(
    favoritosViewModel: FavoritosViewModel = viewModel(),
    onVideoClick: (String, String) -> Unit = { _, _ -> },
    onRecetaClick: (String) -> Unit = {},
    tipoInicial: String = "Todas"
) {
    val context = LocalContext.current

    val favoritos by favoritosViewModel
        .titulosFavoritos.collectAsState()

    var busqueda by remember(tipoInicial) {
        mutableStateOf("")
    }

    var filtro by remember(tipoInicial) {
        mutableStateOf("Todas")
    }

    val filtrados = contenidosMartini.filter { contenido ->
        val coincideBusqueda =
            contenido.titulo.contains(busqueda, true) ||
                    contenido.categoria.contains(busqueda, true) ||
                    contenido.tipo.contains(busqueda, true)

        val coincideCategoria =
            filtro == "Todas" ||
                    contenido.categoria == filtro

        val coincideTipo =
            tipoInicial == "Todas" ||
                    contenido.tipo == tipoInicial

        coincideBusqueda && coincideCategoria && coincideTipo
    }

    val tituloPantalla = when (tipoInicial) {
        "RECETA" -> "Recetas"
        "VIDEO" -> "Videos"
        else -> "Explorar"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdePino)
                .padding(20.dp)
        ) {
            Text(
                text = tituloPantalla,
                color = Blanco,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Inspiración para cada creación",
                color = Blanco.copy(alpha = 0.85f)
            )
        }

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            placeholder = {
                Text("Buscar contenido...")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriasMartini.forEach { categoria ->
                FilterChip(
                    selected = filtro == categoria,
                    onClick = { filtro = categoria },
                    label = { Text(categoria) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${filtrados.size} contenidos",
            color = GrisTexto,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            )
        ) {
            if (filtrados.isEmpty()) {
                item {
                    Text(
                        text = "No hay contenido para esta búsqueda.",
                        color = GrisTexto,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            items(
                items = filtrados,
                key = { it.titulo }
            ) { contenido ->

                val miniatura = contenido.videoId?.let {
                    "https://img.youtube.com/vi/$it/hqdefault.jpg"
                } ?: contenido.imagenUrl

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Blanco
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                when {
                                    contenido.tipo == "RECETA" -> {
                                        contenido.recetaId?.let {
                                            onRecetaClick(it)
                                        }
                                    }

                                    contenido.videoId != null -> {
                                        onVideoClick(
                                            contenido.videoId,
                                            contenido.titulo
                                        )
                                    }

                                    contenido.busquedaVideo != null -> {
                                        val url = Uri.parse(
                                            "https://www.youtube.com/results?search_query=" +
                                                    Uri.encode(contenido.busquedaVideo)
                                        )

                                        try {
                                            context.startActivity(
                                                Intent(
                                                    Intent.ACTION_VIEW,
                                                    url
                                                )
                                            )
                                        } catch (_: Exception) {
                                            Toast.makeText(
                                                context,
                                                "No se pudo abrir YouTube",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(92.dp)
                                .background(
                                    BronceDorado.copy(alpha = 0.18f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            ImagenMartini(
                                url = miniatura,
                                consulta = if (
                                    contenido.videoId != null
                                ) null else contenido.consultaImagen,
                                modifier = Modifier.fillMaxSize()
                            )

                            if (contenido.tipo == "VIDEO") {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = VerdePino.copy(
                                        alpha = 0.9f
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.PlayArrow,
                                        contentDescription = "Ver video",
                                        tint = Blanco,
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = contenido.tipo,
                                color = BronceDorado,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(3.dp))

                            Text(
                                text = contenido.titulo,
                                color = VerdePino,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(5.dp))

                            Text(
                                text = contenido.categoria,
                                color = VerdePino,
                                fontSize = 12.sp
                            )

                            Text(
                                text = "${contenido.duracion} • ${contenido.nivel}",
                                color = GrisTexto,
                                fontSize = 12.sp
                            )

                            Spacer(Modifier.height(5.dp))

                            Text(
                                text = when {
                                    contenido.tipo == "RECETA" ->
                                        "Ver receta completa"
                                    contenido.videoId != null ->
                                        "▶ Ver video"
                                    else ->
                                        "▶ Buscar tutorial en YouTube"
                                },
                                color = VerdePino,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val esFavorito =
                            contenido.titulo in favoritos

                        val colorCorazon by animateColorAsState(
                            targetValue = if (esFavorito)
                                RojoCaoba else GrisTexto,
                            label = "colorFavorito"
                        )

                        IconButton(
                            onClick = {
                                favoritosViewModel.alternar(
                                    FavoritoEntity(
                                        titulo = contenido.titulo,
                                        tipo = contenido.tipo,
                                        duracion = contenido.duracion,
                                        nivel = contenido.nivel
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = if (esFavorito)
                                    Icons.Default.Favorite
                                else
                                    Icons.Default.FavoriteBorder,
                                contentDescription = if (esFavorito)
                                    "Quitar de favoritos"
                                else
                                    "Agregar a favoritos",
                                tint = colorCorazon
                            )
                        }
                    }
                }
            }
        }
    }
}
