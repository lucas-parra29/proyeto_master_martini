package com.example.proyectomartini.ui.screens

data class TutorialMartini(val id: String, val titulo: String, val categoria: String, val objetivo: String, val producto: String, val pasos: List<String>)
val tutorialesMartini = listOf(
    TutorialMartini("torta-avellana", "Torta de chocolate y avellana", "Chocolatería",
        "Observa la aplicación de la crema de relleno y el acabado de una torta.",
        "Caravella Deco", listOf("Organiza el montaje antes de empezar.", "Observa el espesor del relleno.", "Registra el rendimiento al cortar.")),
    TutorialMartini("crema-delice", "Preparación de cremas Delice", "Pastelería",
        "Distingue las preparaciones de pastelera, chantilly y merengue.",
        "Mezclas Delice", listOf("Escoge el producto para la preparación.", "Confirma la proporción en su ficha.", "Comprueba la textura antes de montar.")),
    TutorialMartini("gratin-bravo", "Gratín de papas", "Food Service",
        "Observa el uso de crema vegetal en una preparación salada de horno.",
        "Bravo Crem Cocina", listOf("Prepara la mise en place de las papas.", "Observa la distribución de la crema.", "Evalúa el color y porciona.")),
    TutorialMartini("chocolate", "Ganache de chocolate Ariba", "Chocolatería", "Mezcla y emulsión", "Chocolate Ariba Dark 57%", listOf("Pesa los ingredientes y pica el chocolate.", "Calienta la crema y viértela sobre el chocolate.", "Mezcla hasta emulsionar y conserva refrigerado.")),
    TutorialMartini("pastel", "Decoración con crema vegetal Orasì", "Pastelería", "Acabado de tortas", "Crema vegetal Orasì", listOf("Enfría la crema según su etiqueta.", "Bate hasta lograr la consistencia deseada.", "Decora con manga sobre un bizcocho frío.")),
    TutorialMartini("helado", "Coberturas para helados con Caravella", "Heladería", "Acabado de helados", "Caravella Ice Cacao", listOf("Consulta las instrucciones de la cobertura.", "Prepara el helado firme y la cobertura.", "Aplica una capa fina y conserva en frío.")),
    TutorialMartini("bolleria", "Preparación de masas de bollería", "Bollería", "Manejo de masas", "Master Export Horneo", listOf("Pesa y organiza todos los ingredientes.", "Amasa según la formulación y deja fermentar.", "Divide, forma y hornea según la receta.")),
    TutorialMartini("hoja", "Manejo de masas de hoja", "Masas de hoja", "Laminado y reposo", "Insumos Master Martini según ficha técnica", listOf("Mantén la masa y la grasa a temperatura adecuada.", "Extiende y pliega sin romper las capas.", "Deja reposar en frío antes de cortar y hornear.")),
    TutorialMartini("galletas", "Galletas con chocolate Ariba", "Galletas", "Porcionado uniforme", "Chocolate Ariba", listOf("Mezcla los ingredientes de la receta sin excederte.", "Agrega el chocolate y pesa porciones iguales.", "Hornea según la receta y deja enfriar.")),
    TutorialMartini("empanadas", "Preparación de empanadas", "Empanadas", "Formado y sellado", "Master Export Horneo (según formulación)", listOf("Prepara el relleno y deja enfriar.", "Rellena la masa y sella bien los bordes.", "Hornea y comprueba la cocción antes de servir.")),
    TutorialMartini("pan", "Control de fermentación y horneo", "Horneo", "Preparación del pan", "Master Export Horneo (si la receta lo requiere)", listOf("Pesa los ingredientes y amasa según la receta.", "Observa el volumen durante la fermentación.", "Hornea y enfría sobre rejilla."))
)
