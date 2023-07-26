package csci5708.mobilecomputing.healthharvest.DataModels

data class FoodItem(
    val id: Long,
    val name: String,
    val DateTaken: String,
    val calories: Int,
    val quantity: Int,
    val timeTaken: String
)