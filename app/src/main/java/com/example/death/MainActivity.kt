package com.example.death

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.death.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    private val calendar = Calendar.getInstance()
    private var yearOfBirth: Int = 0
    private var monthOfBirth: Int = 0
    private var dayOfBirth: Int = 0

    private var age: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)


        //////////////    Notification    //////////////
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
        val contentTitle = "My Notification"
        val contentText = setNotificationContent()

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)

        // send notification
        b.btn.setOnClickListener {
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
        //////////////    #Notification#    //////////////

        // Set up the date picker button
        b.pickDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up the number picker
        b.pickAge.minValue = 0
        b.pickAge.maxValue = 100
        b.pickAge.wrapSelectorWheel = false
        b.pickAge.setOnValueChangedListener { _, _, newVal ->
            // Do something with the new value
            age = newVal
        }

    }

    private fun setNotificationContent(): String {
        return "This is my notification $yearOfBirth"
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
}