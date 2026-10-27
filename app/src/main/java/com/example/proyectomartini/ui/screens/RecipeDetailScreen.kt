
package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectomartini.ui.theme.*

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
        "Precalienta el horno a 180 °C.|Derrite el chocolate junto con la mantequilla.|Bate los huevos con el azúcar e incorpora el chocolate tibio.|Agrega la harina, el cacao y la sal.|Vierte en un molde forrado con papel de hornear.|Hornea aproximadamente 20 a 25 minutos y deja enfriar antes de cortar."
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
        "Precalienta el horno a 180 °C.|Bate mantequilla con azúcar y agrega huevos y vainilla.|Incorpora la harina, los polvos de hornear y la leche.|Distribuye en cápsulas hasta dos tercios de su capacidad.|Hornea aproximadamente 18 a 22 minutos y deja enfriar.|Mezcla el queso crema con azúcar flor y decora los cupcakes."
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
        "Bate la mantequilla con ambos tipos de azúcar.|Agrega el huevo e integra bien.|Añade harina y bicarbonato.|Incorpora los chips de chocolate.|Forma pequeñas porciones dejando espacio entre ellas.|Hornea a 180 °C aproximadamente 10 a 13 minutos y deja enfriar."
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
        "Sofríe la cebolla picada y agrega la carne, comino y ají de color.|Cocina completamente el pino, enfría y refrigera.|Amasa harina, grasa, agua y sal hasta obtener una masa flexible.|Divide y estira círculos de masa.|Rellena con pino, huevo duro y aceitunas.|Humedece los bordes y cierra las empanadas.|Hornea a 200 °C aproximadamente 25 a 35 minutos, hasta que estén doradas."
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
        "Mezcla harina, levadura, agua y sal.|Agrega la grasa y amasa hasta obtener una mezcla homogénea.|Deja reposar hasta que aumente de volumen.|Forma panes redondos y aplánalos ligeramente.|Pincha cada pan con un tenedor.|Deja fermentar brevemente y hornea a 200 °C durante aproximadamente 20 a 25 minutos."
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
    recetaId: String,
    onBackClick: () -> Unit
) {
    val receta = recetasMartini.find { it.id == recetaId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Volver a Explorar", color = VerdePino)
        }

        if (receta == null) {
            Text(
                "Receta no encontrada",
                color = MaterialTheme.colorScheme.error
            )
            return@Column
        }

        Text(
            text = receta.titulo,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePino
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${receta.categoria} • ${receta.nivel}",
            color = GrisTexto,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(16.dp))

        ImagenMartini(
            url = receta.imagenUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Receta de demostración para Master Martini",
            fontSize = 12.sp,
            color = GrisTexto
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Ingredientes",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePino
        )

        Spacer(Modifier.height(8.dp))

        receta.ingredientes.forEach { ingrediente ->
            Text(
                text = "• $ingrediente",
                color = VerdePino,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        Spacer(Modifier.height(22.dp))

        Text(
            text = "Preparación paso a paso",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePino
        )

        Spacer(Modifier.height(8.dp))

        receta.pasos.forEachIndexed { index, paso ->
            Text(
                text = "${index + 1}. $paso",
                color = VerdePino,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(30.dp))
    }
}
