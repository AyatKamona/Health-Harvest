package csci5708.mobilecomputing.healthharvest

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import csci5708.mobilecomputing.healthharvest.DataModels.FoodItem
import java.util.Calendar
import kotlin.random.Random

class AddFoodItemActivity : AppCompatActivity() {
    private lateinit var foodNameEditText: EditText
    private lateinit var dateTakenEditText: EditText
    private lateinit var caloriesEditText: EditText
    private lateinit var optionEditText: EditText
    private lateinit var addFoodButton: Button
    private lateinit var cancelFoodButton : Button

    private lateinit var foodDatabaseHelper: FoodDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_item)

        foodNameEditText = findViewById(R.id.foodNameEditText)
        dateTakenEditText = findViewById(R.id.dateTakenEditText)
        caloriesEditText = findViewById(R.id.caloriesEditText)

        addFoodButton = findViewById(R.id.addFoodButton)
        cancelFoodButton = findViewById(R.id.cancelAddFoodButton)

        foodDatabaseHelper = FoodDatabaseHelper(this)

        addFoodButton.setOnClickListener {
            saveFoodItem()
        }

        cancelFoodButton.setOnClickListener{
            val intent = Intent(this, FoodTrackerActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun saveFoodItem() {
        val name = foodNameEditText.text.toString()
        val dateTaken = dateTakenEditText.text.toString()
        val calories = caloriesEditText.text.toString().toIntOrNull()


        if (name.isBlank() || dateTaken.isBlank() || calories == null) {
            showToast("Please fill all the fields.")
            return
        }

        val randomId = Random.nextLong()


        val foodItem = FoodItem(randomId,name , dateTaken, calories)

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
                dateTakenEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}