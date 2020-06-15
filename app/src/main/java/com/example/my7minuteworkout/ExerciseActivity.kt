package com.example.my7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_exercise.*

class ExerciseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        //Setups as an action bar
        setSupportActionBar(toolbar_exercise_activity)
        val actionBar = supportActionBar

        // gives action bar back button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        // makes action bar back button work like screen back button
        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}