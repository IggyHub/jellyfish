package com.example.myapplication4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        database = AppDatabase.getDatabase(this)

        setContent {
            MyApp(database)
        }
    }
}

@Composable
fun MyApp(database: AppDatabase) {
    val list1 = remember { mutableStateListOf<Card>() }
    val list2 = remember { mutableStateListOf<Card>() }

    LaunchedEffect(Unit) {
        val cards = database.cardDao().getAllCards()
        list1.addAll(cards)
        list2.addAll(cards)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ListView(title = "List 1", items = list1, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            ListView(title = "List 2", items = list2, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ListView(title: String, items: List<Card>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.h6)
        LazyColumn {
            items(items.size) { index ->
                Text(
                    text = items[index].toString(),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
