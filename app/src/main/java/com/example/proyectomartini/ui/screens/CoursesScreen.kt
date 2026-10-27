package com.example.proyectomartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectomartini.ui.theme.*

data class LeccionMartini(
    val titulo: String,
    val descripcion: String
)

data class CursoMartini(
    val id: String,
    val titulo: String,
    val categoria: String,
    val nivel: String,
    val descripcion: String,
    val lecciones: List<LeccionMartini>
)

private val cursosMartini = listOf(
    CursoMartini(
        id = "curso-pasteleria",
        titulo = "Fundamentos de Pastelería",
        categoria = "Pastelería",
        nivel = "Básico",
        descripcion = "Aprende las técnicas esenciales de elaboración de postres.",
        lecciones = listOf(
            LeccionMartini(
                "1. Higiene y preparación",
                "Antes de cocinar, lava tus manos y prepara una superficie limpia. Revisa las fechas de los ingredientes y pesa cada cantidad con una balanza. Mantén refrigerados los productos perecederos cuando no se utilicen."
            ),
            LeccionMartini(
                "2. Técnicas de mezclado",
                "El batido incorpora aire y ayuda a obtener preparaciones esponjosas. Al añadir harina a un bizcocho, mezcla lo necesario para integrar los ingredientes y evita trabajar excesivamente la masa."
            ),
            LeccionMartini(
                "3. Horneado y decoración",
                "Precalienta el horno y utiliza el molde adecuado. Comprueba la cocción antes de retirar. Deja enfriar los bizcochos antes de cortar, rellenar o decorar con crema."
            )
        )
    ),
    CursoMartini(
        id = "curso-chocolate",
        titulo = "Introducción a la Chocolatería",
        categoria = "Chocolatería",
        nivel = "Intermedio",
        descripcion = "Descubre técnicas básicas para trabajar con chocolate.",
        lecciones = listOf(
            LeccionMartini(
                "1. Tipos de chocolate",
                "El chocolate negro, con leche y blanco presentan proporciones diferentes de cacao, azúcar y grasa. Estas diferencias modifican su sabor, fluidez y comportamiento durante la preparación."
            ),
            LeccionMartini(
                "2. Preparación de ganache",
                "Pica el chocolate en trozos pequeños. Calienta la crema y viértela sobre el chocolate. Espera unos instantes y mezcla suavemente hasta obtener una preparación homogénea."
            ),
            LeccionMartini(
                "3. Acabados y conservación",
                "Utiliza utensilios secos y limpios para trabajar con chocolate. Protege los productos de temperaturas elevadas, humedad y olores fuertes. Las elaboraciones con crema requieren refrigeración adecuada."
            )
        )
    ),
    CursoMartini(
        id = "curso-horneo",
        titulo = "Panadería y Técnicas de Horneo",
        categoria = "Horneo",
        nivel = "Básico",
        descripcion = "Aprende a preparar masas y controlar su fermentación.",
        lecciones = listOf(
            LeccionMartini(
                "1. Ingredientes del pan",
                "La harina, el agua, la levadura y la sal son ingredientes habituales del pan. Sus cantidades influyen en la textura, el sabor y la fermentación."
            ),
            LeccionMartini(
                "2. Amasado y fermentación",
                "El amasado desarrolla la estructura de la masa. Durante la fermentación, la levadura genera gas que contribuye al aumento de volumen. Controla temperatura, tiempo y aspecto de la masa."
            ),
            LeccionMartini(
                "3. Cocción y enfriamiento",
                "Precalienta el horno según el tipo de pan. Ajusta el tiempo de cocción al tamaño de las piezas. Al terminar, deja enfriar sobre una rejilla antes de guardar o cortar."
            )
        )
    )
)

@Composable
fun CoursesScreen(
    onBackClick: () -> Unit
) {
    var cursoSeleccionado by remember {
        mutableStateOf<CursoMartini?>(null)
    }

    var leccionSeleccionada by remember {
        mutableStateOf<LeccionMartini?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NieveBrillante)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdePino)
                .padding(16.dp)
        ) {
            TextButton(
                onClick = {
                    when {
                        leccionSeleccionada != null ->
                            leccionSeleccionada = null

                        cursoSeleccionado != null ->
                            cursoSeleccionado = null

                        else -> onBackClick()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text("Volver", color = Color.White)
            }

            Text(
                text = leccionSeleccionada?.titulo
                    ?: cursoSeleccionado?.titulo
                    ?: "Cursos Master Martini",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (cursoSeleccionado == null) {
                Text(
                    "Aprende con nuestras lecciones",
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        when {
            leccionSeleccionada != null -> {
                val leccion = leccionSeleccionada!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text(
                        text = leccion.titulo,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdePino
                    )

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = leccion.descripcion,
                        fontSize = 16.sp,
                        lineHeight = 25.sp,
                        color = GrisTexto
                    )

                    Spacer(Modifier.height(25.dp))

                    Button(
                        onClick = {
                            leccionSeleccionada = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdePino
                        )
                    ) {
                        Text("Volver al curso")
                    }
                }
            }

            cursoSeleccionado != null -> {
                val curso = cursoSeleccionado!!

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = curso.descripcion,
                            fontSize = 15.sp,
                            color = GrisTexto
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Lecciones",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerdePino
                        )
                    }

                    items(curso.lecciones) { leccion ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    leccionSeleccionada = leccion
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Blanco
                            )
                        ) {
                            Text(
                                text = leccion.titulo,
                                modifier = Modifier.padding(18.dp),
                                fontWeight = FontWeight.Bold,
                                color = VerdePino
                            )
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(cursosMartini) { curso ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    cursoSeleccionado = curso
                                },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Blanco
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = VerdePino,
                                    modifier = Modifier.size(34.dp)
                                )

                                Spacer(Modifier.width(14.dp))

                                Column {
                                    Text(
                                        text = curso.titulo,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VerdePino
                                    )

                                    Spacer(Modifier.height(5.dp))

                                    Text(
                                        text = "${curso.categoria} • ${curso.nivel}",
                                        fontSize = 13.sp,
                                        color = GrisTexto
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = curso.descripcion,
                                        fontSize = 13.sp,
                                        color = GrisTexto
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = "${curso.lecciones.size} lecciones • Ver curso",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VerdePino
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
