package csci5708.mobilecomputing.healthharvest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RemoteViews
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import csci5708.mobilecomputing.healthharvest.receivers.WaterNotificationActionReceiver
import csci5708.mobilecomputing.healthharvest.services.WaterCounterNotificationService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class WaterTrackerActivity : AppCompatActivity() {

    var count: Int = 0
    private var waveImages: Array<Int> = arrayOf(
        R.drawable.wave0, R.drawable.wave1, R.drawable.wave2, R.drawable.wave3,
        R.drawable.wave4, R.drawable.wave5, R.drawable.wave6, R.drawable.wave7,
        R.drawable.wave8
    )
    private lateinit var waterFill: ImageView
    private lateinit var waterDatabaseHelper: WaterDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_tracker)
        val tvtext: TextView = findViewById(R.id.tvtext)
        val btnplus: Button = findViewById(R.id.btnplus)
        val btnminus: Button = findViewById(R.id.btnminus)
        waterFill = findViewById(R.id.waterFill)
        val waterDialogue: TextView = findViewById(R.id.waterDialogue)

        waterDatabaseHelper = WaterDatabaseHelper(this)
        count = waterDatabaseHelper.getTotalWaterIntakeForToday()
        tvtext.setText("" + count)
        waterDialogue.setText("Let's start!")
        setwaterDialogue(waterDialogue, count)

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
            waterDatabaseHelper.addWaterIntakeForToday()
            count = waterDatabaseHelper.getTotalWaterIntakeForToday()
            tvtext.setText("" + count)
            setwaterDialogue(waterDialogue, count)
            updateNotification(this, count)


        }

        btnminus.setOnClickListener {
            if(count > 0){
                waterDatabaseHelper.removeLatestWaterIntakeForToday()
                count = waterDatabaseHelper.getTotalWaterIntakeForToday()
                tvtext.setText("" + count)
                setwaterDialogue(waterDialogue, count)
                setwaterImage(waterDialogue, count)
                updateNotification(this, count)
            }


        }
    }

    private fun setwaterImage(waterDialogue: TextView, count: Int) {
        val maxWaterLevel = 8
        val clampedCount = count.coerceIn(0, maxWaterLevel)

        // Use Glide to load the image into the ImageView with Crossfade transition
        Glide.with(this)
            .load(waveImages[clampedCount])
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(waterFill)
    }

    private fun setwaterDialogue(waterDialogue: TextView, count: Int) {
        if (count.equals(0)) {
            waterDialogue.setText(getString(R.string.dialogue0))
        } else if (count.equals(1)) {
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

    override fun onDestroy() {
        super.onDestroy()
        Glide.with(this).clear(waterFill)
    }


    private fun getPendingIntentForAction(context: Context, action: String): PendingIntent {
        val intent = Intent(context, WaterNotificationActionReceiver::class.java).apply {
            this.action = action
        }

        // Use PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_MUTABLE
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun updateNotification(context: Context, count: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val contentView = RemoteViews(context.packageName, R.layout.notification_layout)

        contentView.setOnClickPendingIntent(R.id.btnIncrease, getPendingIntentForAction(context,
            WaterCounterNotificationService.ACTION_INCREASE
        ))
        contentView.setOnClickPendingIntent(R.id.btnDecrease, getPendingIntentForAction(context,
            WaterCounterNotificationService.ACTION_DECREASE
        ))


        // Update the count in the notification layout
        contentView.setTextViewText(R.id.txtCount, count.toString())

        // Rebuild the notification with the updated layout
        val notification = NotificationCompat.Builder(context, WaterCounterNotificationService.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setCustomContentView(contentView)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        notificationManager.notify(WaterCounterNotificationService.NOTIFICATION_ID, notification)
    }
}