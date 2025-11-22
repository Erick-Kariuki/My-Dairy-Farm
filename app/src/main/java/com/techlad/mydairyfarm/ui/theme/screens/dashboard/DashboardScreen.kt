package com.techlad.mydairyfarm.ui.theme.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techlad.mydairyfarm.R
import com.techlad.mydairyfarm.navigation.ROUTE_ADD_NEW_COW
import com.techlad.mydairyfarm.navigation.ROUTE_VIEW_COW
import com.techlad.mydairyfarm.navigation.ROUTE_VIEW_INSEMINATED_COWS
import com.techlad.mydairyfarm.ui.theme.MyDairyFarmTheme
import com.techlad.mydairyfarm.viewmodels.CowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val cowViewModel: CowViewModel = viewModel()
    val cows = cowViewModel.cowList
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        cowViewModel.fetchCows(context)
    }


    // Calculate counts
    val totalCount = cows.size
    val milkingCount = cows.count { it.status.equals("Milking", ignoreCase = true) }
    val bullsCount = cows.count { it.status.equals("Bull", ignoreCase = true) }
    val dryCount = cows.count { it.status.equals("Dry", ignoreCase = true) }
    val heifersCount = cows.count { it.status.equals("Heifer", ignoreCase = true) }
    val calvesCount = cows.count { it.status.equals("Calf", ignoreCase = true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "My Dairy Farm", fontSize = 28.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Welcome to your dashboard",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = "Use this app to manage your cows",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // First row of cards
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusCard(R.drawable.cattle, "Total", totalCount,navController)
                StatusCard(R.drawable.cattle, "Milking", milkingCount,navController)
                StatusCard(R.drawable.cattle, "Bull", bullsCount,navController)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Second row of cards
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusCard(R.drawable.cow, "Dry", dryCount, navController)
                StatusCard(R.drawable.cow, "Heifer", heifersCount, navController)
                StatusCard(R.drawable.cow, "Calf", calvesCount,navController)
            }

            Spacer(modifier = Modifier.height(32.dp))


            // Add Cow Card
            Card(
                modifier = Modifier.fillMaxWidth()
                    .clickable { navController.navigate(ROUTE_ADD_NEW_COW) },
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.cowicon),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(
                            text = "Add Cow",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Add a new cow to your app",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Breeding records Card
            Card(
                modifier = Modifier.fillMaxWidth()
                    .clickable { navController.navigate(ROUTE_VIEW_INSEMINATED_COWS) },
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.cowicon),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(
                            text = "Breeding Records",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Manage your breeding records here",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))



            // Milk records Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.cowicon),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(
                            text = "Milk Records",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Manage your milk records here",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusCard(
    imageRes: Int,
    label: String,
    count: Int,
    navController: NavController
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.padding(4.dp)
            .clickable { navController.navigate("$ROUTE_VIEW_COW/$label")}
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Text(text = label, fontWeight = FontWeight.Bold)
            Text(text = count.toString(), fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    MyDairyFarmTheme {
        DashboardScreen(navController = rememberNavController())
    }
}
