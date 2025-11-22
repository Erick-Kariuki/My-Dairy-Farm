package com.techlad.mydairyfarm.viewmodels


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.techlad.mydairyfarm.UiState.CowUiState
import com.techlad.mydairyfarm.models.CowModel
import com.techlad.mydairyfarm.navigation.ROUTE_DASHBOARD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream


class CowViewModel: ViewModel() {

    val db = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()

    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dmd33qk8e/image/upload"
    private val uploadPreset = "cow_img"

    private val _cowState = MutableStateFlow(CowUiState())
    val cowState: StateFlow<CowUiState> = _cowState.asStateFlow()






    fun uploadCow(
        imageUri: Uri?,
        cowName: String,
        motherName: String,
        dob: String,
        status: String,
        breed: String,
        tagNumber: String,
        context: Context,
        navController: NavController
    ) {
        if (cowName.isBlank() || motherName.isBlank() || dob.isBlank() || status.isBlank()
            || breed.isBlank() || tagNumber.isBlank()
        ) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            try {
                // Show progress (optional if you later add a loading UI state)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Uploading cow data...", Toast.LENGTH_SHORT).show()
                }

                // Upload image to Cloudinary (IO thread)
                val imageUrl = withContext(Dispatchers.IO) {
                    imageUri?.let { uploadToCloudinary(context, it) } ?: ""
                }

                val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
                val cowId = db.child("User").child(userId).child("Cows").push().key ?: ""

                val cow = CowModel(
                    cowId = cowId,
                    cowName = cowName,
                    motherName = motherName,
                    dateOfBirth = dob,
                    status = status,
                    breed = breed,
                    tagNumber = tagNumber,
                    imageUrl = imageUrl
                )

                // Save cow data to database
                db.child("User").child(userId).child("Cows").child(cowId)
                    .setValue(cow)
                    .await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Cow saved successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(ROUTE_DASHBOARD) {
                        popUpTo(0)
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Failed to save cow: ${e.message ?: "Unknown error"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw Exception("Image read failed")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset).build()
        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Upload failed")
        val responseBody = response.body?.string()
        val secureUrl =
            Regex("\"secure_url\":\"(.*?)\"").find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image")
    }

    private val _cowList = mutableStateListOf<CowModel>()
    val cowList: List<CowModel> = _cowList

    fun fetchCows(context: Context?) {
        val userId = auth.currentUser?.uid ?: return
        val cowsRef = db.child("User").child(userId).child("Cows")

        _cowState.value = CowUiState(isLoading = true)

        cowsRef.get().addOnSuccessListener { snapshot ->
            _cowList.clear()

            for (child in snapshot.children) {
                val cow = child.getValue(CowModel::class.java)
                cow?.let {
                    it.cowId = child.key.toString()

                    // Normalize the status (trim spaces and lowercase)
                    it.status = it.status.trim().lowercase()

                    // Debug log to verify the values retrieved
                    Log.d("COW_STATUS_DEBUG", "Fetched cow: ${it.cowName}, status: ${it.status}")

                    _cowList.add(it)
                }
            }

            if (_cowList.isEmpty()) {
                Toast.makeText(context, "No cows found in database", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener { e ->
            Toast.makeText(context, "Failed to retrieve cows: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }.addOnCompleteListener {
            _cowState.value = CowUiState(isLoading = false)
        }
    }


    fun deleteCow(cowId: String, context: Context) {
        val userId = auth.currentUser?.uid ?: return
        val ref = db.child("User").child(userId).child("Cows").child(cowId)
        ref.removeValue().addOnSuccessListener {
            _cowList.removeAll { it.cowId == cowId }
        }.addOnFailureListener {
            Toast.makeText(context, "Employee not deleted", Toast.LENGTH_SHORT).show()
        }

    }

    fun updateCow(
        cowId: String,
        imageUri: Uri?,
        cowName: String,
        motherName: String,
        dateOfBirth: String,
        status: String,
        breed: String,
        tagNumber: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) } ?: ""
                val updateCow = mapOf(
                    "cowName" to cowName,
                    "motherName" to motherName,
                    "dateOfBirth" to dateOfBirth,
                    "status" to status,
                    "breed" to breed,
                    "tagNumber" to tagNumber,
                    "imageUrl" to imageUrl
                )

                val ref = db.child("User").child(userId).child("Cows").child(cowId)
                ref.updateChildren(updateCow).await()
                fetchCows(context)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Cow updated successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(ROUTE_DASHBOARD) {
                        popUpTo(0)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Failed to update cow: ${e.message ?: "Unknown error"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }
    }

}