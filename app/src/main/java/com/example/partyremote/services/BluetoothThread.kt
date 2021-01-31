package com.example.partyremote.services

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.*

class BluetoothThread(private val btSocket: BluetoothSocket?, private val handler: Handler?) : Thread() {
    private val inputStream: InputStream?
    private val outputStream: OutputStream?

    override fun run() {
        val br = BufferedReader(InputStreamReader(inputStream))
        Log.i("[BT-THREAD]", "Starting thread")
        while (true) {
            try {
                val resp = br.readLine()
                val msg = Message()
                msg.what = RESPONSE_MESSAGE
                msg.obj = resp
                handler?.sendMessage(msg)
            } catch (e: IOException) {
                break
            }
        }
        Log.i("[BT-THREAD]", "While loop ended")
    }

    fun write(bytes: ByteArray?) {
        try {
            Log.i("[BT-THREAD]", "Writing bytes")
            outputStream?.flush();
            outputStream?.write(bytes)
        } catch (e: IOException) {
        }
    }

    fun cancel() {
        try {
            btSocket?.close()
        } catch (e: IOException) {
        }
    }

    companion object {
        const val RESPONSE_MESSAGE = 10
    }

    init {
        var tmpIn: InputStream? = null
        var tmpOut: OutputStream? = null
        Log.i("[BT-THREAD]", "Creating thread")

        try {
            tmpIn = btSocket?.inputStream
            tmpOut = btSocket?.outputStream
        } catch (e: IOException) {
            Log.e("[BT-THREAD]", "Error:" + e.message)
        }

        inputStream = tmpIn
        outputStream = tmpOut

        try {
            outputStream!!.flush()
        } catch (e: IOException) {}
        Log.i("[BT-THREAD]", "IO's obtained")
    }
}