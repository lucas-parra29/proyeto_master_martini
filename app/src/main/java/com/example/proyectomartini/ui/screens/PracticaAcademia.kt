package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectomartini.ui.theme.*

internal data class PracticaLeccion(
    val objetivo: String, val pasos: List<String>, val pregunta: String,
    val opciones: List<String>, val correcta: Int, val explicacion: String
)

internal val practicasAcademia = mapOf(
    "curso-pasteleria" to listOf(
        PracticaLeccion("Organiza el puesto de trabajo",
            listOf("Anota fechas y alérgenos de cada insumo", "Pesa cada ingrediente en su propio recipiente"),
            "¿Qué debes registrar antes de iniciar un lote para vitrina?",
            listOf("Ingredientes, cantidades y alérgenos", "Solo el nombre del postre", "El precio final, sin ingredientes"), 0,
            "La ficha del lote permite repetir la preparación y responder preguntas sobre sus ingredientes."),
        PracticaLeccion("Compara dos formas de incorporar harina",
            listOf("Integra la harina con movimientos suaves en una muestra", "Compara la densidad con una muestra trabajada de más"),
            "¿Por qué conviene integrar la harina sin batir de más?",
            listOf("Para duplicar la humedad", "Para conservar el aire del batido", "Para que no haya que hornear"), 1,
            "El mezclado excesivo puede bajar el volumen y endurecer la textura."),
        PracticaLeccion("Prepara un montaje para vitrina",
            listOf("Comprueba el punto de cocción del bizcocho", "Deja enfriar y verifica que la decoración conserve su forma"),
            "¿Cuándo conviene rellenar un bizcocho con crema?",
            listOf("Recién salido del horno", "Antes de comprobar la cocción", "Cuando esté frío y estable"), 2,
            "El calor puede ablandar el relleno y volver irregular el montaje.")
    ),
    "curso-chocolate" to listOf(
        PracticaLeccion("Compara chocolate negro, con leche y blanco",
            listOf("Lee el porcentaje de cacao y la composición de cada muestra", "Anota fluidez y sabor con la misma técnica de derretido"),
            "¿Por qué dos chocolates fundidos pueden fluir distinto?",
            listOf("Cambia su composición y la proporción de grasa", "Depende solo del color de la bolsa", "Se debe al tamaño del recipiente únicamente"), 0,
            "La composición y la temperatura influyen en la fluidez y el resultado."),
        PracticaLeccion("Practica la emulsión de una ganache",
            listOf("Agrega la crema según la formulación que estés usando", "Mezcla desde el centro y observa si aparecen vetas de grasa"),
            "¿Qué señal indica que la emulsión quedó uniforme?",
            listOf("Capa aceitosa separada", "Brillo parejo y ausencia de gotas aceitosas", "Grumos duros sin integrar"), 1,
            "Una ganache estable se ve pareja. Si se separa, revisa el método y la temperatura antes de repetir el lote."),
        PracticaLeccion("Prepara una ficha de conservación",
            listOf("Usa utensilios secos y separa piezas sólidas de rellenos con crema", "Anota la fecha y las condiciones de almacenamiento según la ficha aplicable"),
            "¿Con qué criterio guardas una ganache con crema?",
            listOf("La guardas igual que una barra sólida", "Te guías únicamente por el color", "Aplicas la conservación indicada para ese relleno"), 2,
            "La ganache contiene crema y su manejo requiere condiciones distintas a las de un chocolate sólido.")
    ),
    "curso-horneo" to listOf(
        PracticaLeccion("Calcula la hidratación de una masa",
            listOf("Pesa por separado harina y agua", "Divide el peso de agua por el de harina y multiplica por 100"),
            "Si usas 500 g de harina y 300 g de agua, ¿qué hidratación tienes?",
            listOf("60 %", "30 %", "80 %"), 0,
            "300 dividido por 500, multiplicado por 100, da 60 %."),
        PracticaLeccion("Observa el desarrollo de la masa",
            listOf("Estira una pequeña porción para observar la elasticidad", "Marca el nivel de la masa en un recipiente y compara su volumen al fermentar"),
            "¿Qué te ayuda a decidir si la fermentación avanzó?",
            listOf("Solo el reloj, siempre al mismo tiempo", "El volumen y la estructura de la masa", "Añadir harina hasta que no se mueva"), 1,
            "El avance cambia con la temperatura y las condiciones. Observa la masa además de medir el tiempo."),
        PracticaLeccion("Verifica la cocción y el enfriamiento",
            listOf("Separa piezas por peso antes del horno", "Deja enfriar en rejilla y revisa corteza y miga de una pieza"),
            "¿Por qué enfriar el pan sobre rejilla?",
            listOf("Para reactivar la levadura", "Para aumentar la hidratación inicial", "Para que circule aire y no se humedezca la base"), 2,
            "La rejilla facilita la salida de vapor y ayuda a mantener la textura de la corteza.")
    ),
    "curso-gratin-bravo" to listOf(
        PracticaLeccion("Planifica porciones del gratín",
            listOf("Escoge un molde y corta láminas de espesor uniforme", "Pesa la preparación inicial y registra las porciones previstas"),
            "¿Qué favorece una cocción uniforme?",
            listOf("Láminas de espesor parecido", "Mezclar láminas finas con piezas enteras", "Cambiar de molde durante el horneado"), 0,
            "Las láminas de tamaño similar requieren tiempos de cocción parecidos."),
        PracticaLeccion("Controla la cobertura",
            listOf("Distribuye la crema según la receta entre las capas", "Revisa que las esquinas del molde queden cubiertas"),
            "¿Cómo evitas capas secas?",
            listOf("Agregas toda la crema a un solo lado", "Distribuyes la crema sin dejar sectores sin cobertura", "Retiras la cobertura del fondo"), 1,
            "El reparto regular evita zonas secas y charcos."),
        PracticaLeccion("Mide rendimiento final",
            listOf("Pesa el gratín terminado y corta porciones uniformes", "Anota qué ajustar para mejorar textura y rendimiento"),
            "¿Qué dato sirve para calcular el costo por porción?",
            listOf("Solo el peso del molde", "El nombre del insumo", "Número de porciones reales del lote"), 2,
            "El costo total se reparte entre las porciones que realmente puedes servir.")
    ),
    "curso-helado" to listOf(
        PracticaLeccion("Prepara una prueba de cobertura",
            listOf("Lee las instrucciones de uso de la cobertura", "Pesa una porción de helado firme y prepara una segunda para comparar"),
            "¿Por qué conviene ensayar una sola pieza primero?",
            listOf("Para ajustar la capa antes de intervenir todo el lote", "Para evitar pesar los ingredientes", "Para eliminar el control de frío"), 0,
            "La prueba pequeña permite corregir el acabado sin comprometer todo el lote."),
        PracticaLeccion("Compara espesores de la capa",
            listOf("Aplica una capa fina y otra un poco más gruesa", "Compara la cobertura al corte y la facilidad al morder"),
            "¿Qué describe una cobertura bien aplicada?",
            listOf("Un charco en la base", "Capa uniforme sin acumulaciones", "La pieza pierde su forma"), 1,
            "Controla la cantidad aplicada para obtener una capa uniforme."),
        PracticaLeccion("Registra el resultado en frío",
            listOf("Conserva las dos muestras bajo las mismas condiciones", "Compara textura, grietas y adherencia después del reposo"),
            "¿Qué te permite comparar resultados de forma justa?",
            listOf("Dejar una pieza fuera del frío", "Cambiar la formulación y el tiempo a la vez", "Mismas condiciones de conservación"), 2,
            "Cambiar una variable por prueba permite atribuir las diferencias al ajuste correcto.")
    ),
    "curso-bolleria" to listOf(
        PracticaLeccion("Establece pesos de referencia",
            listOf("Pesa ingredientes y anota el peso total de la masa", "Divide por el número de piezas que vas a hacer"),
            "¿Qué favorece piezas uniformes?",
            listOf("Dividir por peso antes de formar", "Calcular las porciones a ojo", "Ajustar todas las piezas al final del horneado"), 0,
            "El peso parecido favorece una cocción y un rendimiento consistentes."),
        PracticaLeccion("Controla amasado y fermentación",
            listOf("Observa si la masa gana elasticidad al amasarla", "Compara el volumen y la consistencia antes de formar"),
            "¿Qué te indica que conviene seguir observando la fermentación?",
            listOf("Masa con más volumen y estructura aireada", "Poco cambio de volumen respecto del inicio", "Piezas ya cocidas"), 1,
            "Usa el estado de la masa para decidir, porque los tiempos cambian de un lote a otro."),
        PracticaLeccion("Cuida el acabado",
            listOf("Forma piezas del mismo peso y colócalas separadas", "Revisa el color y deja enfriar en rejilla"),
            "¿Por qué dejar espacio entre piezas?",
            listOf("Para evitar pesar la harina", "Para aumentar el azúcar de la receta", "Para permitir su expansión y un acabado más parejo"), 2,
            "Las piezas necesitan espacio para expandirse sin pegarse.")
    ),
    "curso-hoja" to listOf(
        PracticaLeccion("Compara masa y grasa",
            listOf("Extiende una pequeña muestra de masa", "Comprueba que la grasa se doble sin romperse ni escurrir"),
            "¿Qué condición ayuda a laminar?",
            listOf("Masa y grasa con plasticidad similar", "Grasa líquida sobre masa rígida", "Grasa quebradiza a cualquier temperatura"), 0,
            "Una plasticidad parecida ayuda a mantener las capas definidas."),
        PracticaLeccion("Controla laminado y pliegues",
            listOf("Estira con presión uniforme y registra cada pliegue", "Detente antes de que la grasa atraviese la masa"),
            "¿Qué haces si la grasa empieza a escaparse?",
            listOf("Aumentas la presión", "Detienes el laminado y dejas reposar en frío", "Agregas agua encima"), 1,
            "El reposo ayuda a recuperar firmeza y conservar la separación de las capas."),
        PracticaLeccion("Busca capas en el corte",
            listOf("Enfría la masa y corta una pieza de prueba con un cuchillo afilado", "Compara el corte crudo con el resultado tras hornear"),
            "¿Por qué evitar aplastar los bordes al cortar?",
            listOf("Para aumentar el peso", "Para enfriar el horno", "Para conservar visibles las capas"), 2,
            "Si presionas demasiado, puedes unir las láminas e impedir que se abran.")
    ),
    "curso-galletas" to listOf(
        PracticaLeccion("Prepara el lote de ensayo",
            listOf("Pesa masa y chips por separado", "Registra cuántas piezas puedes obtener con el peso objetivo"),
            "¿Qué debes anotar para repetir porciones iguales?",
            listOf("Peso objetivo de cada pieza", "Solo el diámetro de la bandeja", "El nombre del horno"), 0,
            "El peso facilita repetir tamaño, cocción y costo por unidad."),
        PracticaLeccion("Compara dos porciones",
            listOf("Pesa dos porciones iguales y deja espacio en la bandeja", "Observa cuánto se extienden al hornear"),
            "¿Qué haces si todas se expanden demasiado?",
            listOf("Cambias ingredientes y horneado al mismo tiempo", "Ajustas una variable en un lote de prueba", "Ignoras el resultado y produces más"), 1,
            "Ajusta una condición por vez y registra el resultado."),
        PracticaLeccion("Evalúa textura una vez fría",
            listOf("Deja enfriar las galletas sobre una rejilla", "Separa y cuenta las piezas aptas para vender"),
            "¿Cuándo conviene medir la textura final?",
            listOf("En plena cocción", "Antes de pesar las piezas", "Después del enfriado"), 2,
            "Las galletas cambian de firmeza durante el enfriado.")
    ),
    "curso-empanadas" to listOf(
        PracticaLeccion("Prepara relleno y masa",
            listOf("Registra el peso de masa y relleno de la porción piloto", "Maneja el relleno de acuerdo con las condiciones de conservación de la receta"),
            "¿Qué debes controlar para repetir la misma porción?",
            listOf("Peso de la masa y del relleno", "Solo la forma de la bandeja", "El color del plato"), 0,
            "Medir ambos componentes ayuda a repetir rendimiento y tamaño."),
        PracticaLeccion("Practica el cierre",
            listOf("Centra el relleno sin manchar los bordes", "Sella dos empanadas y compara el cierre antes de hornear"),
            "¿Qué favorece un sello firme?",
            listOf("Relleno sobre todo el borde", "Bordes limpios y relleno centrado", "Agregar relleno hasta forzar la masa"), 1,
            "El relleno en el borde dificulta que la masa se adhiera."),
        PracticaLeccion("Comprueba el resultado del lote",
            listOf("Evalúa tanto la cocción del relleno como el acabado de la masa", "Anota fugas de relleno y el número de piezas aptas"),
            "¿Qué haces si se abren varias piezas?",
            listOf("Agregas más relleno sin cambiar nada", "Etiquetas todas las piezas como aptas", "Revisas relleno y sellado antes de repetir"), 2,
            "El registro te permite corregir el cierre y calcular el rendimiento real.")
    )
)

