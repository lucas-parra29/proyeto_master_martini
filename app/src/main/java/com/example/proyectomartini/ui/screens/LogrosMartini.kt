package com.example.proyectomartini.ui.screens

import android.content.Context
import android.content.Intent
import android.content.ClipData
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.proyectomartini.R
import com.example.proyectomartini.data.remote.ConstanciaRemota
import com.example.proyectomartini.data.repository.AuthRepository
import com.example.proyectomartini.data.repository.AprendizajeRepository
import com.example.proyectomartini.utils.AcademiaMartini
import com.example.proyectomartini.utils.IdentidadLocal
import com.example.proyectomartini.ui.theme.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal fun preferenciasAcademia(context: Context) = context.getSharedPreferences(
    "academia_" + IdentidadLocal.clave(AuthRepository(context).correoSesion()), Context.MODE_PRIVATE)

internal fun leerAvanceAcademia(context: Context): Set<String> =
    preferenciasAcademia(context).getStringSet("lecciones", emptySet()).orEmpty().toSet()

@Composable
internal fun PremioCurso(curso: CursoMartini, onVolver: () -> Unit, onExplorar: () -> Unit,
    abrirConstancia: Boolean = false) {
    val context = LocalContext.current
    val aprendizaje = remember(context) { AprendizajeRepository(context) }
    val scope = rememberCoroutineScope()
    var datos by remember(curso.id) { mutableStateOf(aprendizaje.constanciaGuardada(curso.id)) }
    var vista by rememberSaveable(curso.id, abrirConstancia) { mutableStateOf(abrirConstancia) }
    var procesando by remember { mutableStateOf(false) }
    var evaluar by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    val logo = remember(context) { BitmapFactory.decodeResource(context.resources, R.drawable.master_martini_logo) }

    fun cargar() {
        if (procesando) return
        procesando = true
        mensaje = ""
        scope.launch {
            try {
                datos = aprendizaje.obtenerConstancia(curso.id)
            } catch (error: CancellationException) {
                throw error
            } catch (error: retrofit2.HttpException) {
                if (error.code() == 422) evaluar = true
                else mensaje = "No se pudo verificar la constancia. Revisa tu conexión."
            } catch (_: Exception) {
                mensaje = "No se pudo verificar la constancia. Revisa tu conexión."
            } finally {
                procesando = false
            }
        }
    }

    LaunchedEffect(vista) { if (vista && datos == null) cargar() }
    val volver = { if (vista && !abrirConstancia) vista = false else onVolver() }
    BackHandler(onBack = volver)
    if (evaluar) {
        EvaluacionCurso(curso.categoria, { evaluar = false }) { respuestas ->
            evaluar = false
            aprendizaje.guardarEvaluacion(curso.id, respuestas)
            cargar()
        }
    }
    val exportar = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { destino ->
        val constancia = datos
        if (destino != null && constancia != null) scope.launch {
            mensaje = try {
                withContext(Dispatchers.IO) { escribirConstancia(context, destino, curso, constancia) }
                "Constancia guardada."
            } catch (error: CancellationException) { throw error }
            catch (_: Exception) { "No se pudo guardar la constancia." }
        }
    }

    if (!vista) {
        Column(Modifier.fillMaxSize().background(Marfil).verticalScroll(rememberScrollState()).padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().height(218.dp)) {
                ConfetiPremio(Modifier.matchParentSize())
                LogoEditorial(Modifier.align(Alignment.TopCenter).size(width = 146.dp, height = 73.dp))
                AccionCircular(Icons.Default.ArrowBack, "Volver", volver, Modifier.align(Alignment.TopStart), fondo = Papel, tinta = VerdePino)
                MedallaEditorial(Modifier.align(Alignment.BottomCenter).width(132.dp), simbolo = simboloMedalla(curso.categoria))
            }
            Text("¡Curso\ncompletado!", style = MaterialTheme.typography.headlineLarge, color = VerdePino,
                fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            Text(curso.titulo, style = MaterialTheme.typography.titleMedium, color = VerdePino, textAlign = TextAlign.Center)
            SuperficieEditorial(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    IndicadorEditorial(Icons.Default.CheckCircle, "${curso.lecciones.size}/${curso.lecciones.size}\nLecciones")
                    IndicadorEditorial(Icons.Default.Schedule, "${curso.tiempoLectura()} min\nLectura")
                    IndicadorEditorial(Icons.Default.Star, "100%\nProgreso")
                }
            }
            SuperficieEditorial(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Tu recompensa", style = MaterialTheme.typography.titleMedium, color = ChocolateMartini,
                            fontWeight = FontWeight.Bold)
                        Text("Medalla de ${curso.categoria}", style = MaterialTheme.typography.bodyMedium)
                    }
                    FotoCurso(curso, Modifier.size(width = 90.dp, height = 60.dp).clip(RoundedCornerShape(10.dp)))
                }
            }
            BotonEditorial("Ver mi certificado", { vista = true }, icono = Icons.Default.WorkspacePremium)
            OutlinedButton(onClick = onExplorar, modifier = Modifier.fillMaxWidth().heightIn(min = 49.dp)) {
                Icon(Icons.Default.MenuBook, null); Spacer(Modifier.width(8.dp)); Text("Explorar más cursos")
            }
        }
        return
    }

    Column(Modifier.fillMaxSize().background(Marfil).verticalScroll(rememberScrollState())) {
        Row(Modifier.fillMaxWidth().background(VerdePino).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AccionCircular(Icons.Default.ArrowBack, "Volver", volver, fondo = VerdePino)
            Text("Mi certificado", style = MaterialTheme.typography.headlineMedium, color = Blanco, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.fillMaxWidth().padding(13.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            val constancia = datos
            if (constancia != null) {
                Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(VerdePino).padding(7.dp)) {
                    Canvas(Modifier.fillMaxWidth().aspectRatio(600f / 800f)) {
                        val lienzo = drawContext.canvas.nativeCanvas
                        lienzo.save(); lienzo.scale(size.width / 600f, size.height / 800f)
                        dibujarDiploma(lienzo, logo, constancia.nombre, curso.titulo, formatoFecha(constancia.fechaEmision))
                        lienzo.restore()
                    }
                }
                BotonEditorial("Descargar certificado", { exportar.launch("constancia-${curso.id}.pdf") },
                    icono = Icons.Default.Download, habilitado = !procesando)
                OutlinedButton(
                    onClick = {
                        procesando = true
                        scope.launch {
                            try {
                                val uri = withContext(Dispatchers.IO) {
                                    val carpeta = java.io.File(context.cacheDir, "constancias").apply { mkdirs() }
                                    val archivo = java.io.File.createTempFile("constancia-${curso.id}-", ".pdf", carpeta)
                                    archivo.outputStream().use { generarConstancia(context, it, curso, constancia) }
                                    FileProvider.getUriForFile(context, "${context.packageName}.constancias", archivo)
                                }
                                val envio = Intent(Intent.ACTION_SEND).apply {
                                    type = "application/pdf"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    clipData = ClipData.newRawUri("Constancia", uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(envio, "Compartir constancia"))
                            } catch (error: CancellationException) { throw error }
                            catch (_: Exception) { mensaje = "No se pudo compartir la constancia." }
                            finally { procesando = false }
                        }
                    }, enabled = !procesando, modifier = Modifier.fillMaxWidth().heightIn(min = 49.dp)
                ) { Icon(Icons.Default.Share, null); Spacer(Modifier.width(8.dp)); Text("Compartir") }
            } else {
                SeccionMartini("Constancia académica") {
                    Text("El certificado se habilita una vez que el servidor confirme tu evaluación.")
                    if (procesando) LinearProgressIndicator(Modifier.fillMaxWidth())
                    else BotonEditorial("Verificar constancia", ::cargar)
                }
            }
            if (mensaje.isNotBlank()) Text(mensaje, color = VerdePino)
        }
    }
}

@Composable
private fun ConfetiPremio(modifier: Modifier) {
    val entrada = remember { Animatable(0f) }
    LaunchedEffect(Unit) { entrada.animateTo(1f, tween(850)) }
    Canvas(modifier) {
        repeat(24) { indice ->
            val lado = if (indice % 2 == 0) .07f else .80f
            val x = size.width * (lado + (indice % 6) * .025f)
            val y = size.height * (.14f + ((indice * 7) % 17) * .044f) * (.72f + entrada.value * .28f)
            val tinta = listOf(DoradoMartini, OroOscuro, RojoCaoba)[indice % 3]
            drawRect(tinta.copy(alpha = entrada.value), Offset(x, y), Size(5.dp.toPx(), 3.dp.toPx()))
        }
    }
}

private fun formatoFecha(fecha: Long) = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG,
    java.util.Locale.forLanguageTag("es-CL")).format(java.util.Date(fecha))

private fun escribirConstancia(context: Context, uri: Uri, curso: CursoMartini, datos: ConstanciaRemota) {
    context.contentResolver.openOutputStream(uri)?.use { generarConstancia(context, it, curso, datos) }
        ?: error("No se pudo abrir el archivo")
}

private fun generarConstancia(context: Context, salida: java.io.OutputStream, curso: CursoMartini, datos: ConstanciaRemota) {
    require(AcademiaMartini.cursoCompleto(curso.id, curso.lecciones.size, leerAvanceAcademia(context)))
    val logo = BitmapFactory.decodeResource(context.resources, R.drawable.master_martini_logo)
    val pdf = PdfDocument()
    try {
        val pagina = pdf.startPage(PdfDocument.PageInfo.Builder(600, 800, 1).create())
        dibujarDiploma(pagina.canvas, logo, datos.nombre, curso.titulo, formatoFecha(datos.fechaEmision))
        pdf.finishPage(pagina)
        pdf.writeTo(salida)
    } finally {
        pdf.close()
    }
}
