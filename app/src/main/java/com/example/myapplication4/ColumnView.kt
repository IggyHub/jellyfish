package com.example.myapplication4

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Card
import com.example.myapplication4.data.Column
import kotlinx.coroutines.launch

@Composable
fun ColumnView(
    database: AppDatabase,
    column: Column,
    cards: List<Card>,
    backgroundColor: Color,
    onColumnDeleted: () -> Unit,
    onCardsDeleted: () -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val columnDao = database.columnDao()
    val cardDao = database.cardDao()

    val openDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(backgroundColor)
            .padding(8.dp)
            .width(300.dp)
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = column.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { expanded.value = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    coroutineScope.launch {
                        cardDao.deleteCardsByColumn(column.id)
                        columnDao.deleteColumn(column)
                        onColumnDeleted()
                    }
                }) {
                    Text("Delete Column")
                }
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    coroutineScope.launch {
                        cardDao.deleteCardsByColumn(column.id)
                        onCardsDeleted()
                    }
                }) {
                    Text("Delete All Cards")
                }
            }
        }
        Text(text = column.description, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { openDialog.value = true },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Add New Card")
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            items(cards) { card ->
                CardView(card)
            }
        }
    }

    if (openDialog.value) {
        AddCardDialog(
            database = database,
            columnId = column.id,
            onDismiss = { openDialog.value = false },
            onCardAdded = { onCardsDeleted() }
        )
    }
}

@Composable
fun AddCardDialog(
    database: AppDatabase,
    columnId: Int,
    onDismiss: () -> Unit,
    onCardAdded: () -> Unit
) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val cardDao = database.cardDao()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Card") },
        text = {
            Column {
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Title") }
                )
                TextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                coroutineScope.launch {
                    cardDao.insertCard(
                        Card(
                            columnId = columnId,
                            title = title.value,
                            description = description.value,
                            sortOrder = (cardDao.getCardsForColumn(columnId).size + 1)
                        )
                    )
                    onDismiss()
                    onCardAdded()
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
