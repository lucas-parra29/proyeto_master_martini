package com.example.proyectomartini.utils

import java.net.URI
import java.net.URLEncoder

object VideoMartini {
    fun idValido(id: String): Boolean = Regex("^[A-Za-z0-9_-]{11}$").matches(id)
    fun embed(id: String): String { require(idValido(id)); return "https://www.youtube.com/embed/$id?playsinline=1&controls=1&fs=0&rel=0" }
    fun pagina(id: String): String { require(idValido(id)); return "https://m.youtube.com/watch?v=$id" }
    fun buscar(texto: String): String = "https://m.youtube.com/results?search_query=" + URLEncoder.encode(texto, "UTF-8")
    fun navegacionPermitida(url: String): Boolean = try {
        val uri = URI(url)
        val host = uri.host?.lowercase(java.util.Locale.ROOT).orEmpty()
        uri.scheme.equals("https", true) && uri.userInfo == null && (uri.port == -1 || uri.port == 443) &&
            (host == "youtube.com" || host.endsWith(".youtube.com") || host == "consent.google.com")
    } catch (_: Exception) { false }
}
