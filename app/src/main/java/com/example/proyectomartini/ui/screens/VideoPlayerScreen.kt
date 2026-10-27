
package com.example.proyectomartini.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

private val VerdeVideo = Color(0xFF174D3D)

private fun Context.obtenerActivity(): Activity? {
    var actual: Context = this
    while (actual is ContextWrapper) {
        if (actual is Activity) return actual
        actual = actual.baseContext
    }
    return null
}

private class MartiniWebChromeClient(
    private val activity: Activity?,
    private val onFullscreen: (Boolean) -> Unit
) : WebChromeClient() {

    private var fullscreenView: View? = null
    private var fullscreenCallback: CustomViewCallback? = null
    private var previousSystemUiVisibility = 0

    override fun onShowCustomView(
        view: View?,
        callback: CustomViewCallback?
    ) {
        if (activity == null || view == null) {
            callback?.onCustomViewHidden()
            return
        }

        if (fullscreenView != null) {
            callback?.onCustomViewHidden()
            return
        }

        val decor = activity.window.decorView as ViewGroup
        previousSystemUiVisibility = decor.systemUiVisibility

        fullscreenView = view
        fullscreenCallback = callback

        (view.parent as? ViewGroup)?.removeView(view)

        decor.addView(
            view,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        view.bringToFront()

        decor.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        onFullscreen(true)
    }

    override fun onHideCustomView() {
        val view = fullscreenView ?: return

        val decor = activity?.window?.decorView as? ViewGroup
        decor?.removeView(view)
        decor?.systemUiVisibility = previousSystemUiVisibility

        fullscreenView = null
        fullscreenCallback?.onCustomViewHidden()
        fullscreenCallback = null

        onFullscreen(false)
    }

    fun cerrarFullscreen() {
        onHideCustomView()
    }
}

@Composable
fun VideoPlayerScreen(
    videoId: String,
    titulo: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val activity = remember(context) {
        context.obtenerActivity()
    }

    val idValido = remember(videoId) {
        Regex("^[A-Za-z0-9_-]{11}$").matches(videoId)
    }

    var fullscreen by remember {
        mutableStateOf(false)
    }

    val chromeClient = remember(activity) {
        MartiniWebChromeClient(activity) {
            fullscreen = it
        }
    }

    BackHandler(enabled = fullscreen) {
        chromeClient.cerrarFullscreen()
    }

    fun abrirEnYouTube() {
        if (!idValido) return

        val url = Uri.parse(
            "https://www.youtube.com/watch?v=$videoId"
        )

        try {
            val customTab = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()

            customTab.launchUrl(context, url)
        } catch (_: Exception) {
            try {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, url)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8F5))
            .verticalScroll(rememberScrollState())
    ) {

        TextButton(onClick = onBackClick) {
            Text(
                "← Volver a Explorar",
                color = VerdeVideo
            )
        }

        Text(
            text = titulo,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeVideo,
            modifier = Modifier.padding(16.dp)
        )

        if (idValido) {

            key(videoId) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),

                    factory = { ctx ->
                        WebView(ctx).apply {
                            setLayerType(View.LAYER_TYPE_HARDWARE, null)
                            setBackgroundColor(
                                android.graphics.Color.BLACK
                            )

                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.mediaPlaybackRequiresUserGesture = true

                            webViewClient = WebViewClient()
                            webChromeClient = chromeClient

                            val videoUrl =
                                "https://www.youtube.com/embed/$videoId" +
                                        "?playsinline=1&controls=1&fs=1"

                            // Identificar Master Martini ante YouTube
                            loadUrl(
                                videoUrl,
                                mapOf(
                                    "Referer" to
                                            "https://${ctx.packageName}/"
                                )
                            )
                        }
                    },

                    onRelease = { webView ->
                        chromeClient.cerrarFullscreen()
                        webView.stopLoading()
                        webView.loadUrl("about:blank")
                        webView.destroy()
                    }
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Reproduce el video directamente aquí o ábrelo en YouTube.",
                color = Color.DarkGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = { abrirEnYouTube() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeVideo
                )
            ) {
                Text("▶ Abrir en YouTube")
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Si YouTube no permite reproducir el video dentro de la aplicación, utiliza el botón para abrirlo externamente.",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            Text(
                text = "El video seleccionado no es válido.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
