package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView


object AppData{


    const val calorieGoal: Double = 3000.01

    const val waterGoal: Double = 8.0
    var cupsOfWater: Double = 0.0

    var lastTimeFoodAdded: Long = 0
    var lastTimeWaterAdded: Long = 0

    var whatsNeeded: String = ""


}


class UserDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val welcomeMessage: TextView = findViewById(R.id.welcomeMessage)
        val user = intent.getStringExtra("userName")

        welcomeMessage.text = "Welcome $user!"

        var height : TextView = findViewById(R.id.height_tree)
        var tree: ImageView = findViewById(R.id.tree_pic)
        var soilMoisture: TextView = findViewById(R.id.soilMoisture)
        var whatsNeeded: TextView = findViewById(R.id.whatsNeeded)
        setTreeType(tree, height, soilMoisture)
        whatsNeeded.text = AppData.whatsNeeded




        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    val intent = Intent(this@UserDashboard, UserDashboard::class.java)
                    startActivity(intent)
                }

                R.id.food -> {
                    val intent = Intent(this@UserDashboard, FoodTrackerActivity::class.java)
                    startActivity(intent)
                }

                R.id.water -> {
                    val intent = Intent(this@UserDashboard, WaterTrackerActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    fun setTreeType(tree: ImageView, height: TextView, soilMoisture: TextView){

        val foodDatabaseHelper = FoodDatabaseHelper(this)
        var waterTracker : Double = AppData.cupsOfWater/ AppData.waterGoal
        var calorieTracker: Double = foodDatabaseHelper.getTotalCaloriesToday().toDouble()/AppData.calorieGoal
        //var calorieTracker: Double = 2.0/AppData.calorieGoal

        var timeWaterDif = (System.currentTimeMillis() - AppData.lastTimeWaterAdded)/(1000*60*60)
        var timeFoodDif = (System.currentTimeMillis() - AppData.lastTimeFoodAdded)/(1000*60*60)


        if(timeFoodDif < 0.5 || timeWaterDif < 0.5){

            AppData.whatsNeeded = ""

            if(timeFoodDif < 0.5 && timeWaterDif < 0.5) {

                if (calorieTracker < 0.33) tree.setImageResource(R.drawable.healthysappling)
                else if (0.66 > calorieTracker) tree.setImageResource(R.drawable.healthyplant)
                else tree.setImageResource(R.drawable.healthytree)

            }

            else if(timeFoodDif < 0.5){

                if (calorieTracker < 0.33) tree.setImageResource(R.drawable.sunnysappling)
                else if (0.66 > calorieTracker) tree.setImageResource(R.drawable.sunnyplant)
                else tree.setImageResource(R.drawable.sunnytree)
            }

            else{
                if (calorieTracker < 0.33) tree.setImageResource(R.drawable.rainsappling)
                else if (0.66 > calorieTracker) tree.setImageResource(R.drawable.rainplant)
                else tree.setImageResource(R.drawable.raintree)
            }

        }

        else{

            if (calorieTracker < 0.33) tree.setImageResource(R.drawable.regular_sapling)
            else if (0.66 > calorieTracker) tree.setImageResource(R.drawable.regular_plant)
            else tree.setImageResource(R.drawable.regular_tree)


        }

        if(waterTracker == 0.0) soilMoisture.text = "Moist Very Low"
        else if (waterTracker < 0.3) soilMoisture.text = "Moist Low"
        else if (waterTracker < 0.6) soilMoisture.text = "Moist Okay"
        else if (waterTracker < 0.9) soilMoisture.text = "Moist Great"
        else soilMoisture.text = "Moist is Perfect"

        var heightValue: Double = calorieTracker * 100
        val formattedNumber = String.format("%.2f", heightValue)
        height.text = "Height: $formattedNumber inches"

    }
}
