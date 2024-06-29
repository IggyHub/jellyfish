package com.example.myapplication4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Card
import com.example.myapplication4.data.Column
import kotlinx.coroutines.runBlocking
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        database = AppDatabase.getDatabase(this)

        var bTestInit = false

        if (bTestInit) {
            runBlocking {
                initializeDatabase(database)
            }
        }

        setContent {
            MyApp(database)
        }
    }
}

suspend fun initializeDatabase(database: AppDatabase) {
    val columnDao = database.columnDao()
    val cardDao = database.cardDao()

    val columns = listOf(
        Column(title = "Column 1", description = "Description 1", sortOrder = 1),
        Column(title = "Column 2", description = "Description 2", sortOrder = 2),
        Column(title = "Column 3", description = "Description 3", sortOrder = 3)
    )

    columns.forEach { column ->
        val columnId = columnDao.insertColumn(column).toInt()
        val cards = listOf(
            Card(columnId = columnId, title = "Card 1", description = "Description 1", sortOrder = 1),
            Card(columnId = columnId, title = "Card 2", description = "Description 2", sortOrder = 2),
            Card(columnId = columnId, title = "Card 3", description = "Description 3", sortOrder = 3),
            Card(columnId = columnId, title = "Card 4", description = "Description 4", sortOrder = 4),
            Card(columnId = columnId, title = "Card 5", description = "Description 5", sortOrder = 5)
        )
        cards.forEach { card -> cardDao.insertCard(card) }
    }
}

@Composable
fun MyApp(database: AppDatabase) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Application") },
                navigationIcon = {
                    val backStackEntry = navController.currentBackStackEntryAsState().value
                    if (backStackEntry?.destination?.route != "home") {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                BottomNavigationItem(
                    icon = { /* Add Icons here if needed */ },
                    label = { Text("Home") },
                    selected = currentDestination?.route == "home",
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                BottomNavigationItem(
                    icon = { /* Add Icons here if needed */ },
                    label = { Text("Dashboard") },
                    selected = currentDestination?.route == "dashboard",
                    onClick = {
                        navController.navigate("dashboard") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                BottomNavigationItem(
                    icon = { /* Add Icons here if needed */ },
                    label = { Text("Settings") },
                    selected = currentDestination?.route == "settings",
                    onClick = {
                        navController.navigate("settings") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") { HomeScreen(database) }
            composable("dashboard") { DashboardScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

@Composable
fun DashboardScreen() {
    // Define your Dashboard screen UI here
    Box(modifier = Modifier.fillMaxSize().background(Color.Green)) {
        Text("Dashboard Screen", style = MaterialTheme.typography.h4, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun SettingsScreen() {
    // Define your Settings screen UI here
    Box(modifier = Modifier.fillMaxSize().background(Color.Blue)) {
        Text("Settings Screen", style = MaterialTheme.typography.h4, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ColumnView(database: AppDatabase, column: Column, cards: List<Card>, backgroundColor: Color) {
    val expanded = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val columnDao = database.columnDao()
    val cardDao = database.cardDao()

    Column(
        modifier = Modifier
            .background(backgroundColor) // Use the provided background color
            .padding(8.dp)
            .width(250.dp) // Adjust the width to be smaller
            .fillMaxHeight() // Ensure column fills the height of the parent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp), // Add some padding to separate the text from the menu
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Center-align items vertically
        ) {
            Text(
                text = column.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f) // Take up remaining space
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
                    //coroutineScope.launch {
                    //    columnDao.deleteColumn(column)
                    //}
                }) {
                    Text("Delete Column")
                }
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    //coroutineScope.launch {
                    //    cardDao.deleteCardsByColumn(column.id)
                    //}
                }) {
                    Text("Delete All Cards")
                }
            }
        }
        Text(text = column.description, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxHeight() // Ensure LazyColumn fills the height of the parent
        ) {
            items(cards) { card ->
                CardView(card)
            }
        }
    }
}

@Composable
fun HomeScreen(database: AppDatabase) {
    val columns = remember { mutableStateListOf<Column>() }
    val cards = remember { mutableStateMapOf<Int, List<Card>>() }

    LaunchedEffect(Unit) {
        val fetchedColumns = database.columnDao().getAllColumns()
        columns.addAll(fetchedColumns)
        fetchedColumns.forEach { column ->
            cards[column.id] = database.cardDao().getCardsForColumn(column.id)
        }
    }

    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Add space between columns
    ) {
        items(columns) { column ->
            ColumnView(
                database = database,
                column = column,
                cards = cards[column.id] ?: emptyList(),
                backgroundColor = Color.LightGray // You can use different colors or random colors
            )
        }
    }
}



@Composable
fun CardView(card: Card) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .padding(8.dp)
    ) {
        Text(text = card.title)
        Text(text = card.description)
    }
}
