package com.example.proyectomartini.ui.screens
import android.net.Uri
import android.content.SharedPreferences
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.font.FontWeight
import android.widget.VideoView
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.data.local.FavoritoEntity
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.viewmodel.FavoritosViewModel

@Composable fun VideoPlayerScreen(videoId: String, titulo: String, onBackClick: () -> Unit,
    onReceta: (String) -> Unit = {}, onCurso: (String) -> Unit = {}, favoritosViewModel: FavoritosViewModel = viewModel()) {
    val tutorial = tutorialesMartini.find { it.id == videoId }
    val context = LocalContext.current
    val favoritos by favoritosViewModel.titulosFavoritos.collectAsState()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val fuente = videosOficiales[videoId]
    val rawId = remember(videoId) {
        context.resources.getIdentifier("tutorial_" + videoId, "raw", context.packageName)
    }
    var mostrarDemo by rememberSaveable(videoId) { mutableStateOf(false) }
    val prefs = remember(context) { preferenciasVideos(context) }
    val claveDemo = "demo_$videoId"
    val huboAvance = remember(videoId) {
        prefs.getInt(claveDemo, 0) > 0 || (fuente != null && prefs.getInt("yt_${fuente.id}", 0) > 0)
    }
    var recarga by remember(videoId) { mutableIntStateOf(0) }
    var player by remember(videoId) { mutableStateOf<VideoView?>(null) }
    var listo by remember(videoId) { mutableStateOf(false) }
    var error by remember(videoId) { mutableStateOf(false) }
    var reproduciendo by remember(videoId) { mutableStateOf(false) }
    var duracion by remember(videoId) { mutableIntStateOf(0) }
    var posicion by remember(videoId) { mutableIntStateOf(0) }
    LaunchedEffect(player,listo) {
        while(listo && player != null) {
            posicion=player?.currentPosition?.coerceAtLeast(0) ?: 0
            reproduciendo=player?.isPlaying == true
            if (reproduciendo && posicion > 0) {
                prefs.edit().putInt(claveDemo, posicion).apply()
                if (posicion >= 10_000) marcarVideoVisto(prefs, videoId)
            }
            delay(700)
        }
    }
    DisposableEffect(lifecycle,player) {
        val observer=LifecycleEventObserver { _, evento -> if(evento == Lifecycle.Event.ON_PAUSE) { player?.pause(); reproduciendo=false } }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
    Column(Modifier.fillMaxSize().background(Marfil).verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().height(124.dp).background(VerdePino)) {
            tutorial?.let { PortadaMartini(it.categoria, Modifier.fillMaxHeight().width(160.dp).align(androidx.compose.ui.Alignment.CenterEnd)) }
            AccionCircular(Icons.Default.ArrowBack, "Volver", onBackClick, Modifier.padding(6.dp))
            Text("Videos técnicos", Modifier.align(androidx.compose.ui.Alignment.BottomStart).padding(14.dp),
                color = Blanco, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        }
        Column(Modifier.fillMaxWidth().padding(15.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
            if(tutorial == null) { Text("Este contenido antiguo ya no está disponible en el nuevo catálogo."); Button(onClick=onBackClick) { Text("Volver") }; return@Column }
            Text(tutorial.titulo, style = MaterialTheme.typography.headlineMedium, color = VerdePino, fontWeight = FontWeight.Bold)
            IndicadorEditorial(Icons.Default.BarChart, "Básico")
            if (huboAvance) EtiquetaMartini("Continúa donde lo dejaste")
            if (fuente != null && !mostrarDemo) {
                EtiquetaMartini("Video publicado por Master Martini Chile · ${fuente.duracion}")
                Text(fuente.titulo, style = MaterialTheme.typography.titleMedium)
                ReproductorOficial(fuente, if (rawId != 0) ({ mostrarDemo = true }) else null)
            } else {
                EtiquetaMartini("Cápsula académica ilustrada · 24 segundos")
                if (fuente != null) {
                    OutlinedButton(onClick = { mostrarDemo = false },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Volver al video oficial") }
                }
            }
            Text(tutorial.objetivo,style=MaterialTheme.typography.bodyLarge,color=GrisTexto)
            if(rawId != 0 && (fuente == null || mostrarDemo)) {
                key(videoId, recarga) {
                    AndroidView(modifier=Modifier.fillMaxWidth().aspectRatio(16f/9f).clip(RoundedCornerShape(15.dp)).background(VerdePino),factory={ ctx -> VideoView(ctx).apply {
                        player=this
                        setOnPreparedListener { duracion=duration.coerceAtLeast(0); listo=true; seekTo(prefs.getInt(claveDemo, 1).coerceIn(0, duracion)) }
                        setOnCompletionListener { reproduciendo=false; prefs.edit().remove(claveDemo).apply() }
                        setOnErrorListener { _,_,_ -> error=true; reproduciendo=false; true }
                        setVideoURI(Uri.parse("android.resource://"+ctx.packageName+"/"+rawId))
                    } },onRelease={ view -> view.stopPlayback(); if(player===view) player=null })
                }
                if (error) {
                    Text("No se pudo reproducir esta cápsula.", color = MaterialTheme.colorScheme.error)
                    OutlinedButton(onClick = { error = false; listo = false; recarga++ },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Reintentar cápsula") }
                }
                else if(!listo) LinearProgressIndicator(Modifier.fillMaxWidth())
                if(listo && !error && duracion>0) {
                    Slider(value=posicion.toFloat().coerceIn(0f,duracion.toFloat()),onValueChange={ posicion=it.toInt(); player?.seekTo(posicion) },valueRange=0f..duracion.toFloat(),modifier=Modifier.fillMaxWidth())
                    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) { Text(formatoTiempo(posicion)); Text(formatoTiempo(duracion)) }
                }
                Button(onClick={ if(reproduciendo) player?.pause() else player?.start(); reproduciendo=!reproduciendo }, enabled=listo&&!error,modifier=Modifier.fillMaxWidth().heightIn(min=56.dp)) { Text(if(reproduciendo) "Pausar video" else "Reproducir video") }
                OutlinedButton(onClick={ player?.seekTo(0); player?.start(); reproduciendo=true },enabled=listo&&!error,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) { Text("Reproducir desde el inicio") }
            }
            OutlinedButton(onClick={ favoritosViewModel.alternar(FavoritoEntity(tutorial.titulo,"VIDEO",fuente?.duracion ?: "24 segundos","Básico")) },modifier=Modifier.fillMaxWidth().heightIn(min=52.dp)) { Text(if(tutorial.titulo in favoritos) "Quitar de favoritos" else "Guardar video") }
            SeccionMartini("Producto relacionado") {
                CatalogoInsumoFoto(tutorial.categoria, Modifier.fillMaxWidth().height(94.dp))
                Text(tutorial.producto, style = MaterialTheme.typography.bodyLarge, color = VerdePino)
            }
            val recetas = recetasMartini.filter { it.categoria == tutorial.categoria }.take(2)
            val curso = cursosMartini.firstOrNull { it.categoria == tutorial.categoria }
            if (recetas.isNotEmpty() || curso != null) SeccionMartini("Sigue aprendiendo") {
                recetas.forEach { r ->
                    OutlinedButton(onClick = { onReceta(r.id) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(r.titulo) }
                }
                curso?.let {
                    OutlinedButton(onClick = { onCurso(it.id) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(it.titulo) }
                }
            }
            SeccionMartini("Guía de la técnica") { tutorial.pasos.forEachIndexed { i,paso -> Text("${i+1}. $paso",style=MaterialTheme.typography.bodyLarge,color=GrisTexto) } }
            if (fuente == null || mostrarDemo) Text("Material audiovisual ilustrado de demostración académica; no es una grabación ni una clase oficial de Master Martini. Consulta la ficha técnica de cada producto.",style=MaterialTheme.typography.bodySmall,color=GrisTexto)
        }
    }
}

private fun formatoTiempo(ms:Int):String { val segundos=ms.coerceAtLeast(0)/1000; return "%d:%02d".format(segundos/60,segundos%60) }

internal fun marcarVideoVisto(prefs: SharedPreferences, id: String) {
    val vistos = prefs.getStringSet("videos_vistos", emptySet()).orEmpty()
    if (id !in vistos) prefs.edit().putStringSet("videos_vistos", vistos + id).apply()
}
