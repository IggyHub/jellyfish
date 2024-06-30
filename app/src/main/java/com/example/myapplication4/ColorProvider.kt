package com.example.myapplication4

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object ColorProvider {
    val homeScreenColor = mutableStateOf(Color(0xFF214EF3))
    val columnColor = mutableStateOf(Color(0xAB1CECF3))
    val cardColor = mutableStateOf(Color(0xFF879EF3))
    val dashboardScreenColor = mutableStateOf(Color(0xFFFF9800))
    val settingsScreenColor = mutableStateOf(Color(0xFF2196F3))
}
