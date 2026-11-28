package com.example.proyectomartini

import android.app.Application
import com.example.proyectomartini.data.repository.AuthRepository
import com.example.proyectomartini.data.repository.ResultadoAuth
import com.example.proyectomartini.viewmodel.RegisterViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
    private val auth = mockk<AuthRepository>(relaxUnitFun = true)
    private val app = mockk<Application>()

    @Test fun formularioVacioPermaneceEnPrimerPaso() {
        val registro = RegisterViewModel(app, auth)
        registro.continuar()
        assertEquals(0, registro.uiState.value.paso)
        assertTrue(registro.uiState.value.errores.keys.containsAll(listOf("nombre", "correo", "password")))
    }

    @Test fun registroSinCamposDelNegocioYConInteresesOpcionales() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val registro = registroValido()
            registro.continuar()
            assertEquals(1, registro.uiState.value.paso)
            coEvery { auth.registrar("Ana Demo", "ana@example.test", "Prueba2026!", any()) } returns
                ResultadoAuth.Exito("Ana Demo", "ana@example.test")
            registro.continuar()
            advanceUntilIdle()
            assertTrue(registro.uiState.value.terminado)
            assertTrue(registro.uiState.value.password.isEmpty())
            coVerify(exactly = 1) {
                auth.registrar("Ana Demo", "ana@example.test", "Prueba2026!",
                    match { it.intereses.isEmpty() && it.tipoNegocio.isEmpty() && it.telefono.isEmpty() })
            }
            verify(exactly = 1) { auth.cerrarSesion() }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test fun errorDelServidorConservaLosInteresesYPermiteReintentar() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val registro = registroValido()
            registro.continuar()
            registro.modificar { it.copy(perfil = it.perfil.copy(intereses = "Galletas, Heladería")) }
            coEvery { auth.registrar(any(), any(), any(), any()) } returns ResultadoAuth.Error("Sin conexión")
            registro.continuar()
            advanceUntilIdle()
            assertEquals("Galletas, Heladería", registro.uiState.value.perfil.intereses)
            assertEquals("Sin conexión", registro.uiState.value.mensaje)
            assertFalse(registro.uiState.value.cargando)
            registro.volver()
            assertEquals(0, registro.uiState.value.paso)
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun registroValido() = RegisterViewModel(app, auth).apply {
        modificar { it.copy(
            perfil = it.perfil.copy(nombre = "Ana Demo", correo = "ana@example.test"),
            password = "Prueba2026!", confirmacion = "Prueba2026!"
        ) }
    }
}
