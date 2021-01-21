package com.example.ledmask

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

class ColorPickerActivity : AppCompatActivity() {

    private lateinit var allSeekBars: Array<SeekBar>
    private lateinit var colorString: EditText
    private lateinit var colorPreview: TextView
    private lateinit var cancelBtn: Button
    private lateinit var applyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        val intent = intent
        var hexValue = intent.getStringExtra("currentColor")

        allSeekBars = arrayOf(
            findViewById<SeekBar>(R.id.brightnessBar),
            findViewById<SeekBar>(R.id.redBar),
            findViewById<SeekBar>(R.id.greenBar),
            findViewById<SeekBar>(R.id.blueBar)
        )

        colorString = findViewById<EditText>(R.id.strColor)
        colorPreview = findViewById<TextView>(R.id.btnColorPreview)
        cancelBtn = findViewById<Button>(R.id.colorCancelBtn)
        applyButton = findViewById<Button>(R.id.colorAppyBtn)

        setColor(hexValue!!)
        setSeekBars(hexValue)

        cancelBtn.setOnClickListener {
            finish()
        }

        applyButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("selectedColor", hexValue)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        for (seekBar in allSeekBars) {
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    hexValue = getColorString()
                    setColor(hexValue!!)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        colorString.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setSeekBars(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setSeekBars(hexValue: String){
        if (hexValue.contains("#")) hexValue.replace("#", "")
        if (hexValue.length == 6 ) allSeekBars[0].progress = 255
        if (hexValue.length == 8 ) allSeekBars[0].progress = Integer.parseInt(hexValue.substring(0..1), 16)
        allSeekBars[1].progress = Integer.parseInt(hexValue.substring(2..3), 16)
        allSeekBars[2].progress = Integer.parseInt(hexValue.substring(4..5), 16)
        allSeekBars[3].progress = Integer.parseInt(hexValue.substring(6..7), 16)
    }

    private fun setColor(hexValue: String) {
        colorString.setText(hexValue.replace("#","").toUpperCase())
        colorPreview.setBackgroundColor(Color.parseColor(hexValue))
    }

    private fun getColorString(): String {
        val brightness = calcColor(findViewById(R.id.brightnessBar))
        val red = calcColor(findViewById(R.id.redBar))
        val green = calcColor(findViewById(R.id.greenBar))
        val blue = calcColor(findViewById(R.id.blueBar))
        return ("#$brightness$red$green$blue")
    }

    private fun calcColor(seekBar: SeekBar, maxVal: Int = 255): String {
        var colorValue = Integer.toHexString((seekBar.progress*maxVal)/maxVal)
        if (colorValue.length == 1) colorValue = "0$colorValue"
        return colorValue
    }

}