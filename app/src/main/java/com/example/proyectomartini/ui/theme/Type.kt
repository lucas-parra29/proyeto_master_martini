package com.example.proyectomartini.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
val Typography = Typography(
    headlineLarge = TextStyle(fontSize=32.sp, lineHeight=39.sp, fontWeight=FontWeight.Bold),
    headlineMedium = TextStyle(fontSize=27.sp, lineHeight=34.sp, fontWeight=FontWeight.Bold),
    titleLarge = TextStyle(fontSize=23.sp, lineHeight=30.sp, fontWeight=FontWeight.Bold),
    titleMedium = TextStyle(fontSize=19.sp, lineHeight=25.sp, fontWeight=FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize=18.sp, lineHeight=27.sp),
    bodyMedium = TextStyle(fontSize=16.sp, lineHeight=24.sp),
    bodySmall = TextStyle(fontSize=14.sp, lineHeight=20.sp),
    labelLarge = TextStyle(fontSize=16.sp, lineHeight=22.sp, fontWeight=FontWeight.SemiBold),
    labelMedium = TextStyle(fontSize=14.sp, lineHeight=20.sp, fontWeight=FontWeight.Medium),
    labelSmall = TextStyle(fontSize=13.sp, lineHeight=18.sp)
)
