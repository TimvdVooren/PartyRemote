package com.example.partyremote.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.partyremote.BaseApplication
import com.skydoves.colorpickerview.ColorEnvelope

class ControlViewModel : ViewModel() {

    lateinit var context: Context

    var isLightOn = true
    var isLaserOn = true
    var isDiscoOn = false

    fun setLightsPower(isLightOn: Boolean) {
        this.isLightOn = isLightOn
        val command = if (isLightOn) "L1" else "L0"
        sendCommand(command)
    }

    fun setLightsColor(colorEnvelope: ColorEnvelope) {
        val command =
            "C${colorEnvelope.argb[0]}|${colorEnvelope.argb[1]}|${colorEnvelope.argb[2]}|${colorEnvelope.argb[3]}"
        sendCommand(command)
    }

    fun setLaserPower(isLaserOn: Boolean) {
        this.isLaserOn = isLaserOn
        val command = if (isLaserOn) "B1" else "B0"
        sendCommand(command)
    }

    fun setDiscoMode(isDiscoOn: Boolean) {
        this.isDiscoOn = isDiscoOn
        val command = if (isDiscoOn) "D1" else "D0"
        sendCommand(command)
    }

    fun sendJoyStickData(angle: Int, strength: Int) {
        val command = "J$angle|$strength"
        sendCommand(command)
    }

    private fun sendCommand(command: String) {
        (context as BaseApplication).btHandler.sendData(command)
    }
}