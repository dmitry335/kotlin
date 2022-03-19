package org.hyperskill.stopwatch

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlin.random.Random


const val CHANNEL_ID = "org.hyperskill"


class MainActivity : AppCompatActivity() {



//    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.getMainLooper())

        val btnStart = findViewById<Button>(R.id.startButton)
        val btnReset = findViewById<Button>(R.id.resetButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        val textView = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.isIndeterminate = true
        var counter = 0
        var counting = false
        var upperLimit: Int = 0



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Delivery status"
            val descriptionText = "Your delivery status"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_counter_01)
                .setContentTitle("Pomodoro")
                .setContentText("Time is up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager





        val allertView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null, false)

        val dialog = AlertDialog.Builder(this)
                .setTitle("Set upper time limit")
                .setView(allertView)
                .setPositiveButton(android.R.string.ok) { _, _ ->

                    val upperLimitEditText = allertView.findViewById<EditText>(R.id.upperLimitEditText)
                    val upperLimitTxt = upperLimitEditText.text.toString()
                    upperLimit = if(upperLimitTxt == "") 0 else upperLimitTxt.toInt()
                }
                .setNegativeButton(android.R.string.cancel, null)

        settingsButton.setOnClickListener {
            dialog.show()
        }


        fun counterToDisplay() {
            val minutes = counter / 60
            val seconds = counter % 60
            val minutesSeparator = if (minutes < 10) "0" else ""
            val secondsSeparator = if (seconds < 10) "0" else ""
            textView.text = "$minutesSeparator$minutes:$secondsSeparator$seconds"
        }



        fun increaseOne() {
            counter++
            val r = Random.nextInt(0, 255)
            val g = Random.nextInt(0, 255)
            val b = Random.nextInt(0, 255)
            progressBar.indeterminateTintList = ColorStateList.valueOf(rgb(r,g,b))
            counterToDisplay()

            if (upperLimit == 0 || upperLimit > counter) {
            } else {
//                handler.removeCallbacks(runnableCounter)
                textView.setTextColor(Color.RED)
                notificationManager.notify(393939, notificationBuilder.build())
            }

        }

        val runnableCounter: Runnable = object : Runnable {
            override fun run() {
                increaseOne()
                handler.postDelayed(this, 1000)
            }
        }





        fun reset() {
            handler.removeCallbacks(runnableCounter)
            counter = 0
            counterToDisplay()
            counting = false
            progressBar.visibility = View.INVISIBLE
            settingsButton.isEnabled = true
            btnStart.isEnabled = true
            textView.setTextColor(Color.parseColor("#999999"))


        }








        btnStart.setOnClickListener {
            if (!counting) {
                counting = true
                handler.postDelayed(runnableCounter, 1000)
                progressBar.visibility = View.VISIBLE
                settingsButton.isEnabled = false
                btnStart.isEnabled = false
            }
        }

        btnReset.setOnClickListener {
            reset()
        }





    }
}