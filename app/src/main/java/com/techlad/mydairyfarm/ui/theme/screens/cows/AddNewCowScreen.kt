package com.techlad.mydairyfarm.ui.theme.screens.cows

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.techlad.mydairyfarm.R
import com.techlad.mydairyfarm.ui.theme.MyDairyFarmTheme
import com.techlad.mydairyfarm.viewmodels.CowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCowScreen(navController: NavController, modifier: Modifier = Modifier){
    var cowName by remember { mutableStateOf("") }
    var motherName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var tagNumber by remember { mutableStateOf("") }
    var cowBreed by remember { mutableStateOf("") }
    var cowStatus by remember { mutableStateOf("") }

    val cowStatusOptions = listOf("Milking","Dry", "Calf", "Heifer", "Bull")
    var expandedCowStatus by remember { mutableStateOf(false) }
    val cowBreedOptions = listOf("Friesian","Holstein","Jersey","Angus","Sahiwal","Gir","Nellore","Zebu" )
    var expandedCowBreed by remember { mutableStateOf(false) }

    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent())
    { uri: Uri? -> uri?.let { imageUri.value = it } }

    val context = LocalContext.current
    val cowViewModel: CowViewModel = viewModel()


    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(title = {Text(text = "Add new cow", fontSize = 28.sp)},
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary))
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {

            Card(
                modifier = Modifier.padding(24.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(18.dp)
                ) {


                    Card(
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier.size(140.dp)
                            .clickable { launcher.launch("image/*") }
                    ) {
                        AnimatedContent(
                            targetState = imageUri.value,
                            label = ""
                        ) { targetUri ->
                            AsyncImage(
                                model = targetUri ?: R.drawable.cowicon,
                                contentDescription = "Cow Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Tap to upload picture",
                        color = Color.Black
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = Color.Black,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))


                    OutlinedTextField(
                        value = cowName,
                        onValueChange = { cowName = it },
                        label = { Text(text = "Cow's name") },
                        placeholder = { Text(text = "Enter cow's name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = motherName,
                        onValueChange = { motherName = it },
                        label = { Text(text = "Mother's name") },
                        placeholder = { Text(text = "Enter mother's name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        label = { Text(text = "Date of Birth") },
                        placeholder = { Text(text = "Enter date of birth") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = tagNumber,
                        onValueChange = { tagNumber = it },
                        label = { Text(text = "Tag number") },
                        placeholder = { Text(text = "Enter cow's tag number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedCowBreed,
                        onExpandedChange = { expandedCowBreed = !expandedCowBreed },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = cowBreed,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expandedCowBreed)
                            },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            label = { Text(text = "Select your cow's breed") }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCowBreed,
                            onDismissRequest = { expandedCowBreed = false }
                        ) {
                            cowBreedOptions.forEach { selectedCowBreed ->
                                DropdownMenuItem(
                                    text = { Text(selectedCowBreed) },
                                    onClick = {
                                        cowBreed = selectedCowBreed
                                        expandedCowBreed = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedCowStatus,
                        onExpandedChange = { expandedCowStatus = !expandedCowStatus },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = cowStatus,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(text = "Select cow status") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expandedCowStatus
                                )
                            },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCowStatus,
                            onDismissRequest = { expandedCowStatus = false }
                        ) {
                            cowStatusOptions.forEach { selectedStatus ->
                                DropdownMenuItem(
                                    text = { Text(selectedStatus) },
                                    onClick = {
                                        cowStatus = selectedStatus
                                        expandedCowStatus = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {}) {
                            Text(text = "CANCEL")
                        }

                        Button(onClick = {
                            cowViewModel.uploadCow(
                                imageUri = imageUri.value,
                                cowName = cowName,
                                motherName = motherName,
                                dob = dateOfBirth,
                                status = cowStatus,
                                breed = cowBreed,
                                tagNumber = motherName,
                                context = context,
                                navController = navController

                            )
                        }) {
                            Text(text = "SAVE")
                        }
                    }

                }
            }
        }
    }
}



@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun AddNewCowScreenPreview(){
    MyDairyFarmTheme {
        AddNewCowScreen(navController = rememberNavController())
    }
}
