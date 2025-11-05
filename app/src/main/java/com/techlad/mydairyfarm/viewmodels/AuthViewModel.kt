package com.techlad.mydairyfarm.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.techlad.mydairyfarm.UiState.AuthUiState
import com.techlad.mydairyfarm.models.UserModel
import com.techlad.mydairyfarm.navigation.ROUTE_DASHBOARD
import com.techlad.mydairyfarm.navigation.ROUTE_LOGIN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()


    fun signup(farmName:String,
               email: String,
               password: String,
               navController: NavController,
               confirmPassword:String,
               context: Context){
        if (farmName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword){
            Toast.makeText(context,"Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }
        _authState.value = AuthUiState(isLoading = true)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: ""
                val user = UserModel(farmName = farmName, email = email, userId = userId)

                saveUserToDatabase(user, navController, context)

            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                _authState.value = AuthUiState(isLoading = false)
            }
        }
    }

    fun saveUserToDatabase(user: UserModel,
                           navController: NavController,
                           context: Context){
        val dbRef = FirebaseDatabase.getInstance().getReference("User/${user.userId}")
        dbRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(ROUTE_LOGIN){
                    popUpTo(0)
                    _authState.value = AuthUiState(isLoading = false)
                }
            }else{
                Toast.makeText(context, "Failed to register user", Toast.LENGTH_SHORT).show()
                _authState.value = AuthUiState(isLoading = false)
            }
        }
    }

    fun login(email: String,
              password: String,
              navController: NavController,
              context: Context){
        if (email.isEmpty() or password.isEmpty()){
            Toast.makeText(context, "Both email and password are required", Toast.LENGTH_SHORT).show()
            return
        }else
        _authState.value = AuthUiState(isLoading = true)
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                navController.navigate(ROUTE_DASHBOARD){
                    popUpTo(0)
                    _authState.value = AuthUiState(isLoading = false)
                }

            }else{
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                _authState.value = AuthUiState(isLoading = false)
            }
        }
    }

    fun resetPassword(email: String, context: Context) {
        if (email.isEmpty()) {
            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Password reset link sent to your email",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Failed to send reset link: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}