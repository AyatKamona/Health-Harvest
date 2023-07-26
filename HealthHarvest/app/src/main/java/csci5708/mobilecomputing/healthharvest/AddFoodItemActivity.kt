package csci5708.mobilecomputing.healthharvest

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import java.util.Calendar
import kotlin.random.Random
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import csci5708.mobilecomputing.healthharvest.DataModels.FoodItem

class AddFoodItemActivity : AppCompatActivity() {
    private lateinit var foodNameEditText: AutoCompleteTextView
    private lateinit var timeTakenEditText: TextView
    private lateinit var caloriesEditText: TextView
    private lateinit var quantityEditText: TextView
    private lateinit var dateEditText: TextView
    private lateinit var addFoodButton: Button
    private lateinit var cancelFoodButton: Button

    private lateinit var foodDatabaseHelper: FoodDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_item)

        foodNameEditText = findViewById(R.id.foodNameEditText)
        timeTakenEditText = findViewById(R.id.dateTakenEditText)
        caloriesEditText = findViewById(R.id.caloriesEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        dateEditText = findViewById(R.id.dateEditText)

        addFoodButton = findViewById(R.id.addFoodButton)
        cancelFoodButton = findViewById(R.id.cancelAddFoodButton)

        foodDatabaseHelper = FoodDatabaseHelper(this)

        // Get all food items from the database
        val foodList = foodDatabaseHelper.getAllFoodItems()

        // Extract food names from the food items list
        val foodNames = foodList.map { it.name }.toTypedArray()

        // Create an ArrayAdapter to provide suggestions to the AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, foodNames)

        // Set the adapter to the AutoCompleteTextView
        foodNameEditText.setAdapter(adapter)

        addFoodButton.setOnClickListener {
            saveFoodItem()
        }

        cancelFoodButton.setOnClickListener {
            val intent = Intent(this, FoodTrackerActivity::class.java)
            startActivity(intent)
            finish()
        }

        timeTakenEditText.setOnClickListener {
            showDatePickerDialog(timeTakenEditText)
        }

        dateEditText.setOnClickListener {
            showDatePickerDialog(dateEditText)
        }
    }

    private fun saveFoodItem() {
        val name = foodNameEditText.text.toString()
        val dateTaken = timeTakenEditText.text.toString()
        val calories = caloriesEditText.text.toString().toIntOrNull()
        val quantity = quantityEditText.text.toString().toIntOrNull()
        val date = dateEditText.text.toString()

        if (name.isBlank() || dateTaken.isBlank() || calories == null || quantity == null || date.isBlank()) {
            showToast("Please fill all the fields.")
            return
        }

        val randomId = Random.nextLong()

        val foodItem = FoodItem(randomId, name, dateTaken, calories, quantity, date)

        val newRowId = foodDatabaseHelper.insertFoodItem(foodItem)

        if (newRowId != -1L) {
            showToast("Entry successfully added.")
            // Move to FoodTrackerActivity after the toast is complete
            val intent = Intent(this, FoodTrackerActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            showToast("Error adding food item.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showDatePickerDialog(view: View) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                if (view == timeTakenEditText) {
                    timeTakenEditText.text = formattedDate
                } else if (view == dateEditText) {
                    dateEditText.text = formattedDate
                }
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}
