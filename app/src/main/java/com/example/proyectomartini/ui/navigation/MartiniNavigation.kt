package com.example.proyectomartini.ui.navigation

import android.net.Uri
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectomartini.ui.screens.Marfil
import com.example.proyectomartini.ui.screens.GorroChef
import com.example.proyectomartini.ui.theme.DoradoMartini
import com.example.proyectomartini.ui.theme.Blanco
import androidx.compose.ui.platform.LocalContext
import com.example.proyectomartini.data.repository.AuthRepository
import com.example.proyectomartini.ui.screens.ServicesScreen
import com.example.proyectomartini.ui.screens.RecoveryScreen
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectomartini.ui.screens.CoursesScreen
import com.example.proyectomartini.ui.screens.EditProfileScreen
import com.example.proyectomartini.ui.screens.ExploreScreen
import com.example.proyectomartini.ui.screens.FavoritesScreen
import com.example.proyectomartini.ui.screens.HomeScreen
import com.example.proyectomartini.ui.screens.AyudaMartini
import com.example.proyectomartini.ui.screens.LoginScreen
import com.example.proyectomartini.ui.screens.ProfileScreen
import com.example.proyectomartini.ui.screens.RecipeDetailScreen
import com.example.proyectomartini.ui.screens.RegisterScreen
import com.example.proyectomartini.ui.screens.VideoPlayerScreen
import com.example.proyectomartini.ui.theme.VerdePino
import androidx.compose.material.icons.filled.Notifications
import com.example.proyectomartini.ui.screens.NotificationsScreen

private open class MartiniScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Login : MartiniScreen(
        "login",
        "Iniciar sesión",
        Icons.Default.Person
    )

    object Home : MartiniScreen(
        "home",
        "Inicio",
        Icons.Default.Home
    )

    object Explore : MartiniScreen(
        "explore",
        "Explorar",
        Icons.Default.Search
    )

    object Favorites : MartiniScreen(
        "favorites",
        "Favoritos",
        Icons.Default.Favorite
    )

    object Profile : MartiniScreen(
        "profile",
        "Perfil",
        Icons.Default.Person
    )

    object Register : MartiniScreen(
        "register",
        "Registro",
        Icons.Default.Person
    )

    object EditProfile : MartiniScreen(
        "edit_profile",
        "Editar perfil",
        Icons.Default.Person
    )
    object Notifications : MartiniScreen(
        "notifications",
        "Notificaciones",
        Icons.Default.Notifications
    )
}

@Composable
fun MartiniNavigation(destinoAviso: String? = null, avisoConsumido: () -> Unit = {}) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val items = listOf(
        MartiniScreen("home", "Inicio", Icons.Outlined.Home),
        MartiniScreen("explore/RECETA", "Recetas", GorroChef),
        MartiniScreen("courses", "Cursos", Icons.Outlined.School),
        MartiniScreen("favorites", "Favoritos", Icons.Outlined.FavoriteBorder),
        MartiniScreen("profile", "Perfil", Icons.Outlined.Person)
    )

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(destinoAviso, currentRoute) {
        val destino = destinoAviso ?: return@LaunchedEffect
        if (currentRoute == null || runCatching { navController.getBackStackEntry(MartiniScreen.Home.route) }.isFailure) return@LaunchedEffect
        if (destino == "courses" || com.example.proyectomartini.ui.screens.recetasMartini.any { "receta/${it.id}" == destino }) {
            navController.navigate(destino) { launchSingleTop = true }
        }
        avisoConsumido()
    }

    // Regresa a la pantalla Inicio sin acumular copias en el historial.
    fun volverAInicio() {
        navController.navigate(MartiniScreen.Home.route) {
            popUpTo(MartiniScreen.Home.route) {
                inclusive = false
            }
            launchSingleTop = true
            restoreState = false
        }
    }

    val ocultarBarra = currentRoute == null || currentRoute in listOf(
        MartiniScreen.Login.route, MartiniScreen.Register.route,
        MartiniScreen.EditProfile.route, "edit_profile/{campo}", "recovery"
    )
    val activa = when (currentRoute) {
        "courses", "courses/{cursoId}?continuar={continuar}&constancia={constancia}" -> "courses"
        "favorites" -> "favorites"
        "profile", "ayuda", "services" -> "profile"
        "receta/{recetaId}", "explore" -> "explore/RECETA"
        "explore/{tipo}" -> if (navBackStackEntry?.arguments?.getString("tipo") == "VIDEO") "home" else "explore/RECETA"
        else -> "home"
    }

    Scaffold(
        containerColor = Marfil,
        bottomBar = {
            if (!ocultarBarra) {
                val forma = RoundedCornerShape(topStart = 21.dp, topEnd = 21.dp)
                Column(Modifier.fillMaxWidth().clip(forma).background(DoradoMartini)
                    .padding(top = 3.dp).clip(forma).background(Marfil).navigationBarsPadding()) {
                    Row(Modifier.fillMaxWidth().selectableGroup().padding(horizontal = 4.dp, vertical = 5.dp)) {
                        items.forEach { opcion ->
                            val seleccionada = activa == opcion.route
                            Column(
                                Modifier.weight(1f).heightIn(min = 56.dp).selectable(
                                    selected = seleccionada, role = Role.Tab,
                                    onClick = {
                                        if (opcion.route == "home") volverAInicio()
                                        else if (opcion.route != currentRoute) navController.navigate(opcion.route) {
                                            popUpTo(MartiniScreen.Home.route)
                                            launchSingleTop = true
                                        }
                                    }
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(Modifier.widthIn(min = 51.dp).height(32.dp)
                                    .background(if (seleccionada) VerdePino else Marfil, RoundedCornerShape(50)),
                                    contentAlignment = Alignment.Center) {
                                    Icon(opcion.icon, null, Modifier.size(24.dp), tint = if (seleccionada) Blanco else VerdePino)
                                }
                                Text(opcion.title, color = VerdePino,
                                    style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MartiniScreen.Login.route,
            enterTransition = { fadeIn(tween(150)) },
            exitTransition = { fadeOut(tween(110)) },
            popEnterTransition = { fadeIn(tween(150)) },
            popExitTransition = { fadeOut(tween(110)) },
            modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)
        ) {

            composable(MartiniScreen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(
                            MartiniScreen.Home.route
                        ) {
                            popUpTo(MartiniScreen.Login.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },

                    onRecoveryClick = { navController.navigate("recovery") },
                    onRegisterClick = {
                        navController.navigate(
                            MartiniScreen.Register.route
                        )
                    }
                )
            }

            composable("recovery") { RecoveryScreen(onBack={navController.popBackStack()}) }
            composable("ayuda") { AyudaMartini { navController.popBackStack() } }
            composable("services") { ServicesScreen(onBack={navController.popBackStack()}, onDeleted={
                AuthRepository(context).cerrarSesion()
                navController.navigate(MartiniScreen.Login.route) { popUpTo(navController.graph.id) { inclusive=true } }
            }) }
            composable(MartiniScreen.Home.route) {
                HomeScreen(
                    onCompletarPerfil = { navController.navigate("edit_profile/$it") },
                    onRecetaDestacada = { navController.navigate("receta/$it") },
                    onContinuarCurso = { navController.navigate("courses/$it?continuar=true") },
                    onExploreClick = {
                        navController.navigate(
                            MartiniScreen.Explore.route
                        )
                    },

                    onRecipesClick = {
                        navController.navigate("explore/RECETA")
                    },

                    onVideosClick = {
                        navController.navigate("explore/VIDEO")
                    },

                    onCoursesClick = {
                        navController.navigate("courses")
                    },

                    onFavoritesClick = {
                        navController.navigate(
                            MartiniScreen.Favorites.route
                        )
                    },
                    onNotificationsClick = {
                        navController.navigate(
                            MartiniScreen.Notifications.route
                        )
                    }
                )
            }

            composable(
                MartiniScreen.Notifications.route
            ) {

                NotificationsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onAbrirContenido = { destino -> navController.navigate(destino) { launchSingleTop = true } }
                )
            }

            composable(MartiniScreen.Explore.route) {
                ExploreScreen(
                    tipoInicial = "Todas",
                    onCursoClick = { navController.navigate("courses/$it") },

                    onInicioClick = { volverAInicio() },

                    onVideoClick = { videoId, titulo ->
                        navController.navigate(
                            "video/$videoId/${Uri.encode(titulo)}"
                        )
                    },

                    onRecetaClick = { recetaId ->
                        navController.navigate(
                            "receta/$recetaId"
                        )
                    }
                )
            }

            composable("explore/{tipo}") { backStackEntry ->
                val tipo = backStackEntry.arguments
                    ?.getString("tipo") ?: "Todas"

                ExploreScreen(
                    tipoInicial = tipo,
                    onCursoClick = { navController.navigate("courses/$it") },

                    onInicioClick = { volverAInicio() },

                    onVideoClick = { videoId, titulo ->
                        navController.navigate(
                            "video/$videoId/${Uri.encode(titulo)}"
                        )
                    },

                    onRecetaClick = { recetaId ->
                        navController.navigate(
                            "receta/$recetaId"
                        )
                    }
                )
            }

            composable("courses") {
                CoursesScreen(
                    onBackClick = { volverAInicio() },
                    onVideoClick = { id, titulo -> navController.navigate("video/$id/${Uri.encode(titulo)}") }
                )
            }


            composable(
                "courses/{cursoId}?continuar={continuar}&constancia={constancia}",
                arguments = listOf(androidx.navigation.navArgument("continuar") {
                    type = androidx.navigation.NavType.BoolType
                    defaultValue = false
                }, androidx.navigation.navArgument("constancia") {
                    type = androidx.navigation.NavType.BoolType
                    defaultValue = false
                })
            ) { entry ->
                CoursesScreen(onBackClick={navController.popBackStack()},cursoInicial=entry.arguments?.getString("cursoId"),continuarAlAbrir=entry.arguments?.getBoolean("continuar") == true,constanciaInicial=entry.arguments?.getBoolean("constancia") == true,onVideoClick={id,titulo -> navController.navigate("video/$id/${Uri.encode(titulo)}")})
            }
            composable("video/{videoId}/{titulo}") { backStackEntry ->
                val videoId = backStackEntry.arguments
                    ?.getString("videoId") ?: ""

                val titulo = backStackEntry.arguments
                    ?.getString("titulo") ?: "Video"

                VideoPlayerScreen(
                    videoId = videoId,
                    titulo = titulo,
                    onReceta = { navController.navigate("receta/$it") },
                    onCurso = { navController.navigate("courses/$it") },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }


            composable("receta/{recetaId}") { backStackEntry ->
                val recetaId = backStackEntry.arguments
                    ?.getString("recetaId") ?: ""

                RecipeDetailScreen(
                    recetaId = recetaId,
                    onVideoClick = { id, titulo -> navController.navigate("video/$id/${Uri.encode(titulo)}") },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }


            composable(MartiniScreen.Favorites.route) {
                FavoritesScreen(
                    onCursoClick = { navController.navigate("courses/$it") },
                    onRecetaClick = { navController.navigate("receta/$it") },
                    onVideoClick = { id, titulo -> navController.navigate("video/$id/${Uri.encode(titulo)}") }
                )
            }


            composable(MartiniScreen.Profile.route) {
                ProfileScreen(
                    onAyudaClick = { navController.navigate("ayuda") },
                    onConstanciaClick = { navController.navigate("courses/$it?constancia=true") },
                    onCompletarPerfil = { navController.navigate("edit_profile/$it") },
                    onCoursesClick = { navController.navigate("courses") },
                    onServicesClick = { navController.navigate("services") },
                    onFavoritesClick = { navController.navigate(MartiniScreen.Favorites.route) { launchSingleTop = true } },
                    onEditProfileClick = {
                        navController.navigate(
                            MartiniScreen.EditProfile.route
                        )
                    },

                    onLogoutClick = {
                        AuthRepository(context).cerrarSesion()
                        navController.navigate(
                            MartiniScreen.Login.route
                        ) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }


            composable(MartiniScreen.Register.route) {
                RegisterScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onLoginClick = {
                        navController.navigate(
                            MartiniScreen.Login.route
                        ) {
                            popUpTo(MartiniScreen.Register.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }


            composable("edit_profile/{campo}") { entry ->
                EditProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    campoInicial = entry.arguments?.getString("campo")
                )
            }
            composable(MartiniScreen.EditProfile.route) {
                EditProfileScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
