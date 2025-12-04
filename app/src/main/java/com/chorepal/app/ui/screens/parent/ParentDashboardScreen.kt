package com.chorepal.app.ui.screens.parent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.chorepal.app.data.models.ChoreStatus
import com.chorepal.app.data.models.ChoreType
import com.chorepal.app.data.models.User
import com.chorepal.app.viewmodel.AuthViewModel
import com.chorepal.app.viewmodel.ChoreViewModel
import com.chorepal.app.viewmodel.UserViewModel
import com.chorepal.app.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    onLogout: () -> Unit,
    onChildClick: (String) -> Unit,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val choreViewModel: ChoreViewModel = viewModel(factory = factory)
    val userViewModel: UserViewModel = viewModel(factory = factory)
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCreateChoreDialog by remember { mutableStateOf(false) }
    var showAddChildDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    
    val currentUser by userViewModel.currentUser.collectAsState()
    val children by userViewModel.children.collectAsState()
    val pendingChores by choreViewModel.pendingChores.collectAsState()
    
    // Load data
    LaunchedEffect(Unit) {
        authViewModel.currentUser.collect { user ->
            user?.let {
                userViewModel.loadUser(it.userId)
                userViewModel.loadChildren(it.userId)
                choreViewModel.loadPendingApprovals(it.userId)
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parent Dashboard") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                showMenu = false
                                onLogout()
                            },
                            leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = null) }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = { showAddChildDialog = true },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Add Child")
                }
                FloatingActionButton(
                    onClick = { showCreateChoreDialog = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Chore")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Dashboard Stats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        title = "Children",
                        value = children.size.toString(),
                        icon = Icons.Default.People
                    )
                    StatCard(
                        title = "Pending",
                        value = pendingChores.size.toString(),
                        icon = Icons.Default.PendingActions
                    )
                }
            }
            
            // Tab Layout
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Children") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Pending Approvals") }
                )
            }
            
            // Content based on selected tab
            when (selectedTab) {
                0 -> ChildrenList(
                    children = children,
                    onChildClick = onChildClick,
                    userViewModel = userViewModel
                )
                1 -> PendingApprovalsList(
                    pendingChores = pendingChores,
                    choreViewModel = choreViewModel
                )
            }
        }
    }
    
    // Dialogs
    if (showCreateChoreDialog) {
        CreateChoreDialog(
            children = children,
            onDismiss = { showCreateChoreDialog = false },
            onCreateChore = { title, description, points, type, assignedTo, dueDate ->
                choreViewModel.createChore(
                    title = title,
                    description = description,
                    pointsValue = points,
                    choreType = type,
                    createdBy = currentUser?.userId ?: "",
                    assignedTo = assignedTo,
                    dueDate = dueDate
                )
                showCreateChoreDialog = false
            }
        )
    }
    
    val authCurrentUser by authViewModel.currentUser.collectAsState()
    
    if (showAddChildDialog) {
        AddChildDialog(
            onDismiss = { showAddChildDialog = false },
            familyCode = authCurrentUser?.familyCode ?: currentUser?.familyCode ?: "LOADING..."
        )
    }
}

@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ChildrenList(
    children: List<User>,
    onChildClick: (String) -> Unit,
    userViewModel: UserViewModel
) {
    if (children.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No children added yet")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(children) { child ->
                ChildCard(child = child, onClick = { onChildClick(child.userId) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildCard(child: User, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = child.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${child.totalPoints} points",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "View details"
            )
        }
    }
}

@Composable
fun PendingApprovalsList(
    pendingChores: List<com.chorepal.app.data.models.Chore>,
    choreViewModel: ChoreViewModel
) {
    val scope = rememberCoroutineScope()
    var selectedChore by remember { mutableStateOf<com.chorepal.app.data.models.Chore?>(null) }
    
    if (pendingChores.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("All caught up!")
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pendingChores) { chore ->
                PendingChoreCard(
                    chore = chore,
                    onApprove = { 
                        scope.launch {
                            choreViewModel.approveChore(chore.choreId)
                        }
                    },
                    onReject = { selectedChore = chore }
                )
            }
        }
    }
    
    // Reject dialog
    selectedChore?.let { chore ->
        AlertDialog(
            onDismissRequest = { selectedChore = null },
            title = { Text("Reject Chore") },
            text = {
                var reason by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            choreViewModel.rejectChore(chore.choreId, "")
                            selectedChore = null
                        }
                    }
                ) {
                    Text("Reject")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedChore = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PendingChoreCard(
    chore: com.chorepal.app.data.models.Chore,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    var showImageDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
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
                    style = MaterialTheme.typography.titleMedium
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
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Show photo proof if available
            chore.imageProofUrl?.let { imageUrl ->
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "Photo Proof Attached",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Chore proof photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clickable { showImageDialog = true },
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Tap to view full size",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reject")
                }
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Approve")
                }
            }
        }
    }
    
    // Full-screen image dialog
    if (showImageDialog && chore.imageProofUrl != null) {
        Dialog(onDismissRequest = { showImageDialog = false }) {
            Card {
                Column {
                    AsyncImage(
                        model = chore.imageProofUrl,
                        contentDescription = "Full size proof photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp),
                        contentScale = ContentScale.Fit
                    )
                    TextButton(
                        onClick = { showImageDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChoreDialog(
    children: List<User>,
    onDismiss: () -> Unit,
    onCreateChore: (String, String, Int, ChoreType, String?, Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("10") }
    var choreType by remember { mutableStateOf(ChoreType.DAILY) }
    var selectedChild by remember { mutableStateOf<User?>(null) }
    var expanded by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Chore") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Chore Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                
                OutlinedTextField(
                    value = points,
                    onValueChange = { points = it },
                    label = { Text("Points Value") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Child selector
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedChild?.name ?: "Select Child",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Assign To") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        children.forEach { child ->
                            DropdownMenuItem(
                                text = { Text(child.name) },
                                onClick = {
                                    selectedChild = child
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                // Chore type selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = choreType == ChoreType.DAILY,
                        onClick = { choreType = ChoreType.DAILY },
                        label = { Text("Daily") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = choreType == ChoreType.BONUS,
                        onClick = { choreType = ChoreType.BONUS },
                        label = { Text("Bonus") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = choreType == ChoreType.WEEKLY,
                        onClick = { choreType = ChoreType.WEEKLY },
                        label = { Text("Weekly") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onCreateChore(
                        title,
                        description,
                        points.toIntOrNull() ?: 10,
                        choreType,
                        selectedChild?.userId,
                        null
                    )
                },
                enabled = title.isNotBlank() && description.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddChildDialog(
    onDismiss: () -> Unit,
    familyCode: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        title = { Text("Add a Child to Your Family") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Family Code Display
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your Family Code",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = familyCode,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 4.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Share this code with your child",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Instructions:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                StepItem(
                    number = "1",
                    text = "Have your child tap 'Sign Up' on the login screen"
                )
                
                StepItem(
                    number = "2",
                    text = "They enter their name, email, and password"
                )
                
                StepItem(
                    number = "3",
                    text = "They select 'Child' as the role"
                )
                
                StepItem(
                    number = "4",
                    text = "They enter your family code: $familyCode"
                )
                
                StepItem(
                    number = "5",
                    text = "Done! You can now assign chores to them"
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Got it!")
            }
        }
    )
}

@Composable
fun StepItem(number: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}


