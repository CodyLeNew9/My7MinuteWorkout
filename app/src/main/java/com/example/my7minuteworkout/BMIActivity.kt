package com.example.my7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bmi.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    // START
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW // A variable to hold a value to make a selected view visible
    // END


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        //Setups as an action bar
        setSupportActionBar(toolbar_bmi_activity)
        val actionBar = supportActionBar

        // gives action bar back button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "CALCULATE BMI"
        }

        // makes action bar back button work like screen back button
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // When the activity is launched make METRIC UNITS VIEW visible.
        makeVisibleMetricUnitsView()

        //Adding a check change listener to the radio group and according to the radio button.)
        // Radio Group change listener is set to the radio group which is added in XML.
        rgUnits.setOnCheckedChangeListener { radioGroup: RadioGroup, checkedId: Int ->

            // Here if the checkId is METRIC UNITS view then make the view visible else US UNITS view.
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        // Button will calculate the input values in Metric Units
        btnCalculateUnits.setOnClickListener {

            // START
            if (currentVisibleView == METRIC_UNITS_VIEW) {
                // The values are validated.
                if (validateMetricUnits()) {

                    // The height value is converted to float value and divided by 100 to convert it to meter.
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100

                    // The weight value is converted to float value
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

                    // BMI value is calculated in METRIC UNITS using the height and weight value.
                    val bmi = weightValue / (heightValue * heightValue)

                    displayBMIResult(bmi)

                } else {
                    Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
                }
            } else {

                // The values are validated.
                if (validateUsUnits()) {

                    val usUnitHeightValueFeet: String = etUsUnitHeightFeet.text.toString() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: String = etUsUnitHeightInch.text.toString() // Height Inch value entered in EditText component.
                    val usUnitWeightValue: Float = etUsUnitWeight.text.toString().toFloat() // Weight value entered in EditText component.

                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                    displayBMIResult(bmi) // Displaying the result into UI

                } else {
                    Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // START
    /**
     * Function is used to make the METRIC UNITS VIEW visible and hide the US UNITS VIEW.
     */
    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW // Current View is updated here.
        llDiplayBMIResult.visibility = View.GONE
        llMetricUnitsView.visibility = View.VISIBLE // METRIC UNITS VIEW is Visible
        llUsUnitsView.visibility = View.GONE // US UNITS VIEW is hidden

        etMetricUnitHeight.text!!.clear() // height value is cleared if it is added.
        etMetricUnitWeight.text!!.clear() // weight value is cleared if it is added.

        tvYourBMI.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIValue.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIType.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIDescription.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
    }
    // END

    //Making a function to make the US UNITS view visible.
    // START
    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW // Current View is updated here.
        llDiplayBMIResult.visibility = View.GONE
        llMetricUnitsView.visibility = View.GONE // METRIC UNITS VIEW is hidden
        llUsUnitsView.visibility = View.VISIBLE // US UNITS VIEW is Visible

        etUsUnitWeight.text!!.clear() // weight value is cleared.
        etUsUnitHeightFeet.text!!.clear() // height feet value is cleared.
        etUsUnitHeightInch.text!!.clear() // height inch is cleared.

        tvYourBMI.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIValue.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIType.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIDescription.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
    }
    // END


    // START
    /**
     * Function is used to validate the input values for METRIC UNITS.
     */
    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (etMetricUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (etMetricUnitHeight.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }
    // END

    // START
    /**
     * Function is used to validate the input values for US UNITS.
     */
    private fun validateUsUnits(): Boolean {

        var isValid = true

        when {
            etUsUnitWeight.text.toString().isEmpty() -> {
                isValid = false
            }
            etUsUnitHeightFeet.text.toString().isEmpty() -> {
                isValid = false
            }
            etUsUnitHeightInch.text.toString().isEmpty() -> {
                isValid = false
            }
        }

        return isValid
    }
    // END

    // START
    /**
     * Function is used to display the result of METRIC UNITS.
     */
    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        tvYourBMI.visibility = View.VISIBLE
        tvBMIValue.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE
        llDiplayBMIResult.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue // Value is set to TextView
        tvBMIType.text = bmiLabel // Label is set to TextView
        tvBMIDescription.text = bmiDescription // Description is set to TextView
    }
    // END
}