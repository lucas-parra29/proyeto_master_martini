package com.example.proyectomartini.ui.navigation

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectomartini.ui.screens.ExploreScreen
import com.example.proyectomartini.ui.screens.FavoritesScreen
import com.example.proyectomartini.ui.screens.HomeScreen
import com.example.proyectomartini.ui.screens.ProfileScreen
import com.example.proyectomartini.ui.theme.VerdePino
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.proyectomartini.ui.screens.RegisterScreen
import com.example.proyectomartini.ui.screens.EditProfileScreen
import com.example.proyectomartini.ui.screens.LoginScreen

sealed class MartiniScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Login : MartiniScreen(
        route = "login",
        title = "Iniciar sesión",
        icon = Icons.Default.Person
    )

    object Home : MartiniScreen(
        route = "home",
        title = "Inicio",
        icon = Icons.Default.Home
    )

    object Explore : MartiniScreen(
        route = "explore",
        title = "Explorar",
        icon = Icons.Default.Search
    )

    object Favorites : MartiniScreen(
        route = "favorites",
        title = "Favoritos",
        icon = Icons.Default.Favorite
    )

    object Profile : MartiniScreen(
        route = "profile",
        title = "Perfil",
        icon = Icons.Default.Person
    )
    object Register : MartiniScreen(
        route = "register",
        title = "Registro",
        icon = Icons.Default.Person
    )
    object EditProfile : MartiniScreen(
        route = "edit_profile",
        title = "Editar perfil",
        icon = Icons.Default.Person
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {

            if (
                currentRoute != MartiniScreen.Login.route &&
                currentRoute != MartiniScreen.Register.route &&
                currentRoute != MartiniScreen.EditProfile.route
            ) {

                NavigationBar {

                    items.forEach { screen ->

                        NavigationBarItem(
                            selected = currentRoute == screen.route,

                            onClick = {

                                if (currentRoute != screen.route) {

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
                                    imageVector = screen.icon,
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

                    onFavoritesClick = {
                        navController.navigate(
                            MartiniScreen.Favorites.route
                        )
                    }
                )
            }

            composable(MartiniScreen.Explore.route) {
                ExploreScreen()
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

