package csci5708.mobilecomputing.healthharvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import csci5708.mobilecomputing.healthharvest.DataModels.FoodItem
import org.w3c.dom.Text


class EditFoodItemActivity : AppCompatActivity() {
    private lateinit var foodDatabaseHelper: FoodDatabaseHelper
    private lateinit var editCaloriesEditText : TextView
    private lateinit var editDateTakenEditText : TextView
    private lateinit var editFoodNameEditText : TextView
    private lateinit var  saveButton : Button
    private lateinit var discardButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food_item)

        val foodItemId = intent.getLongExtra("foodItemId", -1)

        foodDatabaseHelper = FoodDatabaseHelper(this)
        val foodItem = foodDatabaseHelper.getFoodItem(foodItemId)

        editCaloriesEditText = findViewById(R.id.editCaloriesEditText)
        editFoodNameEditText = findViewById(R.id.editFoodNameEditText)
        editDateTakenEditText = findViewById(R.id.editDateTakenEditText)
        saveButton = findViewById(R.id.saveButton)
        discardButton = findViewById(R.id.discardButton)

        // Pre-fill the text fields with existing record data
        if (foodItem != null) {
            editFoodNameEditText.setText(foodItem.name)
        }
        if (foodItem != null) {
            editDateTakenEditText.setText(foodItem.dateTaken)
        }
        if (foodItem != null) {
            editCaloriesEditText.setText(foodItem.calories.toString())
        }

        saveButton.setOnClickListener {
            val newName = editFoodNameEditText.text.toString()
            val newDateTaken = editDateTakenEditText.text.toString()
            val newCalories = editCaloriesEditText.text.toString().toInt()

            // Update the record in the database
            val updatedFoodItem = FoodItem(foodItemId, newName, newDateTaken, newCalories)
            foodDatabaseHelper.updateFoodItem(updatedFoodItem)

            navigateToFoodTrackerActivity()
        }

        discardButton.setOnClickListener {
            // Discard changes and navigate back to FoodTrackerActivity
            navigateToFoodTrackerActivity()
        }
    }

    private fun navigateToFoodTrackerActivity() {
        val intent = Intent(this, FoodTrackerActivity::class.java)
        startActivity(intent)
        finish() // Call finish to remove EditFoodItemActivity from the back stack
    }
}