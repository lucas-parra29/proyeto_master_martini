package com.example.proyectomartini
import com.example.proyectomartini.data.local.FavoritoDao
import com.example.proyectomartini.data.local.FavoritoEntity
import com.example.proyectomartini.data.repository.FavoritoRepository
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
class FavoritoRepositoryTest {
    private val dao = mockk<FavoritoDao>(relaxed=true)
    private val item = FavoritoEntity("Tarta", "RECETA", "20 min", "Inicial")
    @Test fun lectura() = runTest { every { dao.observarTodos() } returns flowOf(listOf(item)); assertEquals(listOf(item), FavoritoRepository(dao).favoritos.first()) }
    @Test fun guardar() = runTest { FavoritoRepository(dao).agregar(item); coVerify(exactly=1) { dao.insertar(item) } }
    @Test fun alternar() = runTest { FavoritoRepository(dao).alternar(item); coVerify(exactly=1) { dao.alternar(item) } }
    @Test fun eliminar() = runTest { FavoritoRepository(dao).quitar(item.titulo); coVerify(exactly=1) { dao.eliminar(item.titulo) } }
}
