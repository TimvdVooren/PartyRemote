package com.example.partyremote

import android.app.Application
import android.bluetooth.BluetoothAdapter
import com.example.partyremote.models.BluetoothHandler

class BaseApplication : Application() {
    lateinit var btHandler: BluetoothHandler

    override fun onCreate() {
        super.onCreate()
        btHandler = BluetoothHandler(BluetoothAdapter.getDefaultAdapter())
    }
}