package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // start the user dashboard activity if the user name is already set
        val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
        val user = sharedPref.getString("userName", null)
        if(user != null) {
            val intent = Intent(this, UserDashboard::class.java)
            startActivity(intent)
        }

        val welcomeButton: Button = findViewById(R.id.welcomeButton)
        welcomeButton.setOnClickListener {
            val intent = Intent(this, UserOnboarding::class.java)
            startActivity(intent)
        }
    }
}