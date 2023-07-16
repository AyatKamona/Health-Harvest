package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var welcomeButton: Button = findViewById(R.id.welcomeButton)
        welcomeButton.setOnClickListener {
            val intent = Intent(this, UserOnboarding::class.java)
            startActivity(intent)
        }
    }
}