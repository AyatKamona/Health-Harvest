package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val welcomeMessage: TextView = findViewById(R.id.welcomeMessage)
        val user = intent.getStringExtra("userName")

        welcomeMessage.text = "Welcome $user!"

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
}