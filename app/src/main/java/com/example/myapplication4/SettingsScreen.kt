package com.example.myapplication4

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize().background(ColorProvider.settingsScreenColor.value)) {
        Text("Settings Screen", style = MaterialTheme.typography.h4, modifier = Modifier.padding(16.dp))
    }
}
