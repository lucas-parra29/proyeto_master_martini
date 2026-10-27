# Caso Master Martini

Aplicación móvil Android para **Master Martini Chile SPA**: plataforma de cursos, recetas y videos técnicos para clientes y prospectos del rubro gastronómico.

Proyecto académico (MVP) de **DSY1105 Desarrollo de Aplicaciones Móviles**, Duoc UC. Evaluación Parcial 2.

## Integrantes
- Daniel Parada
- Lucas Parra
- Sebastian Monsalve

## Problema
Los videos se envían por WhatsApp y YouTube sin constancia, y no hay información segmentada de los usuarios. La app centraliza el contenido con una interfaz simple para usuarios con bajo manejo digital.

## Funcionalidades
- Login y registro con validaciones por campo
- Exploración de contenidos por tipo (cursos, recetas, videos)
- Reproducción de video dentro de la app
- Detalle de recetas
- Favoritos con persistencia local (Room)
- Perfil de usuario editable, con foto desde cámara o galería
- Recordatorios con notificaciones
- Animaciones de retroalimentación

## Arquitectura
MVVM: `ui/` (pantallas Compose), `viewmodel/` (estado y validaciones), `data/local` (Room) y `data/repository`.

## Recursos nativos
Cámara, galería y notificaciones.

## Tecnologías
Kotlin, Jetpack Compose, Room, Navigation Compose.

## Usuario de prueba
Correo `demo@martini.cl` / contraseña `martini123` (datos ficticios).

## Cómo ejecutar
1. git clone https://github.com/lucas-parra29/proyecto_master_martini.git
2. Abrir en Android Studio y esperar el sync de Gradle.
3. Ejecutar en un emulador o dispositivo con Android 7.0 (API 24) o superior y conexión a internet (para los videos).
