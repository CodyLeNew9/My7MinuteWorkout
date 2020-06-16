package com.example.my7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //Setups as an action bar
        setSupportActionBar(toolbar_history_activity)
        val actionBar = supportActionBar

        // gives action bar back button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "HISTORY"
        }

        // makes action bar back button work like screen back button
        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllCompletedDates()
    }

    /**
     * Function is used to get the list exercise completed dates.
     */
    private fun getAllCompletedDates() {

        // Instance of the Sqlite Open Helper class.
        val dbHandler = SqliteOpenHelper(this, null)

        val allCompletedDatesList = dbHandler.getAllCompletedDatesList() // List of history table data

        if (allCompletedDatesList.size > 0) {
            // Here if the List size is greater then 0 we will display the item in the recycler view or else we will show the text view that no data is available.
            tvHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE
            tvNoDataAvailable.visibility = View.GONE

            // Creates a vertical Layout Manager
            rvHistory.layoutManager = LinearLayoutManager(this)

            // History adapter is initialized and the list is passed in the param.
            val historyAdapter = HistoryAdapter(this, allCompletedDatesList)

            // Access the RecyclerView Adapter and load the data into it
            rvHistory.adapter = historyAdapter

        } else {
            tvHistory.visibility = View.GONE
            rvHistory.visibility = View.GONE
            tvNoDataAvailable.visibility = View.VISIBLE
        }
    }
}