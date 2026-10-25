package com.example.proyectomartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomartini.data.local.FavoritoEntity
import com.example.proyectomartini.data.local.MartiniDatabase
import com.example.proyectomartini.data.repository.FavoritoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = FavoritoRepository(
        MartiniDatabase.getInstance(app).favoritoDao()
    )

    val favoritos: StateFlow<List<FavoritoEntity>> = repo.favoritos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val titulosFavoritos: StateFlow<Set<String>> = favoritos
        .map { lista -> lista.map { it.titulo }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun alternar(favorito: FavoritoEntity) {
        viewModelScope.launch {
            if (favorito.titulo in titulosFavoritos.value) repo.quitar(favorito.titulo)
            else repo.agregar(favorito)
        }
    }

    fun quitar(titulo: String) {
        viewModelScope.launch { repo.quitar(titulo) }
    }
}