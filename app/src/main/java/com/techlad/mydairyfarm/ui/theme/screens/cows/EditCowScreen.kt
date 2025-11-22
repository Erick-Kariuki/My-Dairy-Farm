package com.techlad.mydairyfarm.ui.theme.screens.cows

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.techlad.mydairyfarm.models.CowModel
import com.techlad.mydairyfarm.viewmodels.CowViewModel

@Composable
fun EditCowScreen(navController: NavController, cowId: String) {
    val cowViewModel: CowViewModel = viewModel()
    var cow by remember { mutableStateOf<CowModel?>(null) }
    LaunchedEffect(cowId) {
        val ref = cowViewModel.db.child("User").child(cowViewModel.auth.currentUser?.uid ?: "")
            .child("Cows").child(cowId)
        ref.get().addOnSuccessListener { snapshot ->
            cow = snapshot.getValue(CowModel::class.java)
            cow?.cowId = cowId
        }

    }
    if (cow == null) {
        // Show loading or error state
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var cowName by remember { mutableStateOf(cow?.cowName ?: "")  }
    var motherName by remember { mutableStateOf(cow?.motherName ?: "") }
    var dateOfBirth by remember { mutableStateOf(cow?.dateOfBirth ?: "") }
    var status by remember { mutableStateOf(cow?.status ?: "")}
    var tagNumber by remember { mutableStateOf(cow?.tagNumber ?: "")}
    var breed by remember { mutableStateOf(cow?.breed ?: "")}

    val imageUri = remember { mutableStateOf<Uri?>(null)  }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri -> imageUri.value = uri  }
    }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFCE4EC), Color(0xFFF8BBD0))
                )
            )
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Update Cow",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF880E4F)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .size(140.dp)
                        .clickable{ launcher.launch("image/*")}
                        .shadow(8.dp, CircleShape)
                ) {
                    AnimatedContent(
                        targetState = imageUri.value,
                        label = "Image picker animation"
                    ) { targetUri ->
                        AsyncImage(
                            model = imageUri.value ?: cow!!.imageUrl,
                            contentDescription = "Cow Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Text(
                    text = "Tap to change picture",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Divider(
                    modifier = Modifier.padding(vertical = 20.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
                val fieldModifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)

                val fieldShape = RoundedCornerShape(14.dp)

                OutlinedTextField(
                    value = cowName,
                    onValueChange = { cowName = it },
                    label = { Text(text = "Cow Name")},
                    shape = fieldShape,
                    modifier = fieldModifier
                )

                OutlinedTextField(
                    value = motherName,
                    onValueChange = { motherName = it },
                    label = { Text(text = "Mother Name")},
                    shape = fieldShape,
                    modifier = fieldModifier
                )

                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text(text = "Date of Birth")},
                    shape = fieldShape,
                    modifier = fieldModifier
                )

                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text(text = "Status")},
                    shape = fieldShape,
                    modifier = fieldModifier
                )

                OutlinedTextField(
                    value = tagNumber,
                    onValueChange = { tagNumber = it },
                    label = { Text(text = "Tag Number")},
                    shape = fieldShape,
                    modifier = fieldModifier
                )

                OutlinedTextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = { Text(text = "Breed")},
                    shape = fieldShape,
                    modifier = fieldModifier
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors( containerColor = Color.LightGray),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.width(140.dp)
                    ) {
                        Text("Go Back", color = Color.DarkGray)
                    }

                    Button(
                        onClick = {
                            cowViewModel.updateCow(
                                cowId = cowId,
                                imageUri = imageUri.value,
                                cowName = cowName,
                                motherName = motherName,
                                dateOfBirth = dateOfBirth,
                                status = status,
                                breed = breed,
                                tagNumber = tagNumber,
                                context = context,
                                navController = navController
                            )
                        }
                    ) { Text("Update", color = Color.White) }
                }


            }
        }
    }
}