package com.example.partyremote.views

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.partyremote.R
import com.example.partyremote.databinding.ActivityMainBinding
import com.example.partyremote.viewmodels.MainViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private var btConnectionRetryTimer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.context = applicationContext

        binding.mainControlButton.setOnClickListener {
            val intent = Intent(applicationContext, ControlActivity::class.java)
            startActivity(intent)
        }

        viewModel.isBtConnected.observe(this, Observer { isConnected ->
            btConnectionRetryTimer?.cancel()

            if (isConnected) {
                binding.mainBluetoothIcon.setImageResource(R.drawable.ic_bluetooth_connected_24)
                binding.mainBluetoothStatusImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(applicationContext, R.color.connected_green)
                )
                binding.mainTitle.text = getString(R.string.main_title_connected)
                binding.mainBluetoothStatusText.text = getString(R.string.main_bt_status_connected)
                binding.mainControlButton.visibility = View.VISIBLE
                binding.mainControlButton.isEnabled = true
            } else {
                binding.mainBluetoothIcon.setImageResource(R.drawable.ic_bluetooth_disabled_24)
                binding.mainBluetoothStatusImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(applicationContext, R.color.searching_blue)
                )
                binding.mainTitle.text = getString(R.string.main_title_disconnected)
                binding.mainBluetoothStatusText.text =
                    getString(R.string.main_bt_status_disconnected)
                binding.mainControlButton.visibility = View.INVISIBLE
                binding.mainControlButton.isEnabled = false;

                btConnectionRetryTimer = Timer()
                btConnectionRetryTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        viewModel.initiateBluetoothProcess()
                    }
                }, 2000)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        //if bluetooth is not enabled then create Intent for user to turn it on
        if (viewModel.isBluetoothEnabled()) {
            viewModel.initiateBluetoothProcess()
        } else {
            val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBTIntent, viewModel.getRequestCode())
        }
    }


}