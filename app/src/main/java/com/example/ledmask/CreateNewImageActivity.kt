package com.example.ledmask

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class CreateNewImageActivity : AppCompatActivity() {
    private var color:String = "#FFFF0000"
    private val numOfLeds = 64
    private var allLeds = arrayListOf<TextView>()
    private var image: Array<IntArray> = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_image)

        findViewById<TextView>(R.id.selectedColor).setOnClickListener {
            val colorPickerIntent = Intent(this, ColorPickerActivity::class.java).apply {
                putExtra("currentColor", color)
            }
            startActivityForResult(colorPickerIntent, 1)
        }

        for (i in 0 until numOfLeds) {
            val id = resources.getIdentifier("led$i", "id", packageName)
            allLeds.add(findViewById(id))
        }

        for (led in allLeds) {
            led.setOnClickListener {
                changeColor(it as TextView)
            }
        }

        findViewById<Button>(R.id.imageCancelBtn).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.sendImageBtn).setOnClickListener {
            Log.d("SENDBUTTON", "Bild senden")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                color = data?.getStringExtra("selectedColor")!!
                findViewById<TextView>(R.id.selectedColor).setBackgroundColor(Color.parseColor(color))
            }
        }
    }

    private fun changeColor(led : TextView) {
        led.setBackgroundColor(Color.parseColor(color))
        val ledId = resources.getResourceEntryName(led.id)
        val ledNumber = ledId.filter { it.isDigit() }
        val row = ledNumber.toInt() / 8
        val col = ledNumber.toInt() % 8
        val colorValue = color.replace("#", "").toLong(16)
        image[row][col] = colorValue.toInt()
    }
}