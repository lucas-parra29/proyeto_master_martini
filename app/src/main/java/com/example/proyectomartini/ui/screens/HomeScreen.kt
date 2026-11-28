package com.example.proyectomartini.ui.screens
import androidx.compose.foundation.Image
import android.content.Intent
import android.net.Uri
import com.example.proyectomartini.utils.PerfilCompleto
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectomartini.R
import com.example.proyectomartini.data.repository.AuthRepository
import com.example.proyectomartini.data.repository.AprendizajeRepository
import com.example.proyectomartini.data.repository.ProfileRepository
import com.example.proyectomartini.utils.AcademiaMartini
import com.example.proyectomartini.ui.theme.*

@Composable fun HomeScreen(onExploreClick: () -> Unit = {}, onFavoritesClick: () -> Unit = {}, onRecipesClick: () -> Unit = {}, onVideosClick: () -> Unit = {}, onCoursesClick: () -> Unit = {}, onNotificationsClick: () -> Unit = {},
    onCompletarPerfil: (String) -> Unit = {},
    onRecetaDestacada: (String) -> Unit = {},
    onContinuarCurso: (String) -> Unit = {}
) {
    val context=LocalContext.current
    val auth=AuthRepository(context)
    val perfil = ProfileRepository(context).obtenerPerfil()
    val nombre = AcademiaMartini.nombreVisible(perfil.nombre, auth.nombreSesion())
    val intereses = PerfilCompleto.intereses(perfil.intereses)
    val recomendaciones = recetasMartini.filter { receta ->
        intereses.any { PerfilCompleto.coincide(it, receta.categoria) }
    }.take(3)
    val destacada = recomendaciones.firstOrNull() ?: recetasMartini.first()
    val aprendizaje = remember(context) { AprendizajeRepository(context) }
    var avance by remember(aprendizaje) { mutableStateOf(aprendizaje.leer()) }
    LaunchedEffect(aprendizaje) {
        try {
            avance = aprendizaje.sincronizar()
        } catch (_: java.io.IOException) {
            // Se conserva el progreso reciente de este dispositivo.
        } catch (_: retrofit2.HttpException) {
            // El progreso local sigue disponible si el servidor está desactualizado.
        }
    }
    val curso = cursosMartini
        .filter { AcademiaMartini.contarLecciones(it.id, it.lecciones.size, avance) in 1 until it.lecciones.size }
        .maxByOrNull { AcademiaMartini.contarLecciones(it.id, it.lecciones.size, avance) / it.lecciones.size.toFloat() }
    Column(Modifier.fillMaxSize().background(Marfil).verticalScroll(rememberScrollState())
        .padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Box(Modifier.fillMaxWidth().height(63.dp)) {
            LogoEditorial(Modifier.align(Alignment.Center).size(width = 150.dp, height = 60.dp))
            IconButton(onClick = onNotificationsClick, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Default.Notifications, "Notificaciones", tint = VerdePino)
            }
        }
        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp))) {
            FotoEditorial("editorial_saludo", R.drawable.portada_chocolate,
                Modifier.matchParentSize())
            Box(Modifier.matchParentSize().background(Brush.horizontalGradient(
                .46f to Marfil, .66f to Marfil.copy(alpha = .9f), 1f to Marfil.copy(alpha = .08f))))
            Column(Modifier.fillMaxWidth(.75f).padding(horizontal = 6.dp, vertical = 4.dp)) {
                Text(if (nombre.isBlank()) "¡Hola, chef!" else "¡Hola, chef\n$nombre!", color = VerdePino,
                    style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Box(Modifier.width(60.dp).height(4.dp).background(DoradoMartini, RoundedCornerShape(50)))
                Spacer(Modifier.height(7.dp))
                Text("Aprende, crea y haz crecer tu negocio.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        OutlinedButton(onClick = onExploreClick, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, LineaCrema),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Papel, contentColor = VerdePino)) {
            Icon(Icons.Default.Search, null, Modifier.size(19.dp))
            Spacer(Modifier.width(8.dp))
            Text("Buscar recetas, cursos y videos", Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AccesoInicio("Recetas", "Preparaciones paso a paso", GorroChef, VerdePino, Blanco, Modifier.weight(1f), onRecipesClick)
            AccesoInicio("Videos", "Técnicas y demostraciones", Icons.Default.PlayCircle, OroOscuro, Blanco, Modifier.weight(1f), onVideosClick)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AccesoInicio("Cursos", "Aprende con expertos", Icons.Default.School, Color(0xFFF2E7D0), VerdePino, Modifier.weight(1f), onCoursesClick)
            AccesoInicio("Favoritos", "Tu contenido guardado", Icons.Default.FavoriteBorder, RojoCaoba, Blanco, Modifier.weight(1f), onFavoritesClick)
        }
        Card(onClick = { onRecetaDestacada(destacada.id) }, shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7E6C6))) {
            Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f).padding(start = 6.dp)) {
                    Text(if (destacada.categoria == "Chocolatería") "Chocolate\nMaster Martini" else destacada.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold, color = Color(0xFF631B18))
                    Text("Ideas, técnicas y aplicaciones", style = MaterialTheme.typography.labelMedium)
                }
                PortadaMartini(destacada.categoria, Modifier.weight(1f).height(82.dp), recetaId = destacada.id)
                Icon(Icons.Default.ChevronRight, "Ver receta destacada", Modifier.size(28.dp), tint = OroOscuro)
            }
        }
        RecordatorioPerfil(perfil, onCompletarPerfil)
        if (recomendaciones.isNotEmpty()) {
            SeccionMartini("Para tus intereses") {
                recomendaciones.forEach { receta ->
                    OutlinedButton(onClick = { onRecetaDestacada(receta.id) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                        Text(receta.titulo)
                    }
                }
            }
        }

        if (curso != null) {
            val total = curso.lecciones.size
            val hechas = AcademiaMartini.contarLecciones(curso.id, total, avance)
            val siguiente = AcademiaMartini.siguienteLeccion(curso.id, total, avance)
            SuperficieEditorial(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(11.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Mi aprendizaje", style = MaterialTheme.typography.titleLarge, color = VerdePino, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        FotoCurso(curso, Modifier.size(width = 84.dp, height = 75.dp).clip(RoundedCornerShape(12.dp)))
                        Column(Modifier.weight(1f)) {
                            Text(curso.titulo, style = MaterialTheme.typography.titleMedium, color = VerdePino)
                            AvanceEditorial(hechas / total.toFloat(), Modifier.fillMaxWidth())
                            Text("${hechas * 100 / total}% · $hechas de $total lecciones", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    siguiente?.let { Text("Próxima lección: ${curso.lecciones[it].titulo}", style = MaterialTheme.typography.bodyMedium) }
                    Surface(shape = RoundedCornerShape(12.dp), color = CremaMartini) {
                        Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            MedallaEditorial(Modifier.width(36.dp), simbolo = simboloMedalla(curso.categoria))
                            Text("Recompensa pendiente\nMedalla y constancia académica", Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium, color = VerdePino)
                        }
                    }
                    BotonEditorial("Continuar lección", { onContinuarCurso(curso.id) }, icono = Icons.Default.PlayArrow)
                }
            }
        } else {
            SeccionMartini("Mi aprendizaje") {
                Button(onClick = onCoursesClick, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text("Ir a la academia")
                }
            }
        }

        var catalogoError by remember { mutableStateOf(false) }
        OutlinedButton(
            onClick = {
                catalogoError = runCatching {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mastermartini.cl/")))
                }.isFailure
            },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
        ) { Text("Ver catálogo oficial de insumos") }
        if (catalogoError) Text("Abre mastermartini.cl en tu navegador.", color = GrisTexto)
        Spacer(Modifier.height(8.dp))
    }
}
@Composable
private fun AccesoInicio(titulo: String, detalle: String, icono: ImageVector, fondo: Color, tinta: Color,
    modifier: Modifier, accion: () -> Unit) {
    Card(onClick = accion, modifier = modifier.heightIn(min = 96.dp),
        colors = CardDefaults.cardColors(containerColor = fondo), shape = RoundedCornerShape(17.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 9.dp)) {
            Icon(icono, null, Modifier.size(27.dp), tint = tinta)
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = tinta, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(detalle, Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = tinta)
                Icon(Icons.Default.ChevronRight, null, Modifier.size(20.dp), tint = tinta)
            }
        }
    }
}
