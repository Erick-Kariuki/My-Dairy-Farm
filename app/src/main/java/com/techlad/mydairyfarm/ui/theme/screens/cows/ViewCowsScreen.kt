package com.techlad.mydairyfarm.ui.theme.screens.cows

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.techlad.mydairyfarm.models.CowModel
import com.techlad.mydairyfarm.navigation.ROUTE_UPDATE_COW
import com.techlad.mydairyfarm.viewmodels.CowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCowsScreen(
    navController: NavController,
    status: String,
    modifier: Modifier = Modifier
) {
    val cowViewModel: CowViewModel = viewModel()
    val context = LocalContext.current

    val filteredCows = cowViewModel.getFilteredCows(status)
    val cowState by cowViewModel.cowState.collectAsState()
    val isLoading = cowState.isLoading

    //val cows = cowViewModel.cowList
    // Normalize status for comparison
//    val filteredCows = if (status.equals("Total", ignoreCase = true)) {
//        cows
//    } else {
//        cows.filter { it.status.trim().lowercase() == status.trim().lowercase() }
//    }

    LaunchedEffect(Unit) {
        cowViewModel.fetchCows(context)
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "My Cows", fontSize = 28.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            OutlinedTextField(
                value = cowViewModel.searchQuery.value,
                onValueChange = { cowViewModel.updateSearchQuery(it) },
                label = { Text("Search cow by name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            ) {
                when {
                    isLoading -> {
                        // Loading indicator centered
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    filteredCows.isEmpty() -> {
                        // No cows text
                        Text(
                            text = "No cows found in this category",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 24.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.DarkGray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    else -> {
                        // List of cows
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredCows) { cow ->
                                CowCard(
                                    cowModel = cow,
                                    onDelete = { cowId ->
                                        cowViewModel.deleteCow(cowId = cowId, context = context)
                                    },
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CowCard(
    cowModel: CowModel,
    onDelete: (String) -> Unit,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this cow?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onDelete(cowModel.cowId)
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = cowModel.imageUrl,
                    contentDescription = "Cow Image",
                    modifier = Modifier
                        .size(140.dp).clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Cow name: ${cowModel.cowName}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Mother's name: ${cowModel.motherName}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Date of Birth: ${cowModel.dateOfBirth}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Tag Number: ${cowModel.tagNumber}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Breed: ${cowModel.breed}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Status: ${cowModel.status}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(onClick = {
                            // TODO: Add update cow feature later
                            navController.navigate(ROUTE_UPDATE_COW + "/${cowModel.cowId}")
                        }) {
                            Text(
                                text = "EDIT",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        OutlinedButton(onClick = { showDialog = true }) {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
