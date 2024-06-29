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
import kotlinx.coroutines.launch
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
            .width(300.dp) // Adjust the width to take more space
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

@Composable
fun HomeScreen(database: AppDatabase) {
    val columns = remember { mutableStateListOf<Column>() }
    val cards = remember { mutableStateMapOf<Int, List<Card>>() }
    val coroutineScope = rememberCoroutineScope()

    // Function to refresh columns and cards
    val refreshData = {
        coroutineScope.launch {
            val fetchedColumns = database.columnDao().getAllColumns()
            columns.clear()
            columns.addAll(fetchedColumns)
            fetchedColumns.forEach { column ->
                cards[column.id] = database.cardDao().getCardsForColumn(column.id)
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(columns) { column ->
            ColumnView(
                database = database,
                column = column,
                cards = cards[column.id] ?: emptyList(),
                backgroundColor = Color.LightGray,
                onColumnDeleted = { refreshData() },
                onCardsDeleted = { refreshData() }
            )
        }
    }
}





@Composable
fun CardView(card: Card) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.Cyan) // Change the background color here
            .fillMaxWidth() // Make the card take the full width of its parent
            .padding(8.dp)
    ) {
        Text(text = card.title, style = MaterialTheme.typography.h6)
        Text(text = card.description, style = MaterialTheme.typography.body1)
    }
}