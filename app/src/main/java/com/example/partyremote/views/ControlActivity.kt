package com.example.partyremote.views

import android.R
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.partyremote.databinding.ActivityControlBinding
import com.example.partyremote.models.BluetoothHandler
import com.example.partyremote.viewmodels.ControlViewModel
import com.example.partyremote.viewmodels.MainViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote


lateinit var binding: ActivityControlBinding
lateinit var viewModel: ControlViewModel

class ControlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ControlViewModel::class.java]
        viewModel.context = applicationContext

        binding.controlPlayButton.setOnClickListener {
            viewModel.playMusic()
        }

        binding.controlLightsSwitch.setOnCheckedChangeListener() { _, checkedState ->
            viewModel.setLightsPower(checkedState)
        }
        binding.controlLasersSwitch.setOnCheckedChangeListener { _, checkedState ->
            viewModel.setLaserPower(checkedState)
        }
        binding.controlDiscoSwitch.setOnCheckedChangeListener { _, checkedState ->
            viewModel.setDiscoMode(checkedState)
        }

        binding.controlLightsColorButton.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Pick a color")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(R.string.ok,
                    ColorEnvelopeListener { envelope, fromUser ->
                        viewModel.setLightsColor(envelope)
                        binding.controlLightsPickedColor.imageTintList =
                            ColorStateList.valueOf(envelope.color)
                    })
                .setNegativeButton(
                    R.string.cancel
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }

        binding.controlJoystick.setOnMoveListener { angle, strength ->
            viewModel.sendJoyStickData(angle, strength)
        }

        // set default lights color
        viewModel.setLightsColor(
            ColorEnvelope(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.holo_blue_bright
                )
            )
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.connect()
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }
}