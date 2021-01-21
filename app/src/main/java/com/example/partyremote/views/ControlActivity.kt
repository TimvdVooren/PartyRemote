package com.example.partyremote.views

import android.R
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.partyremote.databinding.ActivityControlBinding
import com.example.partyremote.viewmodels.ControlViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


lateinit var binding: ActivityControlBinding
lateinit var viewModel: ControlViewModel

class ControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ControlViewModel::class.java]
        viewModel.context = applicationContext

        binding.controlLightsSwitch.setOnCheckedChangeListener { _, checkedState ->
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
//                        val red = envelope.argb[1]
//                        val green = envelope.argb[2]
//                        val blue = envelope.argb[3]
//
//                        if (red > green && red > blue)
//                            viewModel.toggleRedLedPower()
//                        else if (green > red && green > blue)
//                            viewModel.toggleGreenLedPower()
//                        else if (blue > red && blue > green)
//                            viewModel.toggleBlueLedPower()

                        binding.controlLightsPickedColor.imageTintList =
                            ColorStateList.valueOf(envelope.color)
                    })
                .setNegativeButton(
                    R.string.cancel
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(false)
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }

        binding.controlJoystick.setOnMoveListener { angle, strength ->
            viewModel.sendJoyStickData(angle, strength)
        }

        // set default lights color
//        viewModel.setLightsColor(
//            ColorEnvelope(
//                ContextCompat.getColor(
//                    applicationContext,
//                    R.color.holo_blue_bright
//                )
//            )
//        )
    }
}