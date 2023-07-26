package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FoodTrackerActivity : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout
    private lateinit var foodDatabaseHelper: FoodDatabaseHelper
    private lateinit var accordionLayout: LinearLayout
    private lateinit var layoutInflater: LayoutInflater



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_tracker)

        accordionLayout = findViewById(R.id.accordionLayout)
        layoutInflater = LayoutInflater.from(this)


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


        accordionLayout.removeAllViews()

        for (foodItem in foodList) {
            val accordionItemView = layoutInflater.inflate(R.layout.accordion_item, null)

            val foodNameTextView: TextView = accordionItemView.findViewById(R.id.foodNameTextView)
            val dateTakenTextView: TextView = accordionItemView.findViewById(R.id.dateTakenTextView)
            val caloriesTextView: TextView = accordionItemView.findViewById(R.id.caloriesTextView)
            val optionLayout: LinearLayout = accordionItemView.findViewById(R.id.optionLayout)
            val editImageView: ImageView = accordionItemView.findViewById(R.id.editImageView)
            val deleteImageView: ImageView = accordionItemView.findViewById(R.id.deleteImageView)


            foodNameTextView.text = foodItem.name
            dateTakenTextView.text = foodItem.dateTaken
            caloriesTextView.text = foodItem.calories.toString()

            // Handle edit action for this food item
            editImageView.setOnClickListener {
                val intent = Intent(this, EditFoodItemActivity::class.java)
                intent.putExtra("foodItemId", foodItem.id) // Pass the food item id to EditFoodItemActivity
                startActivity(intent)
            }

            // Handle delete action for this food item
            deleteImageView.setOnClickListener {
                foodDatabaseHelper.deleteFoodItem(foodItem.id)

                // Recalculate the total calories consumed today
                val updatedTotalCaloriesToday = foodDatabaseHelper.getTotalCaloriesToday()

                // Update the ProgressBar
                progressBar.progress = updatedTotalCaloriesToday

                // Update the accordionLayout by re-populating the accordion items
                updateAccordionLayout()
            }

            // Add the accordion item view to the accordionLayout
            accordionLayout.addView(accordionItemView)

            // Add a divider between accordion items
            val divider = layoutInflater.inflate(R.layout.divider, null)
            accordionLayout.addView(divider)
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

    fun updateAccordionLayout() {
        // Get the updated foodList and update the accordionLayout accordingly
        val updatedFoodList = foodDatabaseHelper.getAllFoodItems()

        accordionLayout.removeAllViews()

        for (foodItem in updatedFoodList) {
            // Same code as in the for loop above...
        }
    }




}