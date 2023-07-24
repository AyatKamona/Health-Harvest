package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import csci5708.mobilecomputing.healthharvest.Adapters.FoodAdapter
import csci5708.mobilecomputing.healthharvest.DataModels.FoodItem



class FoodTrackerActivity : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout
    private lateinit var foodDatabaseHelper: FoodDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_tracker)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val fabAddFood: FloatingActionButton = findViewById(R.id.fabAddFood)
        fabAddFood.setOnClickListener {
            val intent = Intent(this, AddFoodItemActivity::class.java)
            startActivity(intent)
        }

        foodDatabaseHelper = FoodDatabaseHelper(this)

        val foodList = foodDatabaseHelper.getAllFoodItems()

        // Get the total calories consumed today
        val totalCaloriesToday = foodDatabaseHelper.getTotalCaloriesToday()

        // Set the ProgressBar values
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.max = 2000 // Max value is 2000 (adjust this as needed)
        progressBar.progress = totalCaloriesToday


        tableLayout = findViewById(R.id.tableLayout)

        for (foodItem in foodList) {
            val tableRow = TableRow(this)

            val foodNameTextView = TextView(this)
            foodNameTextView.text = foodItem.name
            tableRow.addView(foodNameTextView)

            val dateTakenTextView = TextView(this)
            dateTakenTextView.text = foodItem.dateTaken
            tableRow.addView(dateTakenTextView)

            val caloriesTextView = TextView(this)
            caloriesTextView.text = foodItem.calories.toString()
            tableRow.addView(caloriesTextView)

            val optionLayout = LinearLayout(this)
            optionLayout.orientation = LinearLayout.HORIZONTAL

            val editImageView = ImageView(this)
            editImageView.setImageResource(R.drawable.ic_view)
            editImageView.setOnClickListener {
                // Handle edit action for this food item
                val intent = Intent(this, EditFoodItemActivity::class.java)
                intent.putExtra("foodItemId", foodItem.id) // Pass the food item id to EditFoodItemActivity
                startActivity(intent)
            }
            optionLayout.addView(editImageView)

            val deleteImageView = ImageView(this)
            deleteImageView.setImageResource(R.drawable.ic_delete)
            deleteImageView.setOnClickListener {
                // Handle delete action for this food item
                foodDatabaseHelper.deleteFoodItem(foodItem.id)
                tableLayout.removeView(tableRow) // Remove the row from the TableLayout
            }
            optionLayout.addView(deleteImageView)

            tableRow.addView(optionLayout)

            // Add the row to the TableLayout
            tableLayout.addView(tableRow)
        }

        //Bottom Navigation bar
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    val intent = Intent(this@FoodTrackerActivity, UserDashboard::class.java)
                    startActivity(intent)
                }

                R.id.food -> {
                    val intent = Intent(this@FoodTrackerActivity, FoodTrackerActivity::class.java)
                    startActivity(intent)
                }

                R.id.water -> {
                    val intent = Intent(this@FoodTrackerActivity, WaterTrackerActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }


}