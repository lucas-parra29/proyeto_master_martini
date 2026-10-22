package com.example.proyectomartini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proyectomartini.ui.navigation.MartiniNavigation
import com.example.proyectomartini.ui.theme.ProyectoMartiniTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProyectoMartiniTheme {
                MartiniNavigation()
            }
        }
    }
}