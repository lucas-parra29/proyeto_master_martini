package com.example.proyectomartini.ui.screens

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.proyectomartini.data.repository.AuthRepository
import com.example.proyectomartini.utils.IdentidadLocal
import kotlinx.coroutines.delay
import org.json.JSONArray

internal data class VideoOficial(val id: String, val titulo: String, val duracion: String)

internal val videosOficiales = mapOf(
    "chocolate" to VideoOficial("v_WinlHRQt0", "Tips y usos del chocolate con la chef Lili Cañete", "8:18"),
    "pastel" to VideoOficial("3n-h0n7Ttks", "Cómo utilizar la crema vegetal Bravo Crem", "0:43"),
    "helado" to VideoOficial("R2N_oOG0xI8", "Helado artesanal de frutos del bosque · Martini Gelato", "0:56"),
    "torta-avellana" to VideoOficial("ZwOkabO177M", "Torta de chocolate y avellana · Caravella Deco", "1:17"),
    "crema-delice" to VideoOficial("1U1twaw6bP0", "Cómo usar las cremas Delice en polvo", "1:01"),
    "gratin-bravo" to VideoOficial("UJAF5sEBgnA", "Gratín de papas · Bravo Crem Cocina", "0:52")
)

internal fun preferenciasVideos(context: Context) = context.getSharedPreferences(
    "video_martini_" + IdentidadLocal.clave(AuthRepository(context).correoSesion()),
    Context.MODE_PRIVATE
)

@Composable
internal fun ReproductorOficial(video: VideoOficial, onDemo: (() -> Unit)? = null) {
    val context = LocalContext.current
    val prefs = remember(context) { preferenciasVideos(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var intento by remember(video.id) { mutableIntStateOf(0) }
    var listo by remember(video.id, intento) { mutableStateOf(false) }
    var error by remember(video.id, intento) { mutableStateOf(false) }
    var web by remember(video.id, intento) { mutableStateOf<WebView?>(null) }
    val clave = "yt_${video.id}"

    DisposableEffect(lifecycle, web) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    web?.evaluateJavascript("if (window.player?.pauseVideo) player.pauseVideo();", null)
                    web?.onPause()
                }
                Lifecycle.Event.ON_RESUME -> web?.onResume()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(web, intento) {
        val vista = web ?: return@LaunchedEffect
        var espera = 0
        while (!error) {
            vista.evaluateJavascript("window.mmStatus ? window.mmStatus() : null") { value ->
                val datos = runCatching { JSONArray(value) }.getOrNull()
                if (datos != null) {
                    listo = datos.optBoolean(0)
                    error = datos.optInt(1) != 0
                    val segundos = datos.optInt(2, -1)
                    if (segundos >= 0 && listo) {
                        prefs.edit().putInt(clave, segundos).apply()
                        if (segundos >= 10) {
                            videosOficiales.entries.firstOrNull { it.value.id == video.id }?.key?.let {
                                marcarVideoVisto(prefs, it)
                            }
                        }
                    }
                }
            }
            delay(1000)
            if (!listo && ++espera >= 15) error = true
        }
    }

    key(video.id, intento) {
        AndroidView(
            modifier = Modifier.fillMaxWidth().heightIn(min = 205.dp).aspectRatio(16f / 9f),
            factory = { ctx ->
                val origen = "https://${ctx.packageName}"
                val inicio = prefs.getInt(clave, 0).coerceAtLeast(0)
                WebView(ctx).apply {
                    settings.javaScriptEnabled = true
                    settings.allowFileAccess = false
                    settings.allowContentAccess = false
                    settings.mediaPlaybackRequiresUserGesture = true
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean =
                            request.isForMainFrame && request.url.host != ctx.packageName
                    }
                    loadDataWithBaseURL(origen, paginaVideo(video.id, origen, inicio), "text/html", "UTF-8", null)
                    web = this
                }
            },
            onRelease = {
                if (web === it) web = null
                it.destroy()
            }
        )
    }
    if (error) {
        Text("YouTube no pudo cargar el video dentro de la aplicación.", color = MaterialTheme.colorScheme.error)
        Button(onClick = { intento++ }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Reintentar") }
    } else if (!listo) {
        LinearProgressIndicator(Modifier.fillMaxWidth())
    }
    if (onDemo != null) OutlinedButton(onClick = onDemo, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
        Text("Ver cápsula académica")
    }
}

private fun paginaVideo(id: String, origen: String, inicio: Int) = """
<!doctype html><html lang="es"><head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="referrer" content="strict-origin-when-cross-origin">
<style>html,body,#player{margin:0;width:100%;height:100%;background:#111}</style>
</head><body><div id="player"></div>
<script>
var player, mmReady=false, mmError=0;
function onYouTubeIframeAPIReady() {
  player = new YT.Player('player',{
    width:'100%',height:'100%',videoId:'$id',
    playerVars:{origin:'$origen',start:$inicio,playsinline:1,rel:0,hl:'es',cc_lang_pref:'es'},
    events:{onReady:function(){mmReady=true;},onError:function(e){mmError=e.data;}}
  });
}
window.mmStatus=function(){
  if (!mmReady) return [false,mmError,-1];
  return [true,mmError,player.getPlayerState()===0 ? 0 : Math.floor(player.getCurrentTime())];
};
</script><script src="https://www.youtube.com/iframe_api"></script></body></html>
""".trimIndent()
