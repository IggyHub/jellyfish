package com.example.myapplication4

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication4.data.Card

@Composable
fun CardView(card: Card) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background( ColorProvider.cardColor.value)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = card.title, style = MaterialTheme.typography.h6)
        Text(text = card.description, style = MaterialTheme.typography.body1)
    }
}