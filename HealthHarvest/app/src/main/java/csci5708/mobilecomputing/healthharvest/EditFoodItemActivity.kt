package csci5708.mobilecomputing.healthharvest

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import csci5708.mobilecomputing.healthharvest.DataModels.FoodItem
import java.util.Calendar


class EditFoodItemActivity : AppCompatActivity() {
    private lateinit var foodDatabaseHelper: FoodDatabaseHelper
    private lateinit var editCaloriesEditText : TextView
    private lateinit var editDateTakenEditText : TextView
    private lateinit var editFoodNameEditText : AutoCompleteTextView
    private lateinit var  saveButton : Button
    private lateinit var discardButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food_item)

        val foodItemId = intent.getLongExtra("foodItemId", -1)

        foodDatabaseHelper = FoodDatabaseHelper(this)
        val foodItem = foodDatabaseHelper.getFoodItem(foodItemId)

        editCaloriesEditText = findViewById(R.id.editCaloriesEditText)
        //editFoodNameEditText = findViewById(R.id.editFoodNameEditText)
        // Get all food items from the database
        val foodList = foodDatabaseHelper.getAllFoodItems()

        // Extract food names from the food items list
        val foodNames = foodList.map { it.name }.toTypedArray()

        // Create an ArrayAdapter to provide suggestions to the AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, foodNames)

        editFoodNameEditText = findViewById(R.id.editFoodNameEditText)

        // Set the adapter to the AutoCompleteTextView
        editFoodNameEditText.setAdapter(adapter)
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

    // Function to show TimePickerDialog
    fun showTimePickerDialog() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                // Format the selected time and set it to the "Date Taken" EditText
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                editDateTakenEditText.setText(selectedTime)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }
}