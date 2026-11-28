package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectomartini.R
import com.example.proyectomartini.ui.theme.*

fun portadaCategoria(categoria: String): Int = when(categoria) {
    "Chocolatería" -> R.drawable.portada_chocolate
    "Heladería" -> R.drawable.portada_helado
    "Pastelería" -> R.drawable.portada_pastel
    "Galletas" -> R.drawable.portada_galletas
    "Empanadas" -> R.drawable.portada_empanadas
    "Masas de hoja", "Bollería" -> R.drawable.portada_bolleria
    else -> R.drawable.portada_pan
}

fun productoSugerido(categoria: String): String = when(categoria) {
    "Chocolatería" -> "Chocolate Ariba Dark 57% de Master Martini"
    "Pastelería" -> "Crema vegetal Orasì de Master Martini (para decoración)"
    "Heladería" -> "Caravella Ice Cacao de Master Martini (para cobertura)"
    "Masas de hoja", "Bollería", "Horneo", "Galletas", "Empanadas" -> "Margarina Master Export Horneo (según la formulación)"
    else -> "Insumos Master Martini según la preparación"
}

private val fotosCache = LruCache<String, Bitmap>(12)
private fun fotoCatalogo(categoria: String): String? = when(categoria) {
    "Chocolatería" -> "https://mastermartini.cl/wp-content/uploads/2023/06/Chocolates-Sucedaneos-1.jpg"
    "Pastelería" -> "https://mastermartini.cl/wp-content/uploads/2023/06/crema-bravo-pasteleria-1.jpg"
    "Heladería" -> "https://mastermartini.cl/wp-content/uploads/2023/05/3.jpg"
    "Masas de hoja", "Bollería" -> "https://mastermartini.cl/wp-content/uploads/2023/06/Margarinas-1.jpg"
    "Horneo" -> "https://mastermartini.cl/wp-content/uploads/2023/06/boton-panaderia.jpg"
    else -> null
}
internal data class FotoReceta(val url: String, val autor: String, val fuente: String)

internal val fotosAcreditadas = mapOf(
    "mousse" to FotoReceta("https://upload.wikimedia.org/wikipedia/commons/4/4d/Mousse_cake.jpg",
        "Heizco", "https://commons.wikimedia.org/wiki/File:Mousse_cake.jpg"),
    "brownies" to FotoReceta("https://thumb.wikimedia.org/wikipedia/commons/thumb/3/35/Chocolate_brownies.jpg/960px-Chocolate_brownies.jpg",
        "Gdr", "https://commons.wikimedia.org/wiki/File:Chocolate_brownies.jpg")
)

@Composable fun PortadaMartini(categoria: String, modifier: Modifier = Modifier, recurso: Int? = null,
    recetaId: String? = null) {
    if (recetaId != null) {
        val contexto = LocalContext.current
        val fotoLocal = remember(contexto, recetaId) {
            val nombre = if (recetaId == "brownies") "editorial_brownies" else "editorial_receta_" + recetaId.replace('-', '_')
            contexto.resources.getIdentifier(nombre, "drawable", contexto.packageName)
        }
        if (fotoLocal != 0) {
            Image(painterResource(fotoLocal), "Imagen ilustrativa de la receta",
                modifier.clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
            return
        }
    }
    val url = recetaId?.let { fotosAcreditadas[it]?.url } ?: if (recurso == null) fotoCatalogo(categoria) else null
    val foto = cargarFoto(url)
    val forma = modifier.clip(RoundedCornerShape(14.dp))
    if (foto != null) Image(foto.asImageBitmap(), "Fotografía de referencia: $categoria", forma, contentScale = ContentScale.Crop)
    else Image(painterResource(recurso ?: portadaCategoria(categoria)), "Imagen de referencia: $categoria", forma, contentScale = ContentScale.Crop)
}

@Composable
private fun cargarFoto(url: String?): Bitmap? {
    val bitmap by produceState<Bitmap?>(initialValue = null, key1 = url) {
        value = null
        if(url != null) value = withContext(Dispatchers.IO) {
            fotosCache.get(url) ?: try {
                val conn = URL(url).openConnection().apply {
                    connectTimeout = 4500
                    readTimeout = 4500
                    setRequestProperty("User-Agent", "MasterMartiniAcademia/1.5 (prototipo académico DSY1105)")
                }
                conn.getInputStream().use { BitmapFactory.decodeStream(it) }?.also { fotosCache.put(url,it) }
            } catch(e: java.io.IOException) { null } catch(e: SecurityException) { null }
        }
    }
    return bitmap
}

@Composable internal fun CatalogoInsumoFoto(categoria: String, modifier: Modifier) {
    if (categoria != "Chocolatería") {
        PortadaMartini(categoria, modifier)
        return
    }
    val url = "https://www.martiniprofessional.cl/img/containers/assets/products/" +
        "martini-cioccolato/cioccolato-standard-dischi-fondente-1.png/" +
        "e6727d7f71c4444d68953ea4fd3e8a1c/cioccolato-standard-dischi-fondente-1.png"
    val foto = cargarFoto(url)
    if (foto != null) {
        Image(foto.asImageBitmap(), "Ariba Discos de fusión 57% · Martini Professional",
            modifier, contentScale = ContentScale.Fit)
    }
}

@Composable fun CabeceraMartini(titulo: String, subtitulo: String, volver: (() -> Unit)? = null) {
    Column(Modifier.fillMaxWidth().background(CremaMartini).padding(horizontal = 18.dp, vertical = 8.dp)) {
        Box(Modifier.fillMaxWidth().heightIn(min = 60.dp)) {
            Image(painterResource(R.drawable.master_martini_logo), "Master Martini",
                Modifier.align(Alignment.Center).size(width = 120.dp, height = 60.dp))
            if (volver != null) {
                IconButton(onClick = volver, modifier = Modifier.align(Alignment.CenterStart).size(48.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = VerdePino)
                }
            }
        }
        Text(titulo, style = MaterialTheme.typography.headlineMedium, fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold, color = VerdePino)
        if (subtitulo.isNotBlank()) Text(subtitulo, style = MaterialTheme.typography.bodyMedium, color = GrisTexto)
        Spacer(Modifier.height(7.dp))
        Box(Modifier.width(56.dp).height(3.dp).background(DoradoMartini, RoundedCornerShape(4.dp)))
    }
}
@Composable fun EtiquetaMartini(texto: String) {
    Surface(shape=RoundedCornerShape(8.dp), color=CremaMartini) { Text(texto, Modifier.padding(horizontal=10.dp, vertical=6.dp), color=Color(0xFF624B04), style=MaterialTheme.typography.labelMedium) }
}
@Composable fun SeccionMartini(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=Papel), shape=RoundedCornerShape(18.dp), elevation=CardDefaults.cardElevation(defaultElevation=2.dp)) { Column(Modifier.padding(18.dp), verticalArrangement=Arrangement.spacedBy(10.dp)) { Text(titulo, style=MaterialTheme.typography.titleLarge, color=VerdePino); contenido() } }
}
