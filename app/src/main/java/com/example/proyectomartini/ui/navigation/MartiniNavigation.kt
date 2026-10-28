package com.example.proyectomartini.ui.navigation

import android.net.Uri
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
import com.example.proyectomartini.ui.screens.LoginScreen
import com.example.proyectomartini.ui.screens.ProfileScreen
import com.example.proyectomartini.ui.screens.RecipeDetailScreen
import com.example.proyectomartini.ui.screens.RegisterScreen
import com.example.proyectomartini.ui.screens.VideoPlayerScreen
import com.example.proyectomartini.ui.theme.VerdePino

sealed class MartiniScreen(
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
}

@Composable
fun MartiniNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        MartiniScreen.Home,
        MartiniScreen.Explore,
        MartiniScreen.Favorites,
        MartiniScreen.Profile
    )

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

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

    val ocultarBarra = currentRoute in listOf(
        MartiniScreen.Login.route,
        MartiniScreen.Register.route,
        MartiniScreen.EditProfile.route,
        "courses",
        "video/{videoId}/{titulo}",
        "receta/{recetaId}"
    )

    Scaffold(
        bottomBar = {
            if (!ocultarBarra) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route ||
                                    (
                                            screen.route == MartiniScreen.Explore.route &&
                                                    currentRoute == "explore/{tipo}"
                                            ),

                            onClick = {
                                if (screen.route == MartiniScreen.Home.route) {
                                    volverAInicio()
                                } else if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(MartiniScreen.Home.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },

                            icon = {
                                Icon(
                                    screen.icon,
                                    contentDescription = screen.title
                                )
                            },

                            label = {
                                Text(screen.title)
                            },

                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VerdePino,
                                selectedTextColor = VerdePino
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MartiniScreen.Login.route,
            modifier = Modifier.padding(innerPadding)
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

                    onRegisterClick = {
                        navController.navigate(
                            MartiniScreen.Register.route
                        )
                    }
                )
            }

            composable(MartiniScreen.Home.route) {
                HomeScreen(
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
                    }
                )
            }

            composable(MartiniScreen.Explore.route) {
                ExploreScreen(
                    tipoInicial = "Todas",

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
                    onBackClick = { volverAInicio() }
                )
            }


            composable("video/{videoId}/{titulo}") { backStackEntry ->
                val videoId = backStackEntry.arguments
                    ?.getString("videoId") ?: ""

                val titulo = backStackEntry.arguments
                    ?.getString("titulo") ?: "Video"

                VideoPlayerScreen(
                    videoId = videoId,
                    titulo = titulo,
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
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }


            composable(MartiniScreen.Favorites.route) {
                FavoritesScreen()
            }


            composable(MartiniScreen.Profile.route) {
                ProfileScreen(
                    onEditProfileClick = {
                        navController.navigate(
                            MartiniScreen.EditProfile.route
                        )
                    },

                    onLogoutClick = {
                        navController.navigate(
                            MartiniScreen.Login.route
                        ) {
                            popUpTo(MartiniScreen.Home.route) {
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
