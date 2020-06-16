package com.example.my7minuteworkout

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.activity_finish.*
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        setSupportActionBar(toolbar_finish_activity)
        val actionBar = supportActionBar

        // gives action bar back button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        // makes action bar back button work like screen back button
        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed()
        }


        addDateToDataBase()

        btnFinish.setOnClickListener {
            finish()
        }
    }


    private fun addDateToDataBase () {

        val myCalender = Calendar.getInstance()
        val dateTime = myCalender.time

        Log.i("DATE_COMPLETE", "Date from system is $dateTime")

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        Log.i("DATE_COMPLETE", "Date Local from system is ${Locale.getDefault()}")

        val dbHandler = SqliteOpenHelper(this, null)

        dbHandler.addDate(date)

        Log.i("DATE_COMPLETE", "Date is added as $date")

    }
}