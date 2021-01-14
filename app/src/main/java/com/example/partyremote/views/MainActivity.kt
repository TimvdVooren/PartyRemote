package com.example.partyremote.views

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.partyremote.services.ConnectedThread
import com.example.partyremote.R
import com.example.partyremote.databinding.ActivityMainBinding
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val BT_MODULE_MAC_ADDRESS = "98:D3:51:FD:E8:4A"
    val REQUEST_ENABLE_BT = 1
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    var btAdapter: BluetoothAdapter? = null
    var btSocket: BluetoothSocket? = null
    var btDevice: BluetoothDevice? = null
    var btThread: ConnectedThread? = null
    var handler: Handler? = null

    var isLedOn = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainControlButton.setOnClickListener {
            val intent = Intent(applicationContext, ControlActivity::class.java)
            startActivity(intent)
        }

        binding.mainBluetoothButton.setOnClickListener {
            Log.i("[BLUETOOTH]", "Attempting to send data")
            btSocket?.let { btSocket ->
                if (btSocket.isConnected && btThread != null) {
                    if (isLedOn) {
                        val command = "LN"
                        btThread!!.write(command.toByteArray())
                        isLedOn = false
                    } else {
                        val command = "LY"
                        btThread!!.write(command.toByteArray())
                        isLedOn = true
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        btAdapter = BluetoothAdapter.getDefaultAdapter()

        //if bluetooth is not enabled then create Intent for user to turn it on
        if (btAdapter?.isEnabled!!) {
            initiateBluetoothProcess()
        } else {
            val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT)
        }
    }

    private fun initiateBluetoothProcess() {
        if (btAdapter?.isEnabled!!) {

            //attempt to connect to bluetooth module
            var tmp: BluetoothSocket? = null
            btDevice = btAdapter?.getRemoteDevice(BT_MODULE_MAC_ADDRESS)

            //create socket
            try {
                tmp = btDevice!!.createRfcommSocketToServiceRecord(MY_UUID)
                btSocket = tmp
                btSocket?.connect()
                Log.i("[BLUETOOTH]", "Connected to: " + btDevice!!.name)

                binding.mainBluetoothIcon.setImageResource(R.drawable.ic_bluetooth_connected_24)
                binding.mainBluetoothStatusImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(applicationContext, R.color.connected_green)
                )
                binding.mainTitle.text = getString(R.string.main_title_connected)
                binding.mainBluetoothStatusText.text = getString(R.string.main_bt_status_connected)
                binding.mainControlButton.visibility = View.VISIBLE;
                binding.mainControlButton.isEnabled = true;
            } catch (e: IOException) {
                try {
                    btSocket!!.close()
                } catch (c: IOException) {
                    return
                }
            }
            Log.i("[BLUETOOTH]", "Creating handler")
            handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    //super.handleMessage(msg);
                    if (msg.what === ConnectedThread.RESPONSE_MESSAGE) {
                        val txt = msg.obj as String

                    }
                }
            }
            Log.i("[BLUETOOTH]", "Creating and running Thread")
            btThread = ConnectedThread(btSocket, handler)
            btThread?.start()
        }
    }
}