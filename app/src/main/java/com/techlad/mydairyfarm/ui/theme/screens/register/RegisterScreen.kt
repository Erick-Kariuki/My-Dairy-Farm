package com.techlad.mydairyfarm.ui.theme.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.autoSaver
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
import com.techlad.mydairyfarm.navigation.ROUTE_LOGIN
import com.techlad.mydairyfarm.ui.theme.MyDairyFarmTheme
import com.techlad.mydairyfarm.viewmodels.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController,
                   modifier: Modifier = Modifier){

    var farmName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

//    if (authState.isLoading){
//        CircularProgressIndicator()
//    }else

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "My Dairy Farm",
                    fontSize = 28.sp)},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding,)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {


            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = "Register to create your Dairy Farm",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = modifier.height(12.dp))

                Image(
                    painter = painterResource(R.drawable.homescreen_cowicon11),
                    contentDescription = null
                )

                Spacer(modifier = modifier.height(12.dp))

                OutlinedTextField(
                    value = farmName,
                    onValueChange = { farmName = it },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = "Farm name") },
                    placeholder = { Text(text = "Enter the name of your farm") },
                    modifier = modifier.fillMaxWidth()
                )

                Spacer(modifier = modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Email,
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
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            if (isPasswordVisible) {
                                Icon(
                                    Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    visualTransformation = if (isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "Enter Password") },
                    modifier = modifier.fillMaxWidth()
                )

                Spacer(modifier = modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(text = "Confirm Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            isConfirmPasswordVisible = !isConfirmPasswordVisible
                        }) {
                            if (isConfirmPasswordVisible) {
                                Icon(
                                    Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    visualTransformation = if (isConfirmPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    placeholder = { Text(text = "Confirm your Password") },
                    modifier = modifier.fillMaxWidth()
                )

                Spacer(modifier = modifier.height(12.dp))

                Button(
                    onClick = {
                        authViewModel.signup(
                            farmName = farmName,
                            email = email,
                            password = password,
                            navController = navController,
                            confirmPassword = confirmPassword,
                            context = context
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { if (authState.isLoading){
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = Color.White
                    )}else
                    Text(
                        text = "REGISTER",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Text(
                        text = "Already have a farm? ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 24.sp
                    )

                    Text(
                        text = "Login",
                        color = Color.Blue,
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.clickable { navController.navigate(ROUTE_LOGIN) }
                    )

                }
            }
        }
    }
}

@Preview(showSystemUi = true,
    showBackground = true)
@Composable
fun RegisterScreenPreview(){
    MyDairyFarmTheme {
        RegisterScreen(navController = rememberNavController())
    }
}