package com.techlad.mydairyfarm.ui.theme.screens.cows

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.techlad.mydairyfarm.viewmodels.InseminatedCowViewModel
import java.util.*

@Composable
fun AddInseminatedCowScreen(navController: NavController, cowId: String?) {
    val viewModel: InseminatedCowViewModel = viewModel()
    val context = LocalContext.current

    var cowName by remember { mutableStateOf(TextFieldValue("")) }
    var bullBreed by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }

    // Load cow data if editing
    LaunchedEffect(cowId) {
        if (cowId != null) {
            isEditMode = true
            viewModel.getInseminatedCowById(cowId) { cow ->
                cow?.let {
                    cowName = TextFieldValue(it.cowName)
                    bullBreed = TextFieldValue(it.bullBreed)
                    selectedDate = it.inseminationDate
                }
            }
        }
    }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isEditMode) "Edit Inseminated Cow" else "Add Inseminated Cow",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = cowName,
            onValueChange = { cowName = it },
            label = { Text("Cow Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bullBreed,
            onValueChange = { bullBreed = it },
            label = { Text("Bull Breed") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text("Insemination Date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                TextButton(onClick = { datePickerDialog.show() }) {
                    Text("Select")
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isEditMode) {
                    viewModel.updateInseminatedCow(
                        cowId = cowId!!,
                        cowName = cowName.text,
                        bullBreed = bullBreed.text,
                        date = selectedDate,
                        context = context
                    )
                } else {
                    viewModel.addInseminatedCow(
                        cowName.text,
                        bullBreed.text,
                        selectedDate,
                        context
                    )
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditMode) "Update Record" else "Save Record")
        }
    }
}
