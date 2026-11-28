package com.example.proyectomartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomartini.data.remote.*
import com.example.proyectomartini.data.repository.ServiciosRepository
import com.example.proyectomartini.utils.Permisos
import com.example.proyectomartini.utils.ValidadorFormulario
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class ServiciosEstado(val perfil:PerfilRemoto?=null,val contenidos:List<ContenidoRemoto> = emptyList(),val cuentas:List<CuentaRemota> = emptyList(),val postres:List<RecetaExterna> = emptyList(),val ocupado:Boolean=false,val mensaje:String="",val codigo:String="",val eliminado:Boolean=false,val resetOk:Boolean=false)
class ServiciosViewModel(app:Application):AndroidViewModel(app){
 private val repo=ServiciosRepository(app)
 private val _estado=MutableStateFlow(ServiciosEstado())
 val estado=_estado.asStateFlow()
 private fun ejecutar(block:suspend ()->Unit){
  if(_estado.value.ocupado)return
  _estado.update{it.copy(ocupado=true,mensaje="")}
  viewModelScope.launch {
   try{block()}catch(e:CancellationException){throw e}catch(e:HttpException){_estado.update{it.copy(mensaje=when(e.code()){401->"Sesión vencida o credenciales incorrectas";403->"No tienes permiso para esta acción";else->"Error del servidor (${e.code()})"})}}catch(e:Exception){_estado.update{it.copy(mensaje="No se pudo completar la solicitud. Revisa tu conexión.")}}finally{_estado.update{it.copy(ocupado=false)}}
  }
 }
 private suspend fun actualizar(){val perfil=repo.api.perfil();val contenidos=repo.api.contenidos();val cuentas=if(Permisos.puedeAdministrar(perfil.rol))repo.api.cuentas() else emptyList();_estado.update{it.copy(perfil=perfil,contenidos=contenidos,cuentas=cuentas)}}
 fun cargar()=ejecutar{actualizar()}
 fun postres()=ejecutar{val lista=repo.postres();_estado.update{it.copy(postres=lista,mensaje=if(lista.isEmpty())"No hay resultados" else "Postres cargados desde TheMealDB")}}
 fun guardar(id:Long?,titulo:String,tipo:String,descripcion:String){
  val error=Permisos.validarContenido(titulo,descripcion)
  if(error!=null){_estado.update{it.copy(mensaje=error)};return}
  ejecutar{val entrada=EntradaContenido(titulo.trim(),tipo,descripcion.trim());if(id==null)repo.api.crear(entrada) else repo.api.editarContenido(id,entrada);actualizar();_estado.update{it.copy(mensaje="Contenido guardado")}}
 }
 fun borrar(id:Long)=ejecutar{repo.api.borrarContenido(id);actualizar();_estado.update{it.copy(mensaje="Contenido eliminado")}}
 fun cambiarRol(id:Long,rol:String)=ejecutar{repo.api.rol(id,CambioRol(rol));actualizar();_estado.update{it.copy(mensaje="Rol actualizado")}}
 fun generarCodigo()=ejecutar{val c=repo.api.codigo()["codigo"].orEmpty();_estado.update{it.copy(codigo=c)}}
 fun borrarCuenta()=ejecutar{repo.api.borrarCuenta();_estado.update{it.copy(eliminado=true)}}
 fun reset(correo:String,codigo:String,password:String,confirmar:String){
  val error=ValidadorFormulario.validarCorreo(correo)?:ValidadorFormulario.validarPasswordRegistro(password)?:ValidadorFormulario.validarConfirmarPassword(password,confirmar)?:if(codigo.isBlank())"Ingresa tu código de recuperación" else null
  if(error!=null){_estado.update{it.copy(mensaje=error)};return}
  ejecutar{repo.api.reset(ResetRequest(correo.trim(),codigo.trim(),password));_estado.update{it.copy(resetOk=true,mensaje="Contraseña actualizada. Vuelve al inicio de sesión.")}}
 }
}
