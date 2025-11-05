package com.techlad.mydairyfarm.ui.theme.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.techlad.mydairyfarm.R
import com.techlad.mydairyfarm.ui.theme.MyDairyFarmTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
                    modifier: Modifier = Modifier
) {

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
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {

            Column(modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "ERIC DAIRY FARM",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp)

                Spacer(modifier = Modifier.height(18.dp))

                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()) {

                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                        elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)) {
                            Image(
                                painter = painterResource(R.drawable.cattle),
                                contentDescription = null,
                                modifier = modifier.size(90.dp)
                            )
                            Text(
                                text = "Total",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "0",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Image(
                            painter = painterResource(R.drawable.cattle),
                            modifier = modifier.size(90.dp),
                            contentDescription = null
                        )
                        Text(text = "Milking",
                            fontWeight = FontWeight.Bold)
                        Text(text = "0",
                            fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(R.drawable.cattle),
                            modifier = modifier.size(90.dp),
                            contentDescription = null
                        )
                        Text(text = "Bulls",
                            fontWeight = FontWeight.Bold)
                        Text(text = "0",
                            fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(R.drawable.cow),
                            contentDescription = null,
                            modifier = modifier.size(90.dp)
                        )
                        Text(text = "Dry",
                            fontWeight = FontWeight.Bold)
                        Text(text = "0",
                            fontWeight = FontWeight.Bold)
                    }


                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Image(
                            painter = painterResource(R.drawable.cow),
                            modifier = modifier.size(90.dp),
                            contentDescription = null
                        )
                        Text(text = "Heifers",
                            fontWeight = FontWeight.Bold)
                        Text(text = "0",
                            fontWeight = FontWeight.Bold)
                    }


                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(R.drawable.cow),
                            modifier = modifier.size(90.dp),
                            contentDescription = null
                        )
                        Text(text = "Calves",
                            fontWeight = FontWeight.Bold)
                        Text(text = "0",
                            fontWeight = FontWeight.Bold)
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun DashboardPreview(){
    MyDairyFarmTheme {
        DashboardScreen()
    }
}