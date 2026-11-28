package com.example.proyectomartini.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomartini.viewmodel.ServiciosViewModel
import com.example.proyectomartini.utils.Permisos

@Composable
fun ServicesScreen(onBack:()->Unit,onDeleted:()->Unit,vm:ServiciosViewModel=viewModel()){
 val e by vm.estado.collectAsState()
 var titulo by remember{mutableStateOf("")};var descripcion by remember{mutableStateOf("")};var tipo by remember{mutableStateOf("RECETA")};var id by remember{mutableStateOf<Long?>(null)}
 var confirmar by remember{mutableStateOf(false)};var borrar by remember{mutableStateOf<Long?>(null)}
 LaunchedEffect(Unit){vm.cargar()};LaunchedEffect(e.eliminado){if(e.eliminado)onDeleted()}
 val rol=e.perfil?.rol.orEmpty();val tipos=Permisos.tipos.filter{Permisos.puedeEditar(rol,it)}
 LaunchedEffect(rol){if(tipo !in tipos && tipos.isNotEmpty())tipo=tipos.first()}
 LaunchedEffect(e.mensaje){if(e.mensaje=="Contenido guardado" || e.mensaje=="Contenido eliminado"){id=null;titulo="";descripcion=""}}
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  TextButton(onClick=onBack){Text("Volver al perfil")};Text("Cuenta y contenidos",style=MaterialTheme.typography.headlineMedium)
  AnimatedVisibility(e.ocupado){LinearProgressIndicator(Modifier.fillMaxWidth())}
  if(e.mensaje.isNotBlank())Text(e.mensaje)
  OutlinedButton(onClick=vm::cargar,enabled=!e.ocupado){Text("Actualizar datos")}
  e.perfil?.let { Text("${it.nombre} · ${it.rol}");Text(it.correo) }
  if(e.perfil!=null){
   OutlinedButton(onClick=vm::generarCodigo,enabled=!e.ocupado){Text("Generar código de recuperación")}
   if(e.codigo.isNotBlank())Text("Guarda este código en un lugar privado. Solo sirve una vez: ${e.codigo}")
   OutlinedButton(onClick={confirmar=true},enabled=!e.ocupado){Text("Eliminar mi cuenta")}
  }
  HorizontalDivider();Text("Contenidos de la comunidad",style=MaterialTheme.typography.titleLarge)
  if(tipos.isNotEmpty()){
   Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)){tipos.forEach{t->FilterChip(selected=tipo==t,onClick={tipo=t},label={Text(t)})}}
   OutlinedTextField(titulo,{titulo=it},label={Text("Título")},modifier=Modifier.fillMaxWidth())
   OutlinedTextField(descripcion,{descripcion=it},label={Text("Descripción")},modifier=Modifier.fillMaxWidth())
   Button(onClick={vm.guardar(id,titulo,tipo,descripcion)},enabled=!e.ocupado){Text(if(id==null)"Crear contenido" else "Guardar edición")}
   if(id!=null)TextButton(onClick={id=null;titulo="";descripcion=""}){Text("Cancelar edición")}
  }
  e.contenidos.forEach{c->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp)){
   Text(c.titulo,style=MaterialTheme.typography.titleMedium);Text(c.tipo);Text(c.descripcion)
   if(Permisos.puedeEditar(rol,c.tipo)&&(rol=="ADMIN"||c.autor==e.perfil?.correo))Row{
    TextButton(onClick={id=c.id;titulo=c.titulo;tipo=c.tipo;descripcion=c.descripcion},enabled=!e.ocupado){Text("Editar")};TextButton(onClick={borrar=c.id},enabled=!e.ocupado){Text("Eliminar")}
   }
  }}}
  if(rol=="ADMIN"){
   HorizontalDivider();Text("Administrar roles",style=MaterialTheme.typography.titleLarge)
   e.cuentas.filter{it.correo!=e.perfil?.correo}.forEach{c->Text("${c.nombre} (${c.correo}) · ${c.rol}");Permisos.roles.chunked(2).forEach{grupo->Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){grupo.forEach{r->FilterChip(selected=c.rol==r,onClick={vm.cambiarRol(c.id,r)},enabled=!e.ocupado,label={Text(r)})}}}}
  }
  HorizontalDivider();Text("Inspiración: postres internacionales",style=MaterialTheme.typography.titleLarge);Text("API pública TheMealDB")
  Button(onClick=vm::postres,enabled=!e.ocupado){Text("Consultar postres")};e.postres.forEach{Text("• ${it.strMeal}")}
 }
 if(confirmar)AlertDialog(onDismissRequest={confirmar=false},title={Text("Eliminar cuenta")},text={Text("Esta acción es permanente.")},confirmButton={TextButton(onClick={confirmar=false;vm.borrarCuenta()}){Text("Eliminar")}},dismissButton={TextButton(onClick={confirmar=false}){Text("Cancelar")}})
 borrar?.let{objetivo->AlertDialog(onDismissRequest={borrar=null},title={Text("Eliminar contenido")},text={Text("¿Confirmas la eliminación?")},confirmButton={TextButton(onClick={borrar=null;vm.borrar(objetivo)}){Text("Eliminar")}},dismissButton={TextButton(onClick={borrar=null}){Text("Cancelar")}})}
}
@Composable
fun RecoveryScreen(onBack:()->Unit,vm:ServiciosViewModel=viewModel()){
 val e by vm.estado.collectAsState();var correo by remember{mutableStateOf("")};var codigo by remember{mutableStateOf("")};var password by remember{mutableStateOf("")};var repetir by remember{mutableStateOf("")}
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  TextButton(onClick=onBack){Text("Volver")};Text("Recuperar contraseña",style=MaterialTheme.typography.headlineMedium)
  Text("Usa el código que guardaste desde tu perfil. No se envían contraseñas por correo.")
  OutlinedTextField(correo,{correo=it},label={Text("Correo")},modifier=Modifier.fillMaxWidth())
  OutlinedTextField(codigo,{codigo=it},label={Text("Código de recuperación")},modifier=Modifier.fillMaxWidth())
  OutlinedTextField(password,{password=it},label={Text("Nueva contraseña")},visualTransformation=PasswordVisualTransformation(),modifier=Modifier.fillMaxWidth())
  OutlinedTextField(repetir,{repetir=it},label={Text("Confirmar contraseña")},visualTransformation=PasswordVisualTransformation(),modifier=Modifier.fillMaxWidth())
  if(e.mensaje.isNotBlank())Text(e.mensaje,color=if(e.resetOk)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
  AnimatedVisibility(e.ocupado){LinearProgressIndicator(Modifier.fillMaxWidth())}
  Button(onClick={vm.reset(correo,codigo,password,repetir)},enabled=!e.ocupado&&!e.resetOk){Text("Cambiar contraseña")}
 }
}
