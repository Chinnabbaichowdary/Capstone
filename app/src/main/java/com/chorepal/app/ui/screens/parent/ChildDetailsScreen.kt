package com.chorepal.app.ui.screens.parent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chorepal.app.data.models.Chore
import com.chorepal.app.data.models.PointTransaction
import com.chorepal.app.viewmodel.ChoreViewModel
import com.chorepal.app.viewmodel.UserViewModel
import com.chorepal.app.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildDetailsScreen(
    childId: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val choreViewModel: ChoreViewModel = viewModel(factory = factory)
    val userViewModel: UserViewModel = viewModel(factory = factory)
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var showPointsDialog by remember { mutableStateOf(false) }
    
    val child by userViewModel.currentUser.collectAsState()
    val chores by choreViewModel.chores.collectAsState()
    val transactions by userViewModel.pointTransactions.collectAsState()
    
    LaunchedEffect(childId) {
        userViewModel.loadUser(childId)
        choreViewModel.loadChoresForChild(childId)
        userViewModel.loadPointTransactions(childId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(child?.name ?: "Child Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showPointsDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Manage Points")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Points Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Points",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${child?.totalPoints ?: 0}",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                }
            }
            
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Chores") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Points History") }
                )
            }
            
            // Content
            when (selectedTab) {
                0 -> ChoresListView(chores)
                1 -> PointsHistoryView(transactions)
            }
        }
    }
    
    if (showPointsDialog) {
        ManagePointsDialog(
            childId = childId,
            onDismiss = { showPointsDialog = false },
            userViewModel = userViewModel
        )
    }
}

@Composable
fun ChoresListView(chores: List<Chore>) {
    if (chores.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No chores assigned yet")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chores) { chore ->
                ChoreItemCard(chore)
            }
        }
    }
}

@Composable
fun ChoreItemCard(chore: Chore) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = chore.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${chore.pointsValue} pts",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chore.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${chore.status.name.replace("_", " ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun PointsHistoryView(transactions: List<PointTransaction>) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    
    if (transactions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No transaction history yet")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transactions) { transaction ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = transaction.reason,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = dateFormat.format(Date(transaction.timestamp)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${if (transaction.pointsChange > 0) "+" else ""}${transaction.pointsChange}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (transaction.pointsChange > 0) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ManagePointsDialog(
    childId: String,
    onDismiss: () -> Unit,
    userViewModel: UserViewModel
) {
    var pointsAmount by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var isAdding by remember { mutableStateOf(true) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isAdding) "Add Points" else "Redeem Points") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = isAdding,
                        onClick = { isAdding = true },
                        label = { Text("Add") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = !isAdding,
                        onClick = { isAdding = false },
                        label = { Text("Redeem") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                OutlinedTextField(
                    value = pointsAmount,
                    onValueChange = { pointsAmount = it },
                    label = { Text("Points") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val points = pointsAmount.toIntOrNull() ?: 0
                    if (points > 0 && reason.isNotBlank()) {
                        if (isAdding) {
                            userViewModel.addBonusPoints(childId, points, reason, "parent_id")
                        } else {
                            userViewModel.redeemPoints(childId, points, reason, "parent_id")
                        }
                        onDismiss()
                    }
                },
                enabled = pointsAmount.toIntOrNull() != null && reason.isNotBlank()
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

