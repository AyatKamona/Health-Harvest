package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


object AppData{


    const val calorieGoal: Double = 3000.01

    const val waterGoal: Double = 8.0

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
        // fetch the username from shared preferences
        val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
        val user = sharedPref.getString("userName", "User")

        welcomeMessage.text = "$user's Tree"

        val height : TextView = findViewById(R.id.height_tree)
        val tree: ImageView = findViewById(R.id.tree_pic)
        val soilMoisture: TextView = findViewById(R.id.soilMoisture)
        val whatsNeeded: TextView = findViewById(R.id.whatsNeeded)
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
        val waterDatabaseHelper = WaterDatabaseHelper(this)
        val waterTracker : Double = waterDatabaseHelper.getTotalWaterIntakeForToday().toDouble()/ AppData.waterGoal
        val calorieTracker: Double = foodDatabaseHelper.getTotalCaloriesToday().toDouble()/AppData.calorieGoal
        //var calorieTracker: Double = 2.0/AppData.calorieGoal

        val timeWaterDif = (System.currentTimeMillis() - AppData.lastTimeWaterAdded)/(1000*60*60)
        val timeFoodDif = (System.currentTimeMillis() - AppData.lastTimeFoodAdded)/(1000*60*60)


        if(timeFoodDif < 0.5 || timeWaterDif < 0.5){

            AppData.whatsNeeded = ""

            if(timeFoodDif < 0.5 && timeWaterDif < 0.5) {

                if (calorieTracker < 0.33) tree.setImageResource(R.drawable.healthysappling)
                else if (0.66 > calorieTracker) tree.setImageResource(R.drawable.healthyplant)
                else if (1.4 > calorieTracker && calorieTracker>0.9)tree.setImageResource(R.drawable.healthytree)
                else if (calorieTracker > 1.5){
                    tree.setImageResource(R.drawable.deadtree)
                    AppData.whatsNeeded = "Hey there! Your tree is feeling a bit stuffed. Remember, moderation is key to a healthy lifestyle."
                }

            }

            else if(timeFoodDif < 0.5){

                if (calorieTracker < 0.33) tree.setImageResource(R.drawable.sunnysappling)
                else if (0.66 > calorieTracker) tree.setImageResource(R.drawable.sunnyplant)
                else if (1.4 > calorieTracker && calorieTracker>0.9)tree.setImageResource(R.drawable.sunnytree)
                else if (calorieTracker > 1.5){
                    tree.setImageResource(R.drawable.deadtree)
                    AppData.whatsNeeded = "Hey there! Your tree is feeling a bit stuffed. Remember, moderation is key to a healthy lifestyle."
                }
            }

            else{
                if (calorieTracker < 0.33) tree.setImageResource(R.drawable.rainsappling)
                else if (0.66 > calorieTracker) tree.setImageResource(R.drawable.rainplant)
                else if (1.4 > calorieTracker && calorieTracker>0.9)tree.setImageResource(R.drawable.raintree)
                else if (calorieTracker > 1.5){
                    tree.setImageResource(R.drawable.deadtree)
                    AppData.whatsNeeded = "Hey there! Your tree is feeling a bit stuffed. Remember, moderation is key to a healthy lifestyle."
                }
            }

        }

        else{

            if (calorieTracker < 0.33) tree.setImageResource(R.drawable.regular_sapling)
            else if (0.66 > calorieTracker) tree.setImageResource(R.drawable.regular_plant)
            else if (1.4 > calorieTracker && calorieTracker>0.9)tree.setImageResource(R.drawable.regular_tree)
            else if (calorieTracker > 1.5){
                tree.setImageResource(R.drawable.deadtree)
                AppData.whatsNeeded = "Your tree is feeling stuffed. Remember, moderation is key to a healthy lifestyle."
            }


        }

        if(waterTracker == 0.0) soilMoisture.text = "Moist Very Low"
        else if (waterTracker < 0.3) soilMoisture.text = "Moist Low"
        else if (waterTracker < 0.6) soilMoisture.text = "Moist Okay"
        else if (waterTracker < 0.9) soilMoisture.text = "Moist Great"
        else if (1.5 > waterTracker && waterTracker > 1.0)soilMoisture.text = "Moist is Perfect"
        else if (waterTracker > 1.5) soilMoisture.text = "Soil is waterlogged"

        val heightValue: Double = calorieTracker * 100
        val formattedNumber = String.format("%.2f", heightValue)
        height.text = "Height: $formattedNumber inches"

        if(waterTracker < 0.6 && calorieTracker < 0.9) AppData.whatsNeeded = "Don't forget to eat a balanced meal and drink enough water to see your tree flourish"
        else if (waterTracker < 0.6 && 0.9 < calorieTracker && calorieTracker < 1.5)  AppData.whatsNeeded = "Take a sip of water now and watch your tree thrive!"
        else if(waterTracker >= 0.6 && calorieTracker < 0.9) AppData.whatsNeeded = "Remember to fuel your body with nutritious food to help your tree flourish!"
        else if (waterTracker >= 0.6 && 0.9 < calorieTracker && calorieTracker < 1.5) AppData.whatsNeeded = "Great job! Your tree is in full bloom and so are you!"

    }
}
