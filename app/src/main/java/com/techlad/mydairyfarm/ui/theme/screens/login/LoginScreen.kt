package com.techlad.mydairyfarm.ui.theme.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techlad.mydairyfarm.R
import com.techlad.mydairyfarm.navigation.ROUTE_FORGOT_PASSWORD
import com.techlad.mydairyfarm.navigation.ROUTE_LOGIN
import com.techlad.mydairyfarm.navigation.ROUTE_REGISTER
import com.techlad.mydairyfarm.ui.theme.MyDairyFarmTheme
import com.techlad.mydairyfarm.ui.theme.errorLight
import com.techlad.mydairyfarm.ui.theme.onErrorLight
import com.techlad.mydairyfarm.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController,
                modifier: Modifier = Modifier){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

//    if (authState.isLoading){
//        Column (modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally)
//        {
//        CircularProgressIndicator(modifier = Modifier.size(140.dp))
//        }
//    }else

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(title = {Text(text = "My Dairy Farm", fontSize = 28.sp)},
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary))
        }
    ){ innerPadding ->
    Column (modifier= Modifier.padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .imePadding()) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = "Login to your Dairy Farm",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(R.drawable.homescreen_cowicon11),
                contentDescription = null
            )

            Spacer(modifier = modifier.height(12.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null
                    )
                },
                label = { Text(text = "Email") },
                placeholder = { Text(text = "Enter your email") },
                modifier = modifier.fillMaxWidth()
            )

            Spacer(modifier = modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                trailingIcon = { IconButton(onClick = { isPasswordVisible = !isPasswordVisible })
                {
                    if (isPasswordVisible) Icon(Icons.Default.VisibilityOff, contentDescription = null)
                    else Icon(Icons.Default.Visibility, contentDescription = null)
                }},
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                placeholder = { Text(text = "Enter Password") },
                modifier = modifier.fillMaxWidth()
            )

            Spacer(modifier = modifier.height(12.dp))

            Button(
                onClick = {
                    authViewModel.login(email,
                        password,
                        navController,
                        context)
                },
                modifier = modifier.fillMaxWidth()
            ) {
                if (authState.isLoading){
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = Color.White
                    )
                }else
                Text(text = "LOGIN",
                    fontSize = 20.sp)

            }
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "Don't have a farm? ",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 24.sp
                )

                Text(
                    text = "Create farm",
                    color = Color.Blue,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.clickable { navController.navigate(ROUTE_REGISTER) }
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Forgot Password?",
                color = MaterialTheme.colorScheme.error,
                fontSize = 20.sp,
                modifier = Modifier.clickable{
                    navController.navigate(ROUTE_FORGOT_PASSWORD)
                }
            )

        }
    }
    }
}

@Preview(showSystemUi = true,
    showBackground = true)
@Composable
fun LoginScreenPreview(){
    MyDairyFarmTheme {
        LoginScreen(navController = rememberNavController())
    }
}