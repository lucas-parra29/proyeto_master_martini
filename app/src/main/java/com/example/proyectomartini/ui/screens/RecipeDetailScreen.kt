
package com.example.proyectomartini.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import com.example.proyectomartini.utils.CostosReceta
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectomartini.ui.theme.*
import com.example.proyectomartini.R

data class RecetaMartini(
    val id: String,
    val titulo: String,
    val categoria: String,
    val nivel: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    val imagenUrl: String
)

private fun receta(
    id: String,
    titulo: String,
    categoria: String,
    nivel: String,
    imagen: String,
    ingredientes: String,
    pasos: String
) = RecetaMartini(
    id = id,
    titulo = titulo,
    categoria = categoria,
    nivel = nivel,
    imagenUrl = imagen,
    ingredientes = ingredientes.split("|"),
    pasos = pasos.split("|")
)

private const val FOTO_CHOCOLATE =
    "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=600&q=80"

private const val FOTO_PASTEL =
    "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=600&q=80"

private const val FOTO_HELADO =
    "https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=600&q=80"

private const val FOTO_PAN =
    "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=600&q=80"

private const val FOTO_GALLETAS =
    "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=600&q=80"

private const val FOTO_EMPANADAS =
    "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600&q=80"

val recetasMartini = listOf(

    receta(
        "mousse",
        "Torta Mousse de Chocolate y Avellanas",
        "Chocolatería",
        "Intermedio",
        FOTO_CHOCOLATE,
        "200 g de chocolate semiamargo|300 ml de crema para batir|150 g de galletas de chocolate|70 g de mantequilla|100 g de avellanas tostadas|40 g de azúcar",
        "Tritura las galletas y mézclalas con la mantequilla derretida.|Forma una base en un molde y refrigera.|Derrite el chocolate a baño María y deja entibiar.|Bate la crema fría con el azúcar hasta conseguir una textura aireada.|Incorpora el chocolate y las avellanas con movimientos envolventes.|Vierte sobre la base y refrigera al menos cuatro horas.|Decora con avellanas y sirve."
    ),

    receta(
        "brownies",
        "Brownies de Chocolate",
        "Chocolatería",
        "Básico",
        FOTO_CHOCOLATE,
        "180 g de chocolate semiamargo|120 g de mantequilla|150 g de azúcar|3 huevos|100 g de harina|20 g de cacao|1 pizca de sal",
        "Precalienta el horno a 180 °C.|Derrite el chocolate junto con la mantequilla.|Bate los huevos con el azúcar e incorpora el chocolate tibio.|Agrega la harina, el cacao y la sal.|Vierte en un molde forrado con papel de hornear.|Hornea a 180 °C aproximadamente 20 a 25 minutos.|Deja enfriar antes de cortar."
    ),

    receta(
        "trufas",
        "Trufas de Chocolate y Cacao",
        "Chocolatería",
        "Básico",
        FOTO_CHOCOLATE,
        "200 g de chocolate semiamargo|100 ml de crema para batir|15 g de mantequilla|30 g de cacao amargo",
        "Pica el chocolate y colócalo en un recipiente.|Calienta la crema hasta que comience a humear y viértela sobre el chocolate.|Espera dos minutos y mezcla hasta integrar.|Agrega la mantequilla y refrigera hasta que la mezcla esté firme.|Forma pequeñas bolitas y pásalas por cacao amargo.|Conserva las trufas refrigeradas."
    ),



    receta(
        "torta-vainilla",
        "Torta de Vainilla y Crema",
        "Pastelería",
        "Intermedio",
        FOTO_PASTEL,
        "200 g de harina|160 g de azúcar|3 huevos|100 g de mantequilla|120 ml de leche|10 g de polvos de hornear|1 cucharadita de vainilla|300 ml de crema para batir",
        "Precalienta el horno a 180 °C.|Bate la mantequilla con el azúcar y agrega los huevos.|Incorpora la vainilla, la leche, la harina y los polvos de hornear.|Vierte en un molde engrasado y hornea aproximadamente 30 a 40 minutos, hasta que el centro esté cocido.|Enfría, desmolda y divide el bizcocho en capas.|Bate la crema fría y utiliza para rellenar y decorar."
    ),

    receta(
        "cheesecake",
        "Cheesecake de Frutos Rojos",
        "Pastelería",
        "Intermedio",
        FOTO_PASTEL,
        "200 g de galletas|90 g de mantequilla|500 g de queso crema|150 g de azúcar|3 huevos|150 g de yogur natural|150 g de frutos rojos",
        "Precalienta el horno a 160 °C.|Tritura las galletas con la mantequilla y forma una base.|Bate el queso crema con el azúcar y agrega los huevos de uno en uno.|Incorpora el yogur y vierte sobre la base.|Hornea aproximadamente 50 a 60 minutos hasta que los bordes estén firmes y el centro ligeramente tembloroso.|Deja enfriar, refrigera varias horas y decora con frutos rojos."
    ),

    receta(
        "cupcakes",
        "Cupcakes de Vainilla",
        "Pastelería",
        "Básico",
        FOTO_PASTEL,
        "180 g de harina|130 g de azúcar|100 g de mantequilla|2 huevos|120 ml de leche|8 g de polvos de hornear|1 cucharadita de vainilla|200 g de queso crema|80 g de azúcar flor",
        "Precalienta el horno a 180 °C.|Bate mantequilla con azúcar y agrega huevos y vainilla.|Incorpora la harina, los polvos de hornear y la leche.|Distribuye en cápsulas hasta dos tercios de su capacidad.|Hornea a 180 °C aproximadamente 18 a 22 minutos.|Deja enfriar antes de decorar.|Mezcla el queso crema con azúcar flor y decora los cupcakes."
    ),


    receta(
        "helado-vainilla",
        "Helado Casero de Vainilla",
        "Heladería",
        "Básico",
        FOTO_HELADO,
        "400 ml de crema para batir|300 g de leche condensada|2 cucharaditas de vainilla",
        "Bate la crema bien fría hasta que forme picos suaves.|Mezcla la leche condensada con la vainilla.|Incorpora suavemente la crema batida.|Coloca la mezcla en un recipiente con tapa.|Congela entre seis y ocho horas hasta que esté firme.|Deja reposar unos minutos antes de servir."
    ),

    receta(
        "helado-chocolate",
        "Helado Cremoso de Chocolate",
        "Heladería",
        "Básico",
        FOTO_HELADO,
        "400 ml de crema para batir|300 g de leche condensada|50 g de cacao amargo|80 g de chocolate picado",
        "Mezcla la leche condensada con el cacao hasta eliminar grumos.|Bate la crema fría hasta formar picos suaves.|Integra ambas preparaciones con movimientos envolventes.|Agrega el chocolate picado.|Congela en un recipiente tapado al menos seis horas.|Sirve cuando alcance una textura adecuada para formar bolas."
    ),

    receta(
        "sorbete-frutilla",
        "Sorbete de Frutilla",
        "Heladería",
        "Básico",
        FOTO_HELADO,
        "500 g de frutillas limpias|100 g de azúcar|100 ml de agua|2 cucharadas de jugo de limón",
        "Calienta el agua con el azúcar hasta disolver y deja enfriar.|Licúa las frutillas con el almíbar y el jugo de limón.|Cuela si deseas una textura más fina.|Congela en un recipiente poco profundo.|Revuelve cada 40 minutos durante las primeras horas para reducir cristales grandes.|Sirve cuando tenga consistencia de sorbete."
    ),



    receta(
        "rollos-canela",
        "Rollos de Canela",
        "Bollería",
        "Intermedio",
        FOTO_PAN,
        "500 g de harina|250 ml de leche|70 g de azúcar|70 g de mantequilla|7 g de levadura seca|1 huevo|50 g de azúcar rubia|15 g de canela",
        "Mezcla harina, levadura, leche, huevo y azúcar.|Amasa incorporando la mantequilla hasta obtener una masa suave.|Deja fermentar hasta que duplique su volumen.|Estira la masa, espolvorea azúcar rubia y canela y enrolla.|Corta porciones y deja reposar hasta que aumenten de tamaño.|Hornea a 180 °C durante aproximadamente 20 a 25 minutos."
    ),

    receta(
        "brioche",
        "Pan Brioche",
        "Bollería",
        "Avanzado",
        FOTO_PAN,
        "500 g de harina|70 g de azúcar|10 g de sal|7 g de levadura seca|4 huevos|100 ml de leche|180 g de mantequilla",
        "Mezcla la harina, azúcar, sal, levadura, huevos y leche.|Amasa hasta que la mezcla comience a desarrollar elasticidad.|Incorpora la mantequilla poco a poco y continúa amasando.|Deja fermentar y refrigera la masa para facilitar el formado.|Forma piezas en un molde y deja fermentar nuevamente.|Hornea a 180 °C hasta que esté bien dorado y cocido por dentro."
    ),

    receta(
        "bollos-leche",
        "Bollitos de Leche",
        "Bollería",
        "Básico",
        FOTO_PAN,
        "450 g de harina|220 ml de leche|60 g de azúcar|50 g de mantequilla|7 g de levadura seca|1 huevo|7 g de sal",
        "Mezcla todos los ingredientes excepto la mantequilla.|Amasa y agrega la mantequilla poco a poco.|Deja reposar hasta que duplique su volumen.|Divide en bollitos y coloca sobre una bandeja.|Deja fermentar hasta que estén esponjosos.|Hornea a 180 °C durante aproximadamente 15 a 20 minutos."
    ),


    receta(
        "milhojas",
        "Milhojas de Crema y Frutos Rojos",
        "Masas de hoja",
        "Intermedio",
        FOTO_PASTEL,
        "2 láminas de masa de hojaldre|500 ml de leche|4 yemas|100 g de azúcar|40 g de maicena|1 cucharadita de vainilla|200 g de frutos rojos",
        "Precalienta el horno a 200 °C.|Pincha el hojaldre y hornéalo entre bandejas hasta dorarlo.|Calienta la leche con vainilla.|Mezcla las yemas, azúcar y maicena y agrega poco a poco la leche caliente.|Cocina revolviendo hasta espesar y enfría completamente.|Intercala capas de hojaldre, crema y frutos rojos.|Refrigera brevemente y sirve."
    ),

    receta(
        "palmeritas",
        "Palmeritas de Hojaldre",
        "Masas de hoja",
        "Básico",
        FOTO_PASTEL,
        "1 lámina rectangular de hojaldre|80 g de azúcar|1 cucharadita de canela opcional",
        "Precalienta el horno a 200 °C.|Espolvorea azúcar sobre la superficie de trabajo.|Coloca el hojaldre y espolvorea más azúcar encima.|Dobla ambos extremos hacia el centro varias veces.|Corta rodajas y distribuye en una bandeja.|Hornea aproximadamente 12 a 18 minutos, volteando si es necesario, hasta dorar."
    ),

    receta(
        "tartaletas-hojaldre",
        "Tartaletas de Hojaldre y Manzana",
        "Masas de hoja",
        "Básico",
        FOTO_PASTEL,
        "1 lámina de hojaldre|3 manzanas|50 g de azúcar|30 g de mantequilla|1 cucharadita de canela|1 huevo para pintar",
        "Precalienta el horno a 200 °C.|Corta el hojaldre en cuadrados y marca un borde interior.|Corta las manzanas en láminas delgadas.|Coloca las manzanas dentro del borde y añade azúcar, canela y pequeños trozos de mantequilla.|Pinta los bordes con huevo batido.|Hornea aproximadamente 20 minutos hasta que el hojaldre esté dorado."
    ),


    receta(
        "galletas-mantequilla",
        "Galletas de Mantequilla",
        "Galletas",
        "Básico",
        FOTO_GALLETAS,
        "250 g de harina|150 g de mantequilla|100 g de azúcar flor|1 huevo|1 cucharadita de vainilla",
        "Bate la mantequilla con el azúcar flor.|Agrega el huevo y la vainilla.|Incorpora la harina hasta formar una masa homogénea.|Refrigera 30 minutos y estira la masa.|Corta las galletas con moldes.|Hornea a 180 °C durante 10 a 15 minutos hasta dorar ligeramente."
    ),

    receta(
        "galletas-chips",
        "Galletas con Chips de Chocolate",
        "Galletas",
        "Básico",
        FOTO_GALLETAS,
        "250 g de harina|130 g de mantequilla|100 g de azúcar rubia|60 g de azúcar blanca|1 huevo|150 g de chips de chocolate|5 g de bicarbonato",
        "Bate la mantequilla con ambos tipos de azúcar.|Agrega el huevo e integra bien.|Añade harina y bicarbonato.|Incorpora los chips de chocolate.|Forma pequeñas porciones dejando espacio entre ellas.|Hornea a 180 °C aproximadamente 10 a 13 minutos.|Deja enfriar antes de retirar."
    ),

    receta(
        "galletas-avena",
        "Galletas de Avena y Pasas",
        "Galletas",
        "Básico",
        FOTO_GALLETAS,
        "150 g de avena|120 g de harina|100 g de mantequilla|100 g de azúcar rubia|1 huevo|80 g de pasas|5 g de polvos de hornear",
        "Precalienta el horno a 180 °C.|Bate la mantequilla con el azúcar e incorpora el huevo.|Agrega harina, avena y polvos de hornear.|Mezcla las pasas y forma porciones sobre una bandeja.|Hornea entre 12 y 16 minutos hasta dorar ligeramente.|Deja enfriar antes de retirar."
    ),

    receta(
        "empanada-pino",
        "Empanadas Chilenas de Pino",
        "Empanadas",
        "Intermedio",
        FOTO_EMPANADAS,
        "500 g de harina|100 g de manteca o mantequilla|220 ml de agua tibia|10 g de sal|400 g de carne molida|3 cebollas|2 huevos duros|Aceitunas|Comino y ají de color",
        "Sofríe la cebolla picada y agrega la carne, comino y ají de color.|Cocina completamente el pino.|Enfría y refrigera el relleno.|Amasa harina, grasa, agua y sal hasta obtener una masa flexible.|Divide y estira círculos de masa.|Rellena con pino, huevo duro y aceitunas.|Humedece los bordes y cierra las empanadas.|Hornea a 200 °C aproximadamente 25 a 35 minutos, hasta que estén doradas."
    ),

    receta(
        "empanada-queso",
        "Empanadas de Queso al Horno",
        "Empanadas",
        "Básico",
        FOTO_EMPANADAS,
        "500 g de harina|80 g de mantequilla|220 ml de agua tibia|10 g de sal|350 g de queso para fundir|1 huevo para pintar",
        "Mezcla harina, sal, mantequilla y agua hasta formar una masa suave.|Deja reposar la masa unos 20 minutos.|Estira y corta círculos.|Coloca queso en el centro sin sobrecargar.|Sella muy bien los bordes con ayuda de un tenedor.|Pinta con huevo y hornea a 200 °C unos 20 a 25 minutos."
    ),

    receta(
        "empanada-pollo",
        "Empanadas de Pollo al Horno",
        "Empanadas",
        "Intermedio",
        FOTO_EMPANADAS,
        "12 discos de masa de empanada|400 g de pechuga de pollo|1 cebolla|1 pimentón|2 cucharadas de aceite|Sal, pimienta y orégano|1 huevo para pintar",
        "Cocina completamente el pollo y desmenúzalo.|Sofríe cebolla y pimentón en el aceite.|Mezcla el pollo con las verduras y condimentos y deja enfriar.|Distribuye el relleno sobre los discos de masa.|Cierra y sella firmemente.|Pinta con huevo y hornea a 200 °C unos 20 a 25 minutos hasta dorar."
    ),

    receta(
        "pan-amasado",
        "Pan Amasado Chileno",
        "Horneo",
        "Básico",
        FOTO_PAN,
        "500 g de harina|280 ml de agua tibia|60 g de manteca o mantequilla|7 g de levadura seca|10 g de sal",
        "Mezcla harina, levadura, agua y sal.|Agrega la grasa y amasa hasta obtener una mezcla homogénea.|Deja reposar hasta que aumente de volumen.|Forma panes redondos y aplánalos ligeramente.|Pincha cada pan con un tenedor.|Deja fermentar brevemente.|Hornea a 200 °C durante aproximadamente 20 a 25 minutos."
    ),

    receta(
        "focaccia",
        "Focaccia de Romero",
        "Horneo",
        "Intermedio",
        FOTO_PAN,
        "500 g de harina|350 ml de agua|7 g de levadura seca|10 g de sal|50 ml de aceite de oliva|Romero fresco|Sal gruesa",
        "Mezcla harina, agua, levadura, sal y parte del aceite.|Amasa brevemente y deja fermentar hasta que aumente su volumen.|Extiende sobre una bandeja aceitada.|Deja reposar nuevamente y forma hoyuelos con los dedos.|Añade aceite, romero y sal gruesa.|Hornea a 220 °C unos 20 a 25 minutos hasta dorar."
    ),

    receta(
        "pan-integral",
        "Pan Integral Casero",
        "Horneo",
        "Básico",
        FOTO_PAN,
        "350 g de harina integral|150 g de harina blanca|330 ml de agua|7 g de levadura seca|10 g de sal|20 ml de aceite",
        "Mezcla las harinas con levadura, sal, agua y aceite.|Amasa hasta lograr una masa uniforme.|Deja fermentar hasta que aumente su volumen.|Forma una hogaza o coloca en un molde para pan.|Deja fermentar por segunda vez.|Hornea a 200 °C entre 30 y 40 minutos, hasta que esté cocido por dentro."
    )
)

