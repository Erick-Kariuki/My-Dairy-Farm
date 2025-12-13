package com.techlad.mydairyfarm.viewmodels


import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.techlad.mydairyfarm.models.InseminatedCowModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class InseminatedCowViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference
    private val _inseminatedCowList = mutableStateListOf<InseminatedCowModel>()
    val inseminatedCowList: List<InseminatedCowModel> get() = _inseminatedCowList

    val isLoading = mutableStateOf(false)

    var searchQuery = mutableStateOf("")
        private set

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun getFilteredInseminatedCows(): List<InseminatedCowModel> {
        if (searchQuery.value.isBlank()) {
            return inseminatedCowList
        }

        return inseminatedCowList.filter { cow ->
            cow.cowName.trim().lowercase()
                .contains(searchQuery.value.trim().lowercase())
        }
    }


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


    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val GESTATION_PERIOD_DAYS = 283
    fun calculatePregnancyDuration(inseminationDate: String): String {
        if (inseminationDate.isBlank()) return "Unknown"

        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDate: Date = sdf.parse(inseminationDate) ?: return "Unknown"

            val today = Date()
            val diffInMillis = today.time - startDate.time

            if (diffInMillis < 0) return "Invalid date"

            // Convert to days
            val totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

            // Pregnancy duration of a cow is ~283 days
            val fullTermDays = 283

            return if (totalDays <= fullTermDays) {
                val months = totalDays / 30
                val days = totalDays % 30
                "$months months $days days pregnant"
            } else {
                val overdue = totalDays - fullTermDays
                "Overdue by $overdue days"
            }

        }catch (e: Exception){
           // e.printStackTrace()
            "Unknown"
        }
    }

    fun calculateDueDate(inseminationDate: String): String{
        return try {
            val date = sdf.parse(inseminationDate) ?: return "Unknown"
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_YEAR, GESTATION_PERIOD_DAYS)

            sdf.format(calendar.time)
        } catch (e: Exception) {
            "Unknown"
        }
    }


    fun determineStatusColor(inseminationDate: String): Color {
        return try {
            val startDate = sdf.parse(inseminationDate) ?: return Color.Gray
            val today = Date()
            val diff = today.time - startDate.time
            val days = TimeUnit.MILLISECONDS.toDays(diff)

            return when {
                days > GESTATION_PERIOD_DAYS -> Color.Red               // Overdue
                days >= 250 -> Color(0xFFFFA000)                        // Due soon (amber)
                else -> Color(0xFF2E7D32)                               // Healthy (green)
            }
        } catch (e: Exception) {
            Color.Gray
        }
    }

    fun pregnancyProgress(inseminationDate: String): Float {
        return try {
            val start = sdf.parse(inseminationDate) ?: return 0f
            val today = Date()

            val diff = today.time - start.time
            val daysPassed = TimeUnit.MILLISECONDS.toDays(diff).toFloat()

            (daysPassed / GESTATION_PERIOD_DAYS).coerceIn(0f, 1f) // clamp 0–1
        } catch (e: Exception) {
            0f
        }
    }

    fun sortByDueDate(cows: List<InseminatedCowModel>): List<InseminatedCowModel> {
        return cows.sortedBy { cow ->
            try {
                val insemination = sdf.parse(cow.inseminationDate)
                val calDate = Calendar.getInstance().apply {
                    time = insemination!!
                    add(Calendar.DAY_OF_YEAR, GESTATION_PERIOD_DAYS)
                }
                calDate.time
            } catch (e: Exception) {
                Date(Long.MAX_VALUE) // fallback so invalid dates go to the bottom
            }
        }
    }

}
