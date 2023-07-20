package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView


object AppData{

    var calorieGoal: Int = 0
    var calorieAmount: Int = 0
    var cupsOfWater: Int = 0
    var height_of_tree: Int = 0

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
        setTreeType(tree, height)


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

    fun setTreeType(tree: ImageView, height: TextView){

        var calorieGoal: Int = AppData.calorieGoal
        var calorieAmount: Int = AppData.calorieGoal
        var waterIntake: Int = AppData.cupsOfWater
        var height_of_tree: Int = AppData.height_of_tree


        if(calorieGoal == -1){
            tree.setImageResource(R.drawable.boy)
            height.text = "Height: " + height_of_tree

        }
        else if(calorieGoal == 0){ }

    }
}

