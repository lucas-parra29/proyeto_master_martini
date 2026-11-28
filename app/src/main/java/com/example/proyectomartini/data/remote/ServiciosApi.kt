package com.example.proyectomartini.data.remote
import com.example.proyectomartini.data.local.FavoritoCambio
import com.example.proyectomartini.data.local.FavoritoEntity
import retrofit2.http.*
data class PerfilRemoto(val nombre:String="", val correo:String="", val rol:String="CLIENTE", val telefono:String="", val tipoNegocio:String="", val intereses:String="", val nombreEmpresa:String="", val direccionEmpresa:String="", val cargoEmpresa:String="", val descripcionEmpresa:String="", val tamanoEmpresa:String?=null, val rutEmpresa:String?=null)
data class ContenidoRemoto(val id:Long, val titulo:String, val tipo:String, val descripcion:String, val autor:String)
data class EntradaContenido(val titulo:String,val tipo:String,val descripcion:String)
data class CuentaRemota(val id:Long,val nombre:String,val correo:String,val rol:String)
data class CambioRol(val rol:String)
data class ResetRequest(val correo:String,val codigo:String,val password:String)
interface ServiciosApi {
 @POST("api/me/aprendizaje/sincronizar")
 suspend fun sincronizarAvance(@Body body: AvanceRemoto): AvanceRemoto
 @GET("api/me") suspend fun perfil():PerfilRemoto
 @GET("api/me/favoritos") suspend fun favoritos():List<FavoritoEntity>
 @PUT("api/me/favoritos") suspend fun cambiarFavorito(@Body cambio:FavoritoCambio):Unit
 @POST("api/me/constancias/{curso}") suspend fun constancia(
     @Path("curso") curso: String, @Body evaluacion: EvaluacionRemota
 ): ConstanciaRemota
 @PUT("api/me") suspend fun editar(@Body perfil:PerfilRemoto):PerfilRemoto
 @DELETE("api/me") suspend fun borrarCuenta():Unit
 @POST("api/me/recovery") suspend fun codigo():Map<String,String>
 @POST("api/auth/reset") suspend fun reset(@Body body:ResetRequest):Unit
 @GET("api/contenidos") suspend fun contenidos():List<ContenidoRemoto>
 @POST("api/contenidos") suspend fun crear(@Body body:EntradaContenido):ContenidoRemoto
 @PUT("api/contenidos/{id}") suspend fun editarContenido(@Path("id") id:Long,@Body body:EntradaContenido):ContenidoRemoto
 @DELETE("api/contenidos/{id}") suspend fun borrarContenido(@Path("id") id:Long):Unit
 @GET("api/users") suspend fun cuentas():List<CuentaRemota>
 @PUT("api/users/{id}/role") suspend fun rol(@Path("id") id:Long,@Body body:CambioRol):CuentaRemota
}
data class RecetaExterna(val idMeal:String,val strMeal:String)
data class RespuestaRecetas(val meals:List<RecetaExterna>?)
interface RecetasApi { @GET("filter.php") suspend fun postres(@Query("c") categoria:String="Dessert"):RespuestaRecetas }

data class AvanceRemoto(val lecciones: Set<String>)
data class ConstanciaRemota(val nombre: String, val fechaEmision: Long)
data class EvaluacionRemota(val respuestas: List<Int>? = null)
