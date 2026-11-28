package com.example.proyectomartini.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.proyectomartini.R
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PlayArrow
import com.example.proyectomartini.data.repository.AuthRepository
import com.example.proyectomartini.utils.IdentidadLocal
import com.example.proyectomartini.utils.AcademiaMartini
import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectomartini.ui.theme.*

data class LeccionMartini(
    val titulo: String,
    val descripcion: String
)

data class CursoMartini(
    val id: String,
    val titulo: String,
    val categoria: String,
    val nivel: String,
    val descripcion: String,
    val lecciones: List<LeccionMartini>
)

private val cursosBase = listOf(
    CursoMartini(
        id = "curso-pasteleria",
        titulo = "Pastelería de vitrina",
        categoria = "Pastelería",
        nivel = "Básico",
        descripcion = "Aprende las técnicas esenciales de elaboración de postres.",
        lecciones = listOf(
            LeccionMartini(
                "1. Higiene y preparación",
                "Antes de cocinar, lava tus manos y prepara una superficie limpia. Revisa las fechas de los ingredientes y pesa cada cantidad con una balanza. Mantén refrigerados los productos perecederos cuando no se utilicen."
            ),
            LeccionMartini(
                "2. Técnicas de mezclado",
                "El batido incorpora aire y ayuda a obtener preparaciones esponjosas. Al añadir harina a un bizcocho, mezcla lo necesario para integrar los ingredientes y evita trabajar excesivamente la masa."
            ),
            LeccionMartini(
                "3. Horneado y decoración",
                "Precalienta el horno y utiliza el molde adecuado. Comprueba la cocción antes de retirar. Deja enfriar los bizcochos antes de cortar, rellenar o decorar con crema."
            )
        )
    ),
    CursoMartini(
        id = "curso-chocolate",
        titulo = "Chocolatería con Ariba",
        categoria = "Chocolatería",
        nivel = "Intermedio",
        descripcion = "Descubre técnicas básicas para trabajar con chocolate.",
        lecciones = listOf(
            LeccionMartini(
                "1. Tipos de chocolate",
                "El chocolate negro, con leche y blanco presentan proporciones diferentes de cacao, azúcar y grasa. Estas diferencias modifican su sabor, fluidez y comportamiento durante la preparación."
            ),
            LeccionMartini(
                "2. Preparación de ganache",
                "Pica el chocolate en trozos pequeños. Calienta la crema y viértela sobre el chocolate. Espera unos instantes y mezcla suavemente hasta obtener una preparación homogénea."
            ),
            LeccionMartini(
                "3. Acabados y conservación",
                "Utiliza utensilios secos y limpios para trabajar con chocolate. Protege los productos de temperaturas elevadas, humedad y olores fuertes. Las elaboraciones con crema requieren refrigeración adecuada."
            )
        )
    ),
    CursoMartini(
        id = "curso-horneo",
        titulo = "Panadería y Técnicas de Horneo",
        categoria = "Horneo",
        nivel = "Básico",
        descripcion = "Aprende a preparar masas y controlar su fermentación.",
        lecciones = listOf(
            LeccionMartini(
                "1. Ingredientes del pan",
                "La harina, el agua, la levadura y la sal son ingredientes habituales del pan. Sus cantidades influyen en la textura, el sabor y la fermentación."
            ),
            LeccionMartini(
                "2. Amasado y fermentación",
                "El amasado desarrolla la estructura de la masa. Durante la fermentación, la levadura genera gas que contribuye al aumento de volumen. Controla temperatura, tiempo y aspecto de la masa."
            ),
            LeccionMartini(
                "3. Cocción y enfriamiento",
                "Precalienta el horno según el tipo de pan. Ajusta el tiempo de cocción al tamaño de las piezas. Al terminar, deja enfriar sobre una rejilla antes de guardar o cortar."
            )
        )
    )
)


internal val cursosMartini = cursosBase + tutorialesMartini.filter { it.categoria !in cursosBase.map { c -> c.categoria } }.distinctBy { it.categoria }.map { t ->
    CursoMartini("curso-${t.id}", "Curso: ${t.titulo}", t.categoria, "Básico", t.objetivo + " con orientación para emprendedores.", listOf(
        LeccionMartini("1. Organiza tus insumos", "Producto de referencia: ${t.producto}. Consulta la ficha técnica, pesa los ingredientes y prepara una estación limpia. ${t.pasos[0]}"),
        LeccionMartini("2. Aplica la técnica", t.pasos[1] + " Registra las cantidades y observa el resultado antes de producir un lote grande."),
        LeccionMartini("3. Control de calidad", t.pasos[2] + " Evalúa textura, presentación, rendimiento y costo por porción.")
    ))
}

