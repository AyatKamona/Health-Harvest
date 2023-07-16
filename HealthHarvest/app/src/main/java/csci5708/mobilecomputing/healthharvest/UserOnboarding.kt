package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UserOnboarding : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_onboarding)

        var userName: EditText = findViewById(R.id.userName)
        var nextButton: Button = findViewById(R.id.nextButton)

        nextButton.setOnClickListener {
            if(userName.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, UserDashboard::class.java)
            intent.putExtra("userName", userName.text.toString())
            startActivity(intent)
        }
    }
}