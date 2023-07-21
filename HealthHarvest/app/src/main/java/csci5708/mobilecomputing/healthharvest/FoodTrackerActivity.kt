package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView



class FoodTrackerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_tracker)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        //updateCalorie(amountAdded)
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

    fun updateCalorie(calorieAdded: Int){

        AppData.calorieAmount+=calorieAdded
        AppData.lastTimeFoodAdded = System.currentTimeMillis()
    }
}