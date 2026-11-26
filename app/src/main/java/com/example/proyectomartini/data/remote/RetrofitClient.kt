package com.example.proyectomartini.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    const val BASE_URL = "http://54.196.190.80:8080/"

    fun crear(obtenerToken: () -> String?): Retrofit {
        val cliente = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor { cadena ->
                val token = obtenerToken()
                val peticion = if (token != null) {
                    cadena.request().newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    cadena.request()
                }
                cadena.proceed(peticion)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