internal fun CursoMartini.tiempoLectura() = lecciones.sumOf { leccion ->
    ((leccion.descripcion.trim().split(Regex("\\s+")).size + 179) / 180).coerceAtLeast(1)
}

@Composable
internal fun FotoCurso(curso: CursoMartini, modifier: Modifier) {
    FotoEditorial("editorial_" + curso.id.replace('-', '_'), portadaCategoria(curso.categoria),
        modifier, "Imagen ilustrativa de ${curso.titulo}")
}

@Composable
fun CoursesScreen(onBackClick: () -> Unit, onVideoClick: (String, String) -> Unit = { _, _ -> },
    cursoInicial: String? = null, continuarAlAbrir: Boolean = false, constanciaInicial: Boolean = false,
    favoritosViewModel: com.example.proyectomartini.viewmodel.FavoritosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val aprendizaje = remember(context) { com.example.proyectomartini.data.repository.AprendizajeRepository(context) }
    var completadas by remember(aprendizaje) { mutableStateOf(aprendizaje.leer()) }
    LaunchedEffect(aprendizaje, completadas) {
        try { completadas = aprendizaje.sincronizar() }
        catch (_: java.io.IOException) { /* Se conserva el progreso local. */ }
        catch (_: retrofit2.HttpException) { /* Se conserva el progreso local. */ }
    }
    val favoritos by favoritosViewModel.titulosFavoritos.collectAsState()
    var cursoId by rememberSaveable(cursoInicial) { mutableStateOf(cursoInicial) }
    var indice by rememberSaveable(cursoInicial, continuarAlAbrir) {
        val curso = cursosMartini.firstOrNull { it.id == cursoInicial }
        mutableStateOf(if (continuarAlAbrir && curso != null)
            AcademiaMartini.siguienteLeccion(curso.id, curso.lecciones.size, completadas) else null)
    }
    var premio by rememberSaveable(cursoInicial, constanciaInicial) { mutableStateOf(constanciaInicial) }
    var categoria by rememberSaveable { mutableStateOf("Todos") }
    var dificultad by rememberSaveable { mutableStateOf("Todos") }
    var estado by rememberSaveable { mutableStateOf("Todos") }
    var filtros by rememberSaveable { mutableStateOf(false) }

    fun volver() {
        when {
            premio && constanciaInicial -> onBackClick()
            premio -> premio = false
            indice != null -> indice = null
            cursoId != null && cursoInicial != null -> onBackClick()
            cursoId != null -> cursoId = null
            else -> onBackClick()
        }
    }
    BackHandler(onBack = ::volver)

    val curso = cursosMartini.find { it.id == cursoId }
    val leccion = indice
    val contadas = curso?.let { AcademiaMartini.contarLecciones(it.id, it.lecciones.size, completadas) } ?: 0
    val proxima = curso?.let { AcademiaMartini.siguienteLeccion(it.id, it.lecciones.size, completadas) }

    if (curso != null && premio && contadas == curso.lecciones.size) {
        PremioCurso(curso, onVolver = ::volver, onExplorar = { premio = false; cursoId = null; indice = null },
            abrirConstancia = constanciaInicial)
        return
    }

    key(cursoId, leccion) {
        LazyColumn(Modifier.fillMaxSize().background(Marfil), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (curso == null) {
                item {
                    Box(Modifier.fillMaxWidth().heightIn(min = 164.dp).clip(RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp))) {
                        FotoEditorial("editorial_chocolate", R.drawable.foto_ganache, Modifier.matchParentSize())
                        Box(Modifier.matchParentSize().background(Brush.horizontalGradient(.10f to VerdePino,
                            .53f to VerdePino, 1f to Color.Transparent)))
                        Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                            Icon(GorroChef, null, Modifier.size(34.dp), tint = DoradoMartini)
                            Text("Academia\nMaster Martini", style = MaterialTheme.typography.headlineMedium,
                                color = Blanco, fontWeight = FontWeight.ExtraBold)
                            Text("Cursos prácticos para tu negocio", color = Blanco)
                        }
                    }
                }
                item {
                    Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        (listOf("Todos") + cursosMartini.map { it.categoria }.distinct()).forEach {
                            PildoraEditorial(it, categoria == it, { categoria = it }, dorada = true)
                        }
                    }
                    TextButton(onClick = { filtros = !filtros }, modifier = Modifier.padding(horizontal = 12.dp)) {
                        Icon(Icons.Default.Tune, null); Spacer(Modifier.width(6.dp))
                        Text(if (filtros) "Ocultar filtros" else "Filtrar por nivel o progreso")
                    }
                    if (filtros) {
                        Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp)) {
                            listOf("Todos", "Básico", "Intermedio").forEach { PildoraEditorial(it, dificultad == it, { dificultad = it }) }
                        }
                        Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp)) {
                            listOf("Todos", "Sin iniciar", "En progreso", "Completados").forEach {
                                PildoraEditorial(it, estado == it, { estado = it })
                            }
                        }
                    }
                }
                val visibles = cursosMartini.filter { candidato ->
                    val hechas = AcademiaMartini.contarLecciones(candidato.id, candidato.lecciones.size, completadas)
                    val avance = when (hechas) { 0 -> "Sin iniciar"; candidato.lecciones.size -> "Completados"; else -> "En progreso" }
                    (categoria == "Todos" || candidato.categoria == categoria) &&
                        (dificultad == "Todos" || candidato.nivel == dificultad) && (estado == "Todos" || estado == avance)
                }.sortedBy { listOf("Chocolatería", "Pastelería", "Bollería", "Heladería").indexOf(it.categoria).let { posicion -> if (posicion < 0) 4 else posicion } }
                if (visibles.isEmpty()) item { Text("No hay cursos con estos filtros.", Modifier.padding(16.dp)) }
                items(visibles, key = { it.id }) { candidato ->
                    val hechas = AcademiaMartini.contarLecciones(candidato.id, candidato.lecciones.size, completadas)
                    TarjetaCurso(candidato, hechas) { cursoId = candidato.id; indice = null }
                }
            } else if (leccion == null) {
                val guardado = curso.titulo in favoritos
                item {
                    Box(Modifier.fillMaxWidth().heightIn(min = 235.dp).clip(RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp))) {
                        FotoCurso(curso, Modifier.matchParentSize())
                        Box(Modifier.matchParentSize().background(Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = .18f), Color.Transparent, Color.Black.copy(alpha = .89f)))))
                        AccionCircular(Icons.Default.ArrowBack, "Volver", ::volver, Modifier.align(Alignment.TopStart).padding(7.dp))
                        AccionCircular(if (guardado) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            if (guardado) "Quitar curso de favoritos" else "Guardar curso", {
                                favoritosViewModel.alternar(com.example.proyectomartini.data.local.FavoritoEntity(
                                    curso.titulo, "CURSO", "${curso.lecciones.size} lecciones", curso.nivel))
                            }, Modifier.align(Alignment.TopEnd).padding(7.dp))
                        Column(Modifier.align(Alignment.BottomStart).padding(top = 63.dp, start = 17.dp, end = 17.dp, bottom = 17.dp)) {
                            Text(curso.titulo, style = MaterialTheme.typography.headlineMedium,
                                color = Blanco, fontWeight = FontWeight.ExtraBold)
                            Text(curso.descripcion, color = Blanco, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                item {
                    Column(Modifier.padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IndicadorEditorial(Icons.Default.BarChart, curso.nivel, Modifier.weight(1f))
                            IndicadorEditorial(Icons.Default.MenuBook, "${curso.lecciones.size} lecciones", Modifier.weight(1f))
                            IndicadorEditorial(Icons.Default.Schedule, "${curso.tiempoLectura()} min lectura", Modifier.weight(1f))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(Modifier.weight(1f)) {
                                Text("Progreso del curso", style = MaterialTheme.typography.labelLarge)
                                AvanceEditorial(contadas.toFloat() / curso.lecciones.size, Modifier.fillMaxWidth())
                            }
                            Text("${contadas * 100 / curso.lecciones.size}%", color = VerdePino, fontWeight = FontWeight.Bold)
                        }
                        Text("Lecciones", style = MaterialTheme.typography.titleLarge, color = VerdePino, fontWeight = FontWeight.Bold)
                    }
                }
                items(curso.lecciones.size) { i ->
                    val hecha = "${curso.id}:$i" in completadas
                    val siguiente = proxima == i
                    val bloqueada = !hecha && !siguiente
                    Surface(onClick = { indice = i }, enabled = !bloqueada, modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp)
                        .heightIn(min = 49.dp), shape = CircleShape, color = if (siguiente) OroClaro else Papel,
                        border = BorderStroke(1.dp, LineaCrema)) {
                        Row(Modifier.padding(horizontal = 11.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(when { hecha -> Icons.Default.CheckCircle; bloqueada -> Icons.Default.Lock; else -> Icons.Default.PlayCircleOutline },
                                when { hecha -> "Completada"; bloqueada -> "Bloqueada"; else -> "Próxima lección" },
                                Modifier.size(21.dp), tint = when { hecha -> VerdeBosque; bloqueada -> GrisTexto; else -> OroOscuro })
                            Text("${i + 1}. ${curso.lecciones[i].titulo.substringAfter(". ")}", Modifier.weight(1f),
                                color = if (bloqueada) GrisTexto else VerdePino, fontWeight = FontWeight.SemiBold)
                            if (!bloqueada) Icon(Icons.Default.PlayArrow, null, Modifier.size(20.dp), tint = VerdePino)
                        }
                    }
                }
                item {
                    Column(Modifier.padding(horizontal = 14.dp, vertical = 5.dp)) {
                        BotonEditorial(if (proxima == null) "Repasar lecciones" else "Continuar lección",
                            { indice = proxima ?: 0 }, icono = Icons.Default.PlayArrow)
                        Spacer(Modifier.height(8.dp))
                        BotonEditorial(if (proxima == null) "Ver recompensa y certificado" else "Obtén tu certificado al completar el curso",
                            { premio = true }, icono = Icons.Default.WorkspacePremium, dorado = true, habilitado = proxima == null)
                    }
                }
            } else {
                val contenido = curso.lecciones[leccion]
                val clave = "${curso.id}:$leccion"
                item {
                    CabeceraMartini(curso.titulo, "Lección ${leccion + 1} de ${curso.lecciones.size}", ::volver)
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        FotoCurso(curso, Modifier.fillMaxWidth().height(156.dp).clip(RoundedCornerShape(17.dp)))
                        SeccionMartini(contenido.titulo) { Text(contenido.descripcion) }
                        SeccionMartini("Insumo de referencia") {
                            Text(productoSugerido(curso.categoria), color = VerdePino)
                        }
                        tutorialesMartini.firstOrNull { it.categoria == curso.categoria }?.let {
                            OutlinedButton(onClick = { onVideoClick(it.id, it.titulo) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                                Icon(Icons.Default.PlayArrow, null); Text("Ver demostración")
                            }
                        }
                        var evaluar by rememberSaveable(clave) { mutableStateOf(false) }
                        var superada by rememberSaveable(clave) { mutableStateOf(false) }
                        val practica = practicasAcademia[curso.id]?.getOrNull(leccion)
                        if (practica != null) {
                            PracticaGuiada(clave, practica, superada || clave in completadas) { superada = true }
                        }
                        val ultima = clave !in completadas && contadas == curso.lecciones.lastIndex
                        fun completar() {
                            completadas = aprendizaje.guardar(setOf(clave))
                            indice = AcademiaMartini.siguienteLeccion(curso.id, curso.lecciones.size, completadas)
                            if (ultima) premio = true
                        }
                        if (evaluar) EvaluacionCurso(curso.categoria, { evaluar = false }) { respuestas ->
                            evaluar = false
                            aprendizaje.guardarEvaluacion(curso.id, respuestas)
                            completar()
                        }
                        BotonEditorial(when { ultima -> "Hacer evaluación final"; clave in completadas -> "Continuar"; else -> "Marcar como completada" },
                            { if (ultima) evaluar = true else completar() }, icono = Icons.Default.Check,
                            habilitado = practica == null || superada || clave in completadas)
                    }
                }
            }
            item { Spacer(Modifier.height(14.dp)) }
        }
    }
}

@Composable
private fun TarjetaCurso(curso: CursoMartini, completadas: Int, abrir: () -> Unit) {
    val progreso = completadas.toFloat() / curso.lecciones.size
    Surface(onClick = abrir, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        shape = RoundedCornerShape(17.dp), color = Papel, border = BorderStroke(1.dp, LineaCrema), shadowElevation = 2.dp) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            FotoCurso(curso, Modifier.size(width = 99.dp, height = 91.dp).clip(RoundedCornerShape(13.dp)))
            Column(Modifier.weight(1f)) {
                Text(curso.titulo, style = MaterialTheme.typography.titleMedium, color = VerdePino, fontWeight = FontWeight.ExtraBold)
                IndicadorEditorial(Icons.Default.BarChart, curso.nivel)
                IndicadorEditorial(Icons.Default.MenuBook, "${curso.lecciones.size} lecciones")
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    AvanceEditorial(progreso, Modifier.weight(1f))
                    Text("${(100 * progreso).toInt()}%", style = MaterialTheme.typography.labelMedium, color = VerdePino)
                }
            }
            Box(Modifier.size(27.dp).background(OroOscuro, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ChevronRight, "Abrir curso", Modifier.size(19.dp), tint = Blanco)
            }
        }
    }
}
