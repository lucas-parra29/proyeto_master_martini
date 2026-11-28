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
1. git clone https://github.com/lucas-parra29/proyeto_master_martini
2. Abrir en Android Studio y esperar el sync de Gradle.
3. Ejecutar en un emulador o dispositivo con Android 7.0 (API 24) o superior y conexión a internet (para los videos).

## Ampliación EFT (copia local, sin compilar)
- Backend original: https://github.com/daniel-duoc/Backend-master-martini. Usa la carpeta Backend-master-martini incluida en este ZIP para las rutas ampliadas. El repositorio en línea no contiene necesariamente estos cambios.
- Debug usa `http://10.0.2.2:8080/` para el emulador. Para desarrollo por USB, usa `adb reverse tcp:8080 tcp:8080` y compila debug con `-PmartiniDebugApiUrl=http://127.0.0.1:8080/`. Release exige el dominio HTTPS del backend.
- Perfil remoto: GET/PUT/DELETE /api/me, recuperación: POST /api/me/recovery y /api/auth/reset. Gestión de contenidos y roles: /api/contenidos, /api/users (ver README backend).
- API pública Retrofit: https://www.themealdb.com/api/json/v1/1/filter.php?c=Dessert, desde Perfil > Cuenta y contenidos en línea > Consultar postres. No comparte token con la API externa.
- El código de recuperación se genera con la sesión abierta y se guarda de forma privada; no se envía por correo. Los favoritos, foto y recordatorios siguen locales en el dispositivo.
- Pruebas: `gradlew.bat :app:testDebugUnitTest` y `gradlew.bat :app:createDebugUnitTestCoverageReport` (comprueba nombre de tarea con `:app:tasks --all` si tu versión cambia). Se conservan tests anteriores, se agregan 32 casos JUnit y 3 tests MockK del repositorio Room. Se ejecutaron únicamente 32 comprobaciones JVM independientes en este entorno, no la suite JUnit/MockK ni la cobertura. Informe: carpeta evidencias.
- Abre el proyecto en Android Studio con SDK 37 y el JDK de Gradle indicado. Ejecuta `iniciar-pruebas-y-apk.ps1` para probar la lógica local. Ejecuta `generar-apk-aws.ps1 -BackendUrl <URL_HTTPS_REAL>` para validar la conexión, generar el APK firmado y verificarlo. El script pide la contraseña de la llave académica de forma oculta. El resultado se guarda en `app/build/outputs/apk/release/`.


## Versión 1.3
Consulta `../REVISION_V1.3.md` para ver el rediseño, ocho cápsulas MP4 locales, progreso por cuenta y pasos de verificación. No se necesita YouTube para las nuevas cápsulas. Las recetas, cursos, imágenes y videos son material académico ilustrativo no certificado por la marca. Se mantiene la URL de backend para emulador. El APK 1.3 debe compilarse y firmarse localmente.

## Versión 1.4
Inicio compacto, paleta corporativa de referencia, checklist de preparación, cursos favoritos, progreso, medallas y constancias PDF académicas. Consultar REVISION_V1.4.md en la raíz del ZIP.
