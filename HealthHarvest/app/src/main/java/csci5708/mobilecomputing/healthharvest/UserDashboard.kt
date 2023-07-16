package csci5708.mobilecomputing.healthharvest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView


var tree_type: Int = -1
class UserDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val welcomeMessage: TextView = findViewById(R.id.welcomeMessage)
        val user = intent.getStringExtra("userName")

        welcomeMessage.text = "Welcome $user!"

        var tree: ImageView = findViewById(R.id.tree_pic)
        set_tree_type(tree)


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

    fun set_tree_type(tree: ImageView){

        if(tree_type == -1){ tree.setImageResource(R.drawable.boy)}
        else if(tree_type == 0){ }
        else if(tree_type == 1){ }
        else if(tree_type == 2){ }
        else if(tree_type == 3){ }
        else if(tree_type == 4){ }
        else if(tree_type == 5){ }
        else if(tree_type == 6){ }
        else if(tree_type == 7){ }
    }
}

