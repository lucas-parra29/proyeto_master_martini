package com.example.proyectomartini.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas as Lienzo
import android.graphics.Color as Tinta
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas

internal fun simboloMedalla(categoria: String) = when (categoria) {
    "Pastelería" -> 1
    "Heladería" -> 2
    "Chocolatería" -> 3
    "Bollería", "Panadería", "Horneo", "Masas de hoja" -> 4
    "Galletas" -> 5
    "Empanadas" -> 6
    else -> 0
}

@Composable internal fun MedallaEditorial(modifier: Modifier, tono: Int = 0, simbolo: Int = 0) {
    Canvas(modifier.aspectRatio(.90f)) {
        val lienzo = drawContext.canvas.nativeCanvas
        lienzo.save()
        lienzo.scale(size.width / 200f, size.height / 222f)
        dibujarMedalla(lienzo, tono, simbolo)
        lienzo.restore()
    }
}

private fun dibujarMedalla(c: Lienzo, metal: Int = 0, simbolo: Int = 0) {
    val p = Paint(Paint.ANTI_ALIAS_FLAG)
    val tonos = when (metal) {
        1 -> intArrayOf(0xFFF4F5F0.toInt(), 0xFFA3A49E.toInt(), 0xFF666963.toInt())
        2 -> intArrayOf(0xFFF8CC8E.toInt(), 0xFFB8703C.toInt(), 0xFF7E3D18.toInt())
        else -> intArrayOf(0xFFFFE997.toInt(), 0xFFD7A039.toInt(), 0xFF885507.toInt())
    }
    fun cinta(inicio: Float, ultimo: Float, color: Int) {
        p.color = color
        c.drawPath(Path().apply {
            moveTo(inicio, 137f); lineTo(inicio + 43f, 130f); lineTo(ultimo + 18f, 217f)
            lineTo(ultimo, 201f); lineTo(ultimo - 25f, 214f); close()
        }, p)
    }
    cinta(51f, 52f, 0xFFA92330.toInt())
    cinta(99f, 149f, 0xFF095034.toInt())
    p.color = 0x24000000
    c.drawCircle(103f, 103f, 85f, p)
    p.shader = LinearGradient(38f, 21f, 161f, 166f, tonos, null, Shader.TileMode.CLAMP)
    c.drawCircle(100f, 96f, 87f, p)
    p.shader = null
    p.style = Paint.Style.STROKE
    p.color = tonos[2]
    p.strokeWidth = 3.5f
    c.drawCircle(100f, 96f, 82f, p)
    p.color = tonos[0]
    c.drawCircle(100f, 96f, 74f, p)
    p.strokeWidth = 1.4f
    c.drawCircle(100f, 96f, 67f, p)
    p.style = Paint.Style.FILL
    p.color = tonos[2]
    for (lado in listOf(-1, 1)) {
        for (i in 0..4) {
            val x = 100f + lado * (55f - i * 2.5f)
            val y = 113f + i * 8f
            c.save()
            c.rotate(-lado * (38f + i * 4f), x, y)
            c.drawOval(RectF(x - 3.5f, y - 7f, x + 3.5f, y + 7f), p)
            c.restore()
        }
    }
    p.style = Paint.Style.STROKE
    p.strokeJoin = Paint.Join.ROUND
    p.strokeCap = Paint.Cap.ROUND
    p.strokeWidth = 4.6f
    when (simbolo) {
        1 -> {
            c.save()
            c.rotate(32f, 100f, 94f)
            c.drawOval(84f, 58f, 116f, 104f, p)
            c.drawOval(93f, 58f, 107f, 104f, p)
            c.drawLine(100f, 102f, 100f, 131f, p)
            c.restore()
        }
        2 -> {
            c.drawPath(Path().apply { moveTo(81f, 96f); lineTo(100f, 131f); lineTo(119f, 96f); close() }, p)
            c.drawArc(RectF(77f, 61f, 123f, 105f), 175f, 190f, false, p)
        }
        3 -> {
            c.drawRoundRect(74f, 62f, 126f, 127f, 7f, 7f, p)
            c.drawLine(100f, 62f, 100f, 127f, p)
            c.drawLine(74f, 84f, 126f, 84f, p)
            c.drawLine(74f, 106f, 126f, 106f, p)
        }
        4 -> {
            c.drawPath(Path().apply {
                moveTo(66f, 110f); cubicTo(63f, 67f, 126f, 56f, 136f, 100f)
                cubicTo(145f, 126f, 65f, 137f, 66f, 110f); close()
            }, p)
            c.drawLine(88f, 79f, 97f, 94f, p)
            c.drawLine(104f, 72f, 113f, 87f, p)
        }
        5 -> {
            c.drawCircle(100f, 95f, 31f, p)
            c.drawCircle(89f, 84f, 3f, p)
            c.drawCircle(111f, 86f, 3f, p)
            c.drawCircle(104f, 107f, 3f, p)
            c.drawCircle(83f, 103f, 2f, p)
        }
        6 -> {
            c.drawArc(RectF(67f, 71f, 133f, 124f), 180f, 180f, false, p)
            c.drawLine(67f, 97f, 133f, 97f, p)
            c.drawLine(85f, 97f, 85f, 88f, p)
            c.drawLine(100f, 97f, 100f, 84f, p)
            c.drawLine(115f, 97f, 115f, 88f, p)
        }
        else -> c.drawPath(Path().apply {
            moveTo(79f, 119f); lineTo(79f, 97f)
            cubicTo(64f, 93f, 63f, 75f, 80f, 72f)
            cubicTo(85f, 51f, 114f, 52f, 119f, 72f)
            cubicTo(136f, 75f, 136f, 93f, 122f, 97f)
            lineTo(122f, 119f); close()
            moveTo(80f, 109f); lineTo(121f, 109f)
        }, p)
    }
}

internal fun dibujarDiploma(c: Lienzo, logo: Bitmap?, nombre: String, curso: String, fecha: String) {
    val p = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    val oro = 0xFFB58929.toInt()
    val verde = 0xFF064B37.toInt()
    p.shader = LinearGradient(0f, 0f, 600f, 800f,
        intArrayOf(0xFFFFFDF4.toInt(), 0xFFF5E7C7.toInt()), null, Shader.TileMode.CLAMP)
    c.drawRect(0f, 0f, 600f, 800f, p)
    p.shader = null
    p.style = Paint.Style.STROKE
    p.color = oro
    p.strokeWidth = 2.8f
    c.drawRoundRect(18f, 18f, 582f, 782f, 20f, 20f, p)
    p.strokeWidth = 1.1f
    c.drawRoundRect(27f, 27f, 573f, 773f, 14f, 14f, p)
    fun cacao(x: Float, y: Float, giro: Float) {
        c.save(); c.translate(x, y); c.rotate(giro)
        p.color = 0x70A17B3F
        c.drawPath(Path().apply {
            moveTo(0f, -67f); cubicTo(-67f, -24f, -52f, 43f, 0f, 81f)
            cubicTo(44f, 57f, 55f, -29f, 0f, -67f); close()
        }, p)
        for (ancho in listOf(-31f, -14f, 12f, 30f)) {
            c.drawPath(Path().apply { moveTo(0f, -67f); quadTo(ancho * 2, -4f, 0f, 81f) }, p)
        }
        c.restore()
    }
    cacao(504f, 129f, 24f)
    cacao(95f, 661f, -52f)
    cacao(143f, 714f, -44f)
    p.style = Paint.Style.FILL
    logo?.let {
        val ancho = minOf(150f, 75f * it.width / it.height)
        val alto = ancho * it.height / it.width
        c.drawBitmap(it, null, RectF(300f - ancho / 2, 102.5f - alto / 2,
            300f + ancho / 2, 102.5f + alto / 2), p)
    }
    fun linea(texto: String, y: Float, tamaño: Float, color: Int, serif: Boolean = false, negrita: Boolean = false) {
        p.color = color
        p.textAlign = Paint.Align.CENTER
        p.typeface = Typeface.create(if (serif) Typeface.SERIF else Typeface.SANS_SERIF,
            if (negrita) Typeface.BOLD else Typeface.NORMAL)
        p.textSize = tamaño
        while (p.measureText(texto) > 500f && p.textSize > 11f) p.textSize--
        c.drawText(texto, 300f, y, p)
    }
    linea("Certificado de", 208f, 54f, verde, true, true)
    linea("Finalización", 265f, 54f, verde, true, true)
    linea("Se otorga a", 330f, 23f, Tinta.BLACK)
    linea(nombre, 395f, 43f, Tinta.BLACK, true, true)
    p.color = oro
    p.strokeWidth = 1.1f
    c.drawLine(79f, 417f, 521f, 417f, p)
    linea("por completar el curso", 465f, 23f, Tinta.BLACK)
    linea(curso, 512f, 34f, verde, true, true)
    linea(fecha, 552f, 19f, verde)
    c.save(); c.translate(232f, 580f); c.scale(.68f, .68f); dibujarMedalla(c); c.restore()
    linea("Constancia académica · Prototipo DSY1105", 748f, 17f, verde)
}
