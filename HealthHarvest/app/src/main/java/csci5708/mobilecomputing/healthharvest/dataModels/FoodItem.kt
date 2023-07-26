package csci5708.mobilecomputing.healthharvest.dataModels

data class FoodItem(
    val id: Long,        // Changed type to Long
    val name: String,
    val dateTaken: String,
    val calories: Int,
)