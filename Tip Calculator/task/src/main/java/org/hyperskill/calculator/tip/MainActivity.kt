package org.hyperskill.calculator.tip

import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.slider.Slider
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.edit_text)
        val slider = findViewById<Slider>(R.id.slider)
        val textView = findViewById<TextView>(R.id.text_view)


        fun calculate() {
            val billValue = editText.text.toString()
            val tipPercentage = slider.value


            if (billValue != "") {
                val tip = (billValue.toDouble() / 100.0 * tipPercentage.toInt())
                val tip_txt = String.format("%.2f", tip)

                textView.text = "Tip amount: ${tip_txt}"
//                textView.text = "${tipPercentage.toInt()}% tip: ${tip_txt}"
            } else {
                textView.text = ""
            }
        }

        slider.addOnChangeListener { slider, value, fromUser ->
            calculate()
        }

        editText.doAfterTextChanged{
            calculate()
        }



    }
}