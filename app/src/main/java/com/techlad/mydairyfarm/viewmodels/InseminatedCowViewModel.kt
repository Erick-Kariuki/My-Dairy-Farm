package com.techlad.mydairyfarm.viewmodels


import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.techlad.mydairyfarm.models.InseminatedCowModel

class InseminatedCowViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference
    private val _inseminatedCowList = mutableStateListOf<InseminatedCowModel>()
    val inseminatedCowList: List<InseminatedCowModel> get() = _inseminatedCowList

    val isLoading = mutableStateOf(false)

    fun fetchInseminatedCows(context: Context) {
        val userId = auth.currentUser?.uid ?: return
        val ref = db.child("User").child(userId).child("InseminatedCows")

        isLoading.value = true
        ref.get().addOnSuccessListener { snapshot ->
            _inseminatedCowList.clear()
            for (child in snapshot.children) {
                val cow = child.getValue(InseminatedCowModel::class.java)
                cow?.let {
                    it.id = child.key.toString()
                    _inseminatedCowList.add(it)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch inseminated cows", Toast.LENGTH_SHORT).show()
        }.addOnCompleteListener {
            isLoading.value = false
        }
    }

    fun addInseminatedCow(cowName: String, bullBreed: String, date: String, context: Context) {
        val userId = auth.currentUser?.uid ?: return
        val ref = db.child("User").child(userId).child("InseminatedCows").push()

        val cow = InseminatedCowModel(
            id = ref.key ?: "",
            cowName = cowName,
            bullBreed = bullBreed,
            inseminationDate = date,
            pregnancyDuration = ""
        )

        ref.setValue(cow)
            .addOnSuccessListener {
                Toast.makeText(context, "Record added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to add record", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteInseminatedCow(id: String, context: Context) {
        val userId = auth.currentUser?.uid ?: return
        val ref = db.child("User").child(userId).child("InseminatedCows").child(id)
        ref.removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
            }
    }

    fun getInseminatedCowById(cowId: String, onResult: (InseminatedCowModel?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val ref = db.child("User").child(userId).child("InseminatedCows").child(cowId)

        ref.get().addOnSuccessListener { snapshot ->
            val cow = snapshot.getValue(InseminatedCowModel::class.java)
            onResult(cow)
        }.addOnFailureListener {
            onResult(null)
        }
    }

    fun updateInseminatedCow(
        cowId: String,
        cowName: String,
        bullBreed: String,
        date: String,
        context: Context
    ) {
        val userId = auth.currentUser?.uid ?: return
        val ref = db.child("User").child(userId).child("InseminatedCows").child(cowId)

        val updates = mapOf(
            "cowName" to cowName,
            "bullBreed" to bullBreed,
            "inseminationDate" to date
        )

        ref.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Record updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update record", Toast.LENGTH_SHORT).show()
            }
    }

}
