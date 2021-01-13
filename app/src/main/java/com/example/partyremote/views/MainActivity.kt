package com.example.partyremote.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.partyremote.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainControlButton.setOnClickListener {
            val intent = Intent(applicationContext, ControlActivity::class.java)
            startActivity(intent)
        }

    }
}