package csci5708.mobilecomputing.healthharvest.DataModels

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FoodItem(
    val id: Long,
    val name: String,
    val DateTaken: String,
    val calories: Int,
    val quantity: Int,
    val timeTaken: String,
) {
    companion object {
        // Function to get the current time in HH:mm format
        fun getCurrentTime(): String {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return dateFormat.format(Date())
        }

        // Function to get the current date in yyyy-MM-dd format
        fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
}