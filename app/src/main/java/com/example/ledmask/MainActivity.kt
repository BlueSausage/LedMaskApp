package com.example.ledmask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.createNewImageBtn).setOnClickListener {
            val newImageIntent = Intent(this, CreateNewImageActivity::class.java)
            startActivity(newImageIntent)
        }

    }
}