package com.example.death

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.death.databinding.ActivityMainBinding
import kotlinx.datetime.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    private val calendar = Calendar.getInstance()
    private var yearOfBirth: Int = 0
    private var monthOfBirth: Int = 0
    private var dayOfBirth: Int = 0
    private var age: Int = 0
    private var timeRemaining = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)


        //////////////    Notification    //////////////
        // Notification settings
        val channelId = "my_channel_id"
        val channelName = "My Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
            description = "My Notification Channel"
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Life ends in")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // show on lock screen
            .setAutoCancel(false)

        //////////////    #Notification#    //////////////

        // Set up the date picker button
        b.pickDate.setOnClickListener {
            showDatePickerDialog()
        }

        //////////////    get Age selected    //////////////
        b.pickAge.minValue = 18
        b.pickAge.maxValue = 80
        b.pickAge.wrapSelectorWheel = false
        b.pickAge.setOnValueChangedListener { _, _, newVal ->
            age = newVal
        }
        //////////////    #get Age selected#    //////////////

        // Start notification
        b.btn.setOnClickListener {

            // Calculating Time Remaining
            val countdownDate = Calendar.getInstance()
            countdownDate.set(2038, Calendar.NOVEMBER, 14, 0, 0, 0)
            val countdownInMillis = countdownDate.timeInMillis - Calendar.getInstance().timeInMillis

            val countDownTimer = object : CountDownTimer(countdownInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000).toInt()
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val days = hours / 24
                    val months = days / 30
                    val years = months / 12

                    val countdownString = String.format(
                        "%02d Y %02d M %02d D %02d H %02d Min %02d Sec",
                        years,
                        months % 12,
                        days % 30,
                        hours % 24,
                        minutes % 60,
                        seconds % 60
                    )

                    notificationBuilder.setContentText(countdownString)

                    // Update the notification with the new text
                    notificationManager.notify(notificationId, notificationBuilder.build())
                }

                override fun onFinish() {
                    // Countdown finished, cancel the notification
                    notificationManager.cancel(notificationId)
                }
            }

// Start the countdown timer and update the notification text periodically
            countDownTimer.start()

            // Make sure that the date is selected before launching the notification
//            if (yearOfBirth != 0) {
                // send notification
//                notificationManager.notify(notificationId, notificationBuilder.build())
//            } else msg("Please select a date") // Select date first y7bibi
        }

    }

    private fun showDatePickerDialog() {
        // Set up the date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                // Save the selected date
                yearOfBirth = year
                monthOfBirth = monthOfYear
                dayOfBirth = dayOfMonth

                // Update the text on the button to show the selected date
                val dateString = "$dayOfMonth/${monthOfYear + 1}/$year"
                b.pickDate.text = dateString
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set the maximum date to today's date
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        // Show the dialog
        datePickerDialog.show()
    }

    private fun msg(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}