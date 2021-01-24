package com.example.partyremote.models

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.partyremote.services.BluetoothThread
import java.io.IOException
import java.io.Serializable
import java.util.*

class BluetoothHandler(val btAdapter: BluetoothAdapter) {

    private val BT_MODULE_MAC_ADDRESS = "98:D3:51:FD:E8:4A"
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    val REQUEST_ENABLE_BT = 1

    var btSocket: BluetoothSocket? = null
    var btDevice: BluetoothDevice? = null
    var btThread: BluetoothThread? = null
    var handler: Handler? = null

    fun initiateBluetoothProcess(): Boolean{
        var isConnected = false

        if (btAdapter.isEnabled) {

            //attempt to connect to bluetooth module
            var tmp: BluetoothSocket? = null
            btDevice = btAdapter.getRemoteDevice(BT_MODULE_MAC_ADDRESS)

            //create socket
            try {
                tmp = btDevice!!.createRfcommSocketToServiceRecord(MY_UUID)
                btSocket = tmp
                btSocket?.connect()
                Log.i("[BLUETOOTH]", "Connected to: " + btDevice!!.name)
                isConnected = true
            } catch (e: IOException) {
                try {
                    btSocket?.close()
                } catch (c: IOException) {
                    return isConnected
                }
            }
            Log.i("[BLUETOOTH]", "Creating handler")
            handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    //super.handleMessage(msg);
                    if (msg.what === BluetoothThread.RESPONSE_MESSAGE) {
                        val txt = msg.obj as String

                    }
                }
            }
            Log.i("[BLUETOOTH]", "Creating and running Thread")
            btThread = BluetoothThread(btSocket, handler)
            btThread?.start()
        }

        return isConnected
    }

    fun sendData(command: String) {
        Log.i("[BLUETOOTH]", "Attempting to send data")
        btSocket?.let { btSocket ->
            if (btSocket.isConnected) {
                btThread?.write(command.toByteArray())
            }
        }
    }
}