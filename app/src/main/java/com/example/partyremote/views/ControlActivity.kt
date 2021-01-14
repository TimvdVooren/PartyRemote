package com.example.partyremote.views

import android.R
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.partyremote.databinding.ActivityControlBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


lateinit var binding: ActivityControlBinding

var lightsColor = 0
var isLightOn = true
var isLaserOn = true
var isDiscoOn = false

class ControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.controlLightsSwitch.setOnCheckedChangeListener { _, checkedState -> isLightOn = checkedState}
        binding.controlLasersSwitch.setOnCheckedChangeListener { _, checkedState -> isLaserOn = checkedState}
        binding.controlLightsSwitch.setOnCheckedChangeListener { _, checkedState -> isDiscoOn = checkedState}

        binding.controlLightsColorButton.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Pick a color")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(R.string.ok,
                    ColorEnvelopeListener { envelope, fromUser ->
                        setLightsColor(envelope)
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
            // TODO: send data to Arduino
        }

        // set default lights color
        setLightsColor(
            ColorEnvelope(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.holo_blue_bright
                )
            )
        )
    }

    private fun setLightsColor(colorEnvelope: ColorEnvelope) {
        lightsColor = colorEnvelope.color
        binding.controlLightsPickedColor.imageTintList = ColorStateList.valueOf(lightsColor)
    }
}