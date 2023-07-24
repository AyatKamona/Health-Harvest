package csci5708.mobilecomputing.healthharvest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class WaterTrackerActivity : AppCompatActivity() {

    var count: Int = 0
    private lateinit var waterIcon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_tracker)

        var tvtext: TextView = findViewById(R.id.tvtext)
        var btnplus: Button = findViewById(R.id.btnplus)
        var btnminus: Button = findViewById(R.id.btnminus)
        var progressBar: ProgressBar = findViewById(R.id.progressBar)
        waterIcon = findViewById(R.id.waterIcon)
        var waterDialogue: TextView = findViewById(R.id.waterDialogue)

        tvtext.setText("" + count)
        waterDialogue.setText("Let's start!")

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)


        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    val intent = Intent(this@WaterTrackerActivity, UserDashboard::class.java)
                    startActivity(intent)
                    true
                }

                R.id.food -> {
                    val intent = Intent(this@WaterTrackerActivity, FoodTrackerActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.water -> {
                    val intent = Intent(this@WaterTrackerActivity, WaterTrackerActivity::class.java)
                    startActivity(intent)
                    true
                }
            }
            true
        }

        btnplus.setOnClickListener {
            tvtext.setText("" + ++count)
            progressBar.setProgress(count * 10)
            setwaterDialogue(waterDialogue, count)
            animateIcons(waterDialogue)
            updateWater()


        }

        btnminus.setOnClickListener {
            if(count > 0){
                tvtext.setText("" + --count)
                progressBar.setProgress(count * 10)
                setwaterDialogue(waterDialogue, count)
                animateIcons(waterDialogue)
                updateWater()
            }


        }
    }

    private fun animateIcons(waterDialogue: TextView) {

        val refreshRotation = ObjectAnimator.ofFloat(waterIcon, View.ROTATION, -45f, 0f, 45f, 0f)
        refreshRotation.duration = 1000
        refreshRotation.interpolator = AccelerateDecelerateInterpolator()

        val starScaleX = ObjectAnimator.ofFloat(waterDialogue, View.SCALE_X, 1f, 1.2f, 1f)
        starScaleX.duration = 500
        starScaleX.repeatCount = 1
        starScaleX.repeatMode = ObjectAnimator.REVERSE

        val starScaleY = ObjectAnimator.ofFloat(waterDialogue, View.SCALE_Y, 1f, 1.2f, 1f)
        starScaleY.duration = 500
        starScaleY.repeatCount = 1
        starScaleY.repeatMode = ObjectAnimator.REVERSE

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(refreshRotation, starScaleX, starScaleY)
        animatorSet.start()
    }

    private fun setwaterDialogue(waterDialogue: TextView, count: Int) {
        if (count.equals(1)) {
            waterDialogue.setText(getString(R.string.dialogue1))
        } else if (count.equals(2)) {
            waterDialogue.setText(getString(R.string.dialogue2))
        } else if (count.equals(3)) {
            waterDialogue.setText(getString(R.string.dialogue3))
        } else if (count.equals(4)) {
            waterDialogue.setText(getString(R.string.dialogue4))
        } else if (count.equals(5)) {
            waterDialogue.setText(getString(R.string.dialogue5))
        } else if (count.equals(6)) {
            waterDialogue.setText(getString(R.string.dialogue6))
        } else if (count.equals(7)) {
            waterDialogue.setText(getString(R.string.dialogue7))
        } else if (count.equals(8)) {
            waterDialogue.setText(getString(R.string.dialogue8))
        } else {
            waterDialogue.setText(getString(R.string.dialogue9))
        }
    }

    fun updateWater(){

        AppData.cupsOfWater = count.toDouble()
        AppData.lastTimeWaterAdded = System.currentTimeMillis()
    }
}