package com.example.proyectomartini.data.repository
import android.content.Context
import com.example.proyectomartini.data.remote.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class ServiciosRepository(context:Context) {
 private val auth=AuthRepository(context)
 val api:ServiciosApi=RetrofitClient.crear { auth.obtenerToken() }.create(ServiciosApi::class.java)
 private val externa=Retrofit.Builder().baseUrl("https://www.themealdb.com/api/json/v1/1/").addConverterFactory(GsonConverterFactory.create()).build().create(RecetasApi::class.java)
 suspend fun postres()=externa.postres().meals.orEmpty()
}
