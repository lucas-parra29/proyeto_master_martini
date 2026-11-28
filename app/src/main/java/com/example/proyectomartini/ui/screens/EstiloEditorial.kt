package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectomartini.R
import com.example.proyectomartini.ui.theme.*

internal val Marfil = Color(0xFFFAF5E9)
internal val Papel = Color(0xFFFFFBF3)
internal val LineaCrema = Color(0xFFE6DCC7)
internal val OroOscuro = Color(0xFF9B6903)
internal val OroClaro = Color(0xFFFFE9A9)

internal val GorroChef = ImageVector.Builder("Gorro de chef", 24.dp, 24.dp, 24f, 24f).apply {
    path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f,
        strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
        moveTo(6f, 20f); lineTo(6f, 13.4f)
        curveTo(2f, 12.8f, 1.9f, 7f, 6.6f, 6.2f)
        curveTo(7.5f, 1.9f, 16.3f, 1.9f, 17.4f, 6.2f)
        curveTo(22.1f, 7f, 22f, 12.8f, 18f, 13.4f)
        lineTo(18f, 20f); close()
        moveTo(6f, 16.8f); lineTo(18f, 16.8f)
    }
}.build()

@Composable internal fun FotoEditorial(nombre: String, respaldo: Int, modifier: Modifier = Modifier, descripcion: String? = null) {
    val contexto = LocalContext.current
    val recurso = contexto.resources.getIdentifier(nombre, "drawable", contexto.packageName).takeIf { it != 0 } ?: respaldo
    Image(painterResource(recurso), descripcion, modifier, contentScale = ContentScale.Crop)
}

@Composable internal fun LogoEditorial(modifier: Modifier = Modifier) {
    Image(painterResource(R.drawable.master_martini_logo), "Master Martini Chile", modifier, contentScale = ContentScale.Fit)
}

@Composable internal fun SuperficieEditorial(
    modifier: Modifier = Modifier,
    fondo: Color = Papel,
    contenido: @Composable () -> Unit
) {
    Surface(modifier, RoundedCornerShape(17.dp), color = fondo,
        border = BorderStroke(1.dp, LineaCrema), shadowElevation = 2.dp, content = contenido)
}

@Composable internal fun AccionCircular(icono: ImageVector, etiqueta: String, onClick: () -> Unit,
    modifier: Modifier = Modifier, fondo: Color = Color(0xFF202018).copy(alpha = .67f), tinta: Color = Blanco) {
    IconButton(onClick, modifier.size(48.dp)) {
        Box(Modifier.size(37.dp).background(fondo, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icono, etiqueta, Modifier.size(21.dp), tint = tinta)
        }
    }
}

@Composable internal fun BotonEditorial(texto: String, accion: () -> Unit, modifier: Modifier = Modifier,
    icono: ImageVector? = null, habilitado: Boolean = true, dorado: Boolean = false) {
    Button(onClick = accion, enabled = habilitado,
        modifier = modifier.fillMaxWidth().heightIn(min = 49.dp), shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = if (dorado) OroClaro else VerdePino,
            contentColor = if (dorado) VerdePino else Blanco)) {
        icono?.let { Icon(it, null, Modifier.size(20.dp)); Spacer(Modifier.width(8.dp)) }
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

@Composable internal fun PildoraEditorial(texto: String, activa: Boolean, accion: () -> Unit,
    modifier: Modifier = Modifier, dorada: Boolean = false) {
    FilterChip(selected = activa, onClick = accion, label = { Text(texto, fontWeight = FontWeight.SemiBold) },
        modifier = modifier.heightIn(min = 48.dp), shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Papel, labelColor = VerdePino,
            selectedContainerColor = if (dorada) OroClaro else VerdePino,
            selectedLabelColor = if (dorada) VerdePino else Blanco
        ))
}

@Composable internal fun IndicadorEditorial(icono: ImageVector, etiqueta: String, modifier: Modifier = Modifier,
    tinta: Color = OroOscuro) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Icon(icono, null, Modifier.size(17.dp), tint = tinta)
        Text(etiqueta, style = MaterialTheme.typography.labelMedium, color = VerdePino)
    }
}

@Composable internal fun AvanceEditorial(progreso: Float, modifier: Modifier = Modifier) {
    val valor = progreso.coerceIn(0f, 1f)
    Box(modifier.clip(CircleShape).background(LineaCrema).height(7.dp)) {
        Box(Modifier.fillMaxWidth(valor).fillMaxHeight().background(
            Brush.horizontalGradient(listOf(Color(0xFF419535), VerdePino)), CircleShape))
    }
}
