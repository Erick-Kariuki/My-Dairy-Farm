package com.techlad.mydairyfarm.ui.theme.screens.cows


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.techlad.mydairyfarm.models.InseminatedCowModel
import com.techlad.mydairyfarm.navigation.ROUTE_ADD_INSEMINATED_COW
import com.techlad.mydairyfarm.viewmodels.InseminatedCowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewInseminatedCowsScreen(navController: NavController) {
    val viewModel: InseminatedCowViewModel = viewModel()
    val context = LocalContext.current

    val cows = viewModel.inseminatedCowList
    val isLoading by viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.fetchInseminatedCows(context)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Breeding Records",
                        fontSize = 28.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding,)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF6F6F6))
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    cows.isEmpty() -> {
                        Text(
                            text = "No inseminated cows found",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(cows) { cow ->
                                InseminatedCowCard(
                                    cow = cow,
                                    onEdit = { navController.navigate(ROUTE_ADD_INSEMINATED_COW + "/${cow.id}") },
                                    onDelete = { viewModel.deleteInseminatedCow(it, context) }
                                )
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { navController.navigate(ROUTE_ADD_INSEMINATED_COW) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Inseminated Cow",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

    @Composable
    fun InseminatedCowCard(
        cow: InseminatedCowModel,
        onEdit: (String) -> Unit,
        onDelete: (String) -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cow Name: ${cow.cowName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Bull Breed: ${cow.bullBreed}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Pregnancy Duration: ${cow.pregnancyDuration}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = { onEdit(cow.id) }) {
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { onDelete(cow.id) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