@Composable
fun RecipeDetailScreen(
    recetaId: String, onBackClick: () -> Unit,
    onVideoClick: (String, String) -> Unit = { _, _ -> },
    favoritosViewModel: com.example.proyectomartini.viewmodel.FavoritosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val favoritos by favoritosViewModel.titulosFavoritos.collectAsState()
    val receta = recetasMartini.find { it.id == recetaId }
    if (receta == null) {
        CabeceraMartini("Receta no encontrada", "", onBackClick)
        return
    }
    val context = LocalContext.current
    val base = when (recetaId) {
        "mousse", "brownies", "trufas", "cupcakes", "empanada-pollo", "galletas-chips", "galletas-mantequilla", "galletas-avena" -> 12
        "torta-vainilla", "cheesecake", "milhojas", "rollos-canela", "brioche", "focaccia", "pan-integral" -> 8
        "helado-vainilla", "helado-chocolate", "sorbete-frutilla" -> 6
        else -> 10
    }
    var porciones by rememberSaveable(recetaId) { mutableIntStateOf(base) }
    var pestaña by rememberSaveable(recetaId) { mutableStateOf("Ingredientes") }
    var marcados by rememberSaveable(recetaId) { mutableStateOf(intArrayOf()) }
    var pasosListos by rememberSaveable(recetaId) { mutableStateOf(intArrayOf()) }
    var guiado by rememberSaveable(recetaId) { mutableStateOf(false) }
    var pasoActual by rememberSaveable(recetaId) { mutableIntStateOf(0) }
    val tiempo = tiemposRecetas[recetaId]
    val producto = receta.ingredientes.indexOfFirst { it.endsWith("chocolate semiamargo") }
    fun marcarPaso(paso: Int) {
        pasosListos = if (paso in pasosListos) pasosListos.filterNot { it == paso }.toIntArray()
            else pasosListos + paso
    }

    if (guiado) {
        BackHandler { guiado = false }
        CocinaGuiada(receta, base, porciones, tiempo, pasoActual, { pasoActual = it },
            pasosListos.toSet(), ::marcarPaso, { guiado = false })
        return
    }
    val estadoTabs = rememberSaveableStateHolder()
    val tutorialId = when (receta.categoria) {
        "Chocolatería" -> if (receta.id == "mousse") "torta-avellana" else "chocolate"
        "Pastelería" -> "crema-delice"
        "Heladería" -> "helado"
        else -> null
    }
    val video = tutorialesMartini.firstOrNull { it.id == tutorialId } ?: tutorialesMartini.firstOrNull { it.categoria == receta.categoria }
    val duracion = tiempo?.let { formatoTiempoCocina(it.preparacion + it.coccion + it.reposo) }
    fun compartir() {
        val cantidad = receta.ingredientes.joinToString("\n") { "• " + CostosReceta.escalar(it, base, porciones) }
        val envio = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "${receta.titulo} · ${textoPorciones(porciones)}\n\n$cantidad")
        }
        context.startActivity(Intent.createChooser(envio, "Compartir ingredientes"))
    }
    Column(Modifier.fillMaxSize().background(Marfil)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.fillMaxWidth().aspectRatio(1.70f).clip(RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp))) {
                if (recetaId == "brownies") FotoEditorial("editorial_brownies", R.drawable.foto_brownies, Modifier.matchParentSize(), receta.titulo)
                else PortadaMartini(receta.categoria, Modifier.matchParentSize(), recetaId = recetaId)
                AccionCircular(Icons.Default.ArrowBack, "Volver", onBackClick, Modifier.align(Alignment.TopStart).padding(8.dp))
                Row(Modifier.align(Alignment.TopEnd).padding(8.dp)) {
                    val guardada = receta.titulo in favoritos
                    AccionCircular(if (guardada) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        if (guardada) "Quitar de favoritos" else "Guardar en favoritos", {
                            favoritosViewModel.alternar(com.example.proyectomartini.data.local.FavoritoEntity(
                                receta.titulo, "RECETA", duracion ?: "Preparación paso a paso", receta.nivel))
                        }, fondo = Papel, tinta = RojoCaoba)
                    AccionCircular(Icons.Default.Share, "Compartir", ::compartir, fondo = Papel, tinta = VerdePino)
                }
            }
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text(receta.titulo, Modifier.fillMaxWidth(), style = MaterialTheme.typography.headlineMedium,
                    color = VerdePino, fontWeight = FontWeight.ExtraBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    duracion?.let { IndicadorEditorial(Icons.Default.Schedule, it, Modifier.weight(1f)) }
                    IndicadorEditorial(Icons.Default.BarChart, receta.nivel, Modifier.weight(1f))
                    IndicadorEditorial(Icons.Default.Restaurant, "${textoPorciones(porciones)}", Modifier.weight(1f))
                }
                Text(if (recetaId == "brownies") "Un clásico intenso de chocolate, con centro suave y una corteza fina y crujiente."
                    else "Preparación de ${receta.categoria.lowercase()} para practicar en tu negocio.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("Ingredientes", "Preparación", "Tips").forEach {
                        PildoraEditorial(it, pestaña == it, { pestaña = it }, Modifier.weight(if (it == "Tips") .65f else 1f))
                    }
                }
                estadoTabs.SaveableStateProvider(pestaña) {
                    when (pestaña) {
                        "Ingredientes" -> {
                            SuperficieEditorial {
                                Column(Modifier.padding(13.dp)) {
                                    Text("Ingredientes", style = MaterialTheme.typography.titleLarge, color = VerdePino)
                                    receta.ingredientes.forEachIndexed { i, linea ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(i in marcados, { listo -> marcados = if (listo) marcados + i else marcados.filterNot { it == i }.toIntArray() })
                                            Column(Modifier.weight(1f)) {
                                                Text(CostosReceta.escalar(linea, base, porciones))
                                                if (i == producto) Text("Alternativa Master Martini disponible",
                                                    style = MaterialTheme.typography.labelMedium, color = VerdePino)
                                            }
                                        }
                                    }
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp),
                                        verticalAlignment = Alignment.CenterVertically) {
                                        OutlinedButton(onClick = { porciones-- }, enabled = porciones > 1) { Text("−") }
                                        Text("${textoPorciones(porciones)}", Modifier.weight(1f))
                                        OutlinedButton(onClick = { porciones++ }, enabled = porciones < 120) { Text("+") }
                                    }
                                }
                            }
                            if (producto >= 0) SeccionMartini("Producto Master Martini") {
                                CatalogoInsumoFoto("Chocolatería", Modifier.fillMaxWidth().height(135.dp))
                                Text("Ariba · Discos de chocolate oscuro 57%", style = MaterialTheme.typography.titleMedium)
                                Text("Chocolate de fusión con 57% de cacao. Puedes usarlo en la misma cantidad que el chocolate semiamargo de esta receta.")
                                Text("Aplicaciones · Masas, mousses y ganaches", color = VerdePino)
                                val navegador = androidx.compose.ui.platform.LocalUriHandler.current
                                TextButton(onClick = {
                                    navegador.openUri("https://www.martiniprofessional.cl/p/ariba-dischi-fondenti-57")
                                }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Ver ficha del producto") }
                            }
                            tiempo?.let { TiemposDeReceta(it) }
                        }
                        "Preparación" -> {
                            BotonEditorial("Abrir modo cocina", {
                                if (pasoActual == receta.pasos.size) pasoActual = 0
                                guiado = true
                            }, icono = Icons.Default.Restaurant)
                            tiempo?.let { TiemposDeReceta(it) }
                            SeccionMartini("Preparación paso a paso") {
                                AvanceEditorial(pasosListos.size.toFloat() / receta.pasos.size, Modifier.fillMaxWidth())
                                receta.pasos.forEachIndexed { i, paso ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(i in pasosListos, { marcarPaso(i) })
                                        Text("${i + 1}. $paso", Modifier.weight(1f))
                                    }
                                    tiempo?.temporizadores?.get(i + 1)?.let { minutos ->
                                        OutlinedButton(onClick = { pasoActual = i; guiado = true },
                                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                                            Text("Temporizador · ${formatoTiempoCocina(minutos)}")
                                        }
                                    }
                                }
                            }
                            SeccionMartini("Temporizador libre") {
                                TemporizadorCocina("$recetaId/libre", receta.titulo)
                            }
                        }
                        else -> {
                            SeccionMartini("Tips del chef") {
                                Text("Pesa las porciones, registra el rendimiento y comprueba el resultado antes de aumentar el lote.")
                                Text("Receta de demostración académica. Revisa la ficha técnica y los alérgenos del insumo antes de usarlo.")
                                Text("Portada ilustrativa.")
                            }
                            CalculadoraReceta(porciones)
                        }
                    }
                }
            }
            Spacer(Modifier.height(5.dp))
        }
        if (video != null) {
            Box(Modifier.fillMaxWidth().background(Marfil).padding(horizontal = 16.dp, vertical = 6.dp)) {
                BotonEditorial("Ver video de la receta", { onVideoClick(video.id, video.titulo) }, icono = Icons.Default.PlayArrow)
            }
        }
    }
}