@Composable
internal fun PracticaGuiada(clave: String, practica: PracticaLeccion, resuelta: Boolean, resolver: () -> Unit) {
    var primero by rememberSaveable(clave) { mutableStateOf(false) }
    var segundo by rememberSaveable(clave) { mutableStateOf(false) }
    var elegida by rememberSaveable(clave) { mutableIntStateOf(-1) }
    var revisada by rememberSaveable(clave) { mutableStateOf(false) }

    SeccionMartini("Práctica guiada") {
        Text(practica.objetivo, style = MaterialTheme.typography.titleMedium, color = VerdePino)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(primero, { primero = it })
            Text(practica.pasos[0], Modifier.weight(1f))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(segundo, { segundo = it })
            Text(practica.pasos[1], Modifier.weight(1f))
        }
    }

    SeccionMartini("Comprueba lo aprendido") {
        Text(practica.pregunta, style = MaterialTheme.typography.titleMedium)
        practica.opciones.forEachIndexed { i, respuesta ->
            PildoraEditorial(respuesta, (resuelta && i == practica.correcta) || (!resuelta && elegida == i),
                { if (!resuelta) { elegida = i; revisada = false } }, Modifier.fillMaxWidth())
        }
        if (resuelta) {
            Text("Respuesta correcta. ${practica.explicacion}", color = VerdePino)
        } else {
            if (revisada) {
                Text("Revisa la técnica y prueba otra respuesta.", color = RojoCaoba)
                Text(practica.explicacion, color = GrisTexto)
            }
            BotonEditorial("Comprobar respuesta", {
                if (elegida == practica.correcta) resolver() else revisada = true
            }, habilitado = elegida >= 0)
        }
    }
}
