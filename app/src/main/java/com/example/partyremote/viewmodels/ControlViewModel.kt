package com.example.partyremote.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.partyremote.BaseApplication
import com.skydoves.colorpickerview.ColorEnvelope
import java.util.*

class ControlViewModel : ViewModel() {

    lateinit var context: Context

    var isLightOn = true
    var isLaserOn = true
    var isDiscoOn = false
    private var previousMillis: Long = 0
    var writingDelay = 200
    private var joystickTimer: Timer? = null

    fun setLightsPower(isLightOn: Boolean) {
        this.isLightOn = isLightOn
        val command = if (isLightOn) "L1" else "L0"
        sendCommand(command)
    }

    fun setLightsColor(colorEnvelope: ColorEnvelope) {
        val command =
            "C${colorEnvelope.argb[1]}|${colorEnvelope.argb[2]}|${colorEnvelope.argb[3]}"
        sendCommand(command)
    }

    fun toggleRedLedPower() {
        val command = "CR"
        sendCommand(command)
    }

    fun toggleGreenLedPower() {
        val command = "CG"
        sendCommand(command)
    }

    fun toggleBlueLedPower() {
        val command = "CB"
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
        joystickTimer?.cancel()

        var command = "J$angle|$strength"
        sendCommand(command)

        joystickTimer = Timer()
        joystickTimer?.schedule(object : TimerTask() {
            override fun run() {
                command = "J0|0"
                sendCommand(command)
            }
        }, (writingDelay*2).toLong())
    }

    private fun sendCommand(command: String) {
        val currentMillis = System.currentTimeMillis()
        if (currentMillis - previousMillis >= writingDelay) {
            previousMillis = currentMillis
            (context as BaseApplication).btHandler.sendData(command)
        }
    }
}