package com.example.my7minuteworkout


import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    //START
    private var restTimer: CountDownTimer? = null // Variable for Rest Timer and later on we will initialize it.
    private var restProgress = 0 // Variable for timer progress. As initial value the rest progress is set to 0. As we are about to start.
    private var exerciseTimer: CountDownTimer? = null // Variable for Rest Timer and later on we will initialize it.
    private var exerciseProgress = 0 // Variable for timer progress. As initial value the rest progress is set to 0. As we are about to start.

    private val restTimerDuration : Long = 10000 //millisecond
    private val exerciseTimerDuration : Long = 30000 //milliseconds

    private var exerciseList: ArrayList<ExerciseModel>? = null //our exercise list
    private var currentExercisePosition = -1 //Keeping track of postition

    private var tts: TextToSpeech? = null //Text to speech
    private var player: MediaPlayer? = null //Media Player

    private var exerciseAdapter: ExerciseStatusAdapter? = null //adapter needed for the recycle view
    //END

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
            customDialogFunction()
        }

        //init text to speech
        tts = TextToSpeech(this,this)


        //START

        exerciseList = Constants.defaultExerciseList() //get default list
        setupRestView() // REST View is set in this function

        setupExerciseStatusRecyclerView()

        //END
    }

    override fun onBackPressed() {
        customDialogFunction()
    }

    //START
    /**
     * Function is used to set the timer for REST.
     */
    private fun setupRestView() {

        /**
         * Here firstly we will check if the timer is running the and it is not null then cancel the running timer and start the new one.
         * And set the progress to initial which is 0.
         */

        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE

        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        // This function is used to set the progress details.
        setRestProgressBar()

        //Setup preview of next exercise
        tvExerciseNamePreview.text = exerciseList!![currentExercisePosition + 1].getName()
    }
    // END


    //START
    /**
     * Function is used to set the progress of timer using the progress
     */
    private fun setRestProgressBar() {

        progressBar.progress = restProgress // Sets the current progress to the specified value.

        /**
         * @param millisInFuture The number of millis in the future from the call
         *   to {#start()} until the countdown is done and {#onFinish()}
         *   is called.
         * @param countDownInterval The interval along the way to receive
         *   {#onTick(long)} callbacks.
         */
        // Here we have started a timer of 10 seconds so the 10000 is milliseconds is 10 seconds and the countdown interval is 1 second so it 1000.
        restTimer = object : CountDownTimer(restTimerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++ // It is increased by 1
                progressBar.progress = 10 - restProgress // Indicates progress bar progress
                tvTimer.text =
                    (10 - restProgress).toString()  // Current progress is set to text view in terms of seconds.
            }

            override fun onFinish() {
                // When the 10 seconds will complete this will be executed.
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()

            }
        }.start()
    }
    //END


    //START
    /**
     * Function is used to set the timer for REST.
     */
    private fun setupExerciseView() {

        /**
         * Here firstly we will check if the timer is running the and it is not null then cancel the running timer and start the new one.
         * And set the progress to initial which is 0.
         */

        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        // This function is used to set the progress details.
        setExerciseProgressBar()

        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()

        speakOut("${exerciseList!![currentExercisePosition].getName()}, begin")

    }
    // END

    //START
    /**
     * Function is used to set the progress of timer using the progress
     */
    private fun setExerciseProgressBar() {

        progressBarExercise.progress = exerciseProgress // Sets the current progress to the specified value.

        /**
         * @param millisInFuture The number of millis in the future from the call
         *   to {#start()} until the countdown is done and {#onFinish()}
         *   is called.
         * @param countDownInterval The interval along the way to receive
         *   {#onTick(long)} callbacks.
         */
        // Here we have started a timer of 10 seconds so the 10000 is milliseconds is 10 seconds and the countdown interval is 1 second so it 1000.
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++ // It is increased by 1
                progressBarExercise.progress = 30 - exerciseProgress // Indicates progress bar progress
                tvTimerExercise.text =
                    (30 - exerciseProgress).toString()  // Current progress is set to text view in terms of seconds.
            }

            override fun onFinish() {
                // When the 30 seconds will complete this will be executed.

                if (currentExercisePosition < exerciseList!!.size - 1) {

                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                    setupRestView()

                    speakOut("Rest for ten seconds")

                } else {
                    Toast.makeText(this@ExerciseActivity, "7 min workout done", Toast.LENGTH_SHORT).show()

                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }
    //END

    /**
     * Speech function
     */
    private fun speakOut (text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    /**
     * function using adapter to setup recycle view with workout list
     */
    private fun setupExerciseStatusRecyclerView(){
        //Set xml layout of recycle view from code to horzolntal
        rvExerciseStatus.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //init adapter with list
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        //finally set recycler view with adapter
        rvExerciseStatus.adapter = exerciseAdapter
    }

    /**
     * Method is used to show the Custom Dialog.
     */
    private fun customDialogFunction() {
        val customDialog = Dialog(this)
        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)

        customDialog.tvYes.setOnClickListener(View.OnClickListener {

            finish() // back to upper activity

            //onBackPressed() // back to upper activity

            customDialog.dismiss() // Dialog will be dismissed
        })

        customDialog.tvNo.setOnClickListener(View.OnClickListener {

            Toast.makeText(applicationContext, "Smart Choice", Toast.LENGTH_SHORT).show()

            customDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        customDialog.show()
    }


    /**
     * Here in the Destroy function we will reset the rest timer if it is running.
     */
    public override fun onDestroy() {

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (player != null) {
            player!!.stop()
        }

        super.onDestroy()

    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {

            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported")
            }

        } else {
            Log.e("TTS", "TTS Init Failed")
        }
    }
}