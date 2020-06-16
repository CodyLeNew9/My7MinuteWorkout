package com.example.my7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.activity_finish.*

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


        btnFinish.setOnClickListener {
            finish()
        }
    }
}