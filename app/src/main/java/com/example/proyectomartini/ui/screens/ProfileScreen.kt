
package com.example.proyectomartini.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.proyectomartini.data.repository.ProfileRepository
import com.example.proyectomartini.ui.theme.*

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    onCompletarPerfil: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onServicesClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onCoursesClick: () -> Unit = {},
    onConstanciaClick: (String) -> Unit = {},
    onAyudaClick: () -> Unit = {},
    favoritosViewModel: com.example.proyectomartini.viewmodel.FavoritosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val favoritos by favoritosViewModel.favoritos.collectAsState()
    val avance = leerAvanceAcademia(context)
    val logros = cursosMartini.filter { com.example.proyectomartini.utils.AcademiaMartini.cursoCompleto(it.id,it.lecciones.size,avance) }
    val recetasGuardadas = com.example.proyectomartini.utils.EstadisticasFavoritos.contarRecetas(favoritos.map { it.tipo })

    val repository = remember(context) {
        ProfileRepository(context)
    }

    var perfil by remember {
        mutableStateOf(repository.obtenerPerfil())
    }

    var actualizacion by remember {
        mutableIntStateOf(0)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                perfil = repository.obtenerPerfil()
                actualizacion++
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val foto = remember(perfil.foto, actualizacion) {
        if (perfil.foto.isNotEmpty()) {
            try {
                BitmapFactory.decodeFile(perfil.foto)
                    ?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    val auth = com.example.proyectomartini.data.repository.AuthRepository(context)
    val nombre = listOf(perfil.nombre, auth.nombreSesion()).firstOrNull {
        com.example.proyectomartini.utils.AcademiaMartini.nombreVisible(it, "").isNotBlank()
    }.orEmpty().ifBlank { "Chef Master Martini" }
    val rol = when (auth.rol()) {
        "ADMIN" -> "Administración"
        "EDITOR" -> "Editor"
        "INSTRUCTOR" -> "Instructor"
        else -> "Cliente emprendedor"
    }
    val videosVistos = preferenciasVideos(context).getStringSet("videos_vistos", emptySet()).orEmpty().size
    val categorias = logros.map { it.categoria }.toSet()
    val medallas = listOf(
        Triple("Primer paso", "Completa una lección", avance.isNotEmpty()),
        Triple("Chef del chocolate", "Completa un curso de chocolatería", "Chocolatería" in categorias),
        Triple("Maestro pastelero", "Completa un curso de pastelería", "Pastelería" in categorias),
        Triple("Experto en heladería", "Completa un curso de heladería", "Heladería" in categorias),
        Triple("Maestro de masas", "Completa un curso de panadería o masas",
            categorias.any { simboloMedalla(it) == 4 }),
        Triple("Especialista en galletas", "Completa un curso de galletas", "Galletas" in categorias),
        Triple("Especialista en empanadas", "Completa un curso de empanadas", "Empanadas" in categorias),
        Triple("Aprendiz constante", "Completa tres cursos", logros.size >= 3),
        Triple("Emprendedor gastronómico", "Completa tu perfil y termina dos cursos",
            logros.size >= 2 && com.example.proyectomartini.utils.PerfilCompleto
                .pendientes(perfil).isEmpty())
    )
    Column(Modifier.fillMaxSize().background(Marfil).verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().height(105.dp)) {
            Box(Modifier.matchParentSize().clip(RoundedCornerShape(bottomStart = 41.dp, bottomEnd = 82.dp))
                .background(Brush.horizontalGradient(listOf(VerdePino, Color(0xFF216952)))))
            Text("Mi perfil", Modifier.align(Alignment.TopStart).padding(18.dp),
                style = MaterialTheme.typography.headlineMedium, color = Blanco, fontWeight = FontWeight.ExtraBold)
            AccionCircular(Icons.Default.Settings, "Ayuda y tamaño del texto", onAyudaClick,
                Modifier.align(Alignment.TopEnd).padding(10.dp), fondo = VerdePino)
        }
        Column(Modifier.padding(horizontal = 15.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(13.dp)) {
                Surface(Modifier.size(88.dp), CircleShape, color = LineaCrema,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Papel)) {
                    if (foto != null) Image(foto, "Foto de perfil", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    else Icon(Icons.Default.Person, "Perfil sin fotografía", Modifier.padding(18.dp), tint = VerdePino)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(nombre, style = MaterialTheme.typography.titleLarge, color = VerdePino, fontWeight = FontWeight.ExtraBold)
                    EtiquetaMartini(rol)
                    OutlinedButton(onClick = onEditProfileClick, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                        Icon(Icons.Default.Edit, null, Modifier.size(17.dp)); Spacer(Modifier.width(6.dp)); Text("Editar perfil")
                    }
                }
            }
            Text("Mi progreso", style = MaterialTheme.typography.titleLarge, color = VerdePino, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TarjetaEstadistica(Modifier.weight(1f), Icons.Default.MenuBook, logros.size, "Cursos\ncompletados", VerdePino, onCoursesClick)
                TarjetaEstadistica(Modifier.weight(1f), Icons.Default.PlayCircleOutline, videosVistos, "Videos\nvistos", OroOscuro)
                TarjetaEstadistica(Modifier.weight(1f), Icons.Default.FavoriteBorder, recetasGuardadas, "Recetas\nguardadas", RojoCaoba, onFavoritesClick)
            }
            Text("Mis logros", style = MaterialTheme.typography.titleLarge, color = VerdePino, fontWeight = FontWeight.Bold)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                medallas.forEachIndexed { indice, (titulo, requisito, obtenida) ->
                    SuperficieEditorial(Modifier.width(127.dp)) {
                        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            MedallaEditorial(Modifier.width(66.dp), tono = if (!obtenida) 1 else if (indice == 0) 2 else 0,
                                simbolo = listOf(0, 3, 1, 2, 4, 5, 6, 0, 0)[indice])
                            Text(titulo, textAlign = TextAlign.Center, color = VerdePino,
                                style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Text(if (obtenida) "Obtenida" else requisito, textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall, color = GrisTexto)
                        }
                    }
                }
            }
            HorizontalDivider(color = LineaCrema)
            Text("Mis intereses", style = MaterialTheme.typography.titleLarge, color = VerdePino, fontWeight = FontWeight.Bold)
            val intereses = com.example.proyectomartini.utils.PerfilCompleto.intereses(perfil.intereses)
            if (intereses.isEmpty()) Text("Elige intereses desde Editar perfil.", color = GrisTexto)
            else Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                intereses.forEach { EtiquetaMartini("✓ $it") }
            }
            RecordatorioPerfil(perfil, onCompletarPerfil)
            SeccionMartini("Mis constancias") {
                if (logros.isEmpty()) Text("Completa un curso para obtener una constancia académica.")
                logros.forEach { curso ->
                    OutlinedButton(onClick = { onConstanciaClick(curso.id) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(curso.titulo) }
                }
            }
            Spacer(Modifier.height(6.dp))
            if (com.example.proyectomartini.utils.PerfilCompleto.tieneEmpresa(perfil)) {
                SeccionMartini("Mi emprendimiento") {
                    if (perfil.nombreEmpresa.isNotBlank()) Text(perfil.nombreEmpresa, style = MaterialTheme.typography.titleMedium)
                    if (perfil.tipoNegocio.isNotBlank()) EtiquetaMartini(perfil.tipoNegocio)
                    if (perfil.rutEmpresa.isNotBlank()) Text("RUT · ${perfil.rutEmpresa}")
                    if (perfil.direccionEmpresa.isNotBlank()) Text(perfil.direccionEmpresa)
                }
            }

            Text(
                text = "Información de contacto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdePino
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Blanco
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Correo electrónico",
                        fontWeight = FontWeight.Bold,
                        color = VerdePino
                    )

                    Text(
                        text = perfil.correo.ifBlank {
                            "Sin correo registrado"
                        },
                        color = GrisTexto
                    )

                    HorizontalDivider()

                    Text(
                        text = "Teléfono",
                        fontWeight = FontWeight.Bold,
                        color = VerdePino
                    )

                    Text(
                        text = perfil.telefono.ifBlank {
                            "Sin teléfono registrado"
                        },
                        color = GrisTexto
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onServicesClick, modifier = Modifier.fillMaxWidth()) { Text("Cuenta y contenidos en línea") }
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Cerrar sesión",
                    color = RojoCaoba,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TarjetaEstadistica(modifier: Modifier, icono: androidx.compose.ui.graphics.vector.ImageVector,
    numero: Int, nombre: String, color: Color, abrir: (() -> Unit)? = null) {
    val contenido: @Composable () -> Unit = {
        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(icono, null, Modifier.size(21.dp), tint = color)
            Text("$numero", fontWeight = FontWeight.ExtraBold, color = VerdePino, style = MaterialTheme.typography.titleLarge)
            Text(nombre, textAlign = TextAlign.Center, color = GrisTexto, style = MaterialTheme.typography.labelMedium)
        }
    }
    if (abrir == null) SuperficieEditorial(modifier, contenido = contenido)
    else Surface(onClick = abrir, modifier = modifier, color = Papel, shape = RoundedCornerShape(17.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, LineaCrema), shadowElevation = 2.dp, content = contenido)
}
