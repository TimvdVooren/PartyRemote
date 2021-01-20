package com.example.partyremote.views

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.partyremote.R
import com.example.partyremote.databinding.ActivityMainBinding
import com.example.partyremote.viewmodels.MainViewModel
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    private val CLIENT_ID = "9128cfcc2523420e90fde0851968fdbc"
    private val REDIRECT_URI = "http://localhost:8888/callback"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.context = applicationContext

        binding.mainControlButton.setOnClickListener {
//            val intent = Intent(applicationContext, ControlActivity::class.java)
//            startActivity(intent)
            connected()
        }
//
//        binding.mainBluetoothButton.setOnClickListener {
//            viewModel.initiateBluetoothProcess()
//        }
//
//        viewModel.isBtConnected.observe(this, Observer { isConnected ->
//            if (isConnected) {
                binding.mainBluetoothIcon.setImageResource(R.drawable.ic_bluetooth_connected_24)
                binding.mainBluetoothStatusImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(applicationContext, R.color.connected_green)
                )
                binding.mainTitle.text = getString(R.string.main_title_connected)
                binding.mainBluetoothStatusText.text = getString(R.string.main_bt_status_connected)
                binding.mainControlButton.visibility = View.VISIBLE;
                binding.mainControlButton.isEnabled = true;
//
//                binding.mainBluetoothButton.setOnClickListener {}
//            } else {
//                binding.mainBluetoothIcon.setImageResource(R.drawable.ic_bluetooth_disabled_24)
//                binding.mainBluetoothStatusImage.imageTintList = ColorStateList.valueOf(
//                    ContextCompat.getColor(applicationContext, R.color.searching_blue)
//                )
//                binding.mainTitle.text = getString(R.string.main_title_disconnected)
//                binding.mainBluetoothStatusText.text =
//                    getString(R.string.main_bt_status_disconnected)
//                binding.mainControlButton.visibility = View.INVISIBLE;
//                binding.mainControlButton.isEnabled = false;
//
//                binding.mainBluetoothButton.setOnClickListener {
//                    viewModel.initiateBluetoothProcess()
//                }
//            }
//        })
    }

    override fun onResume() {
        super.onResume()

//        //if bluetooth is not enabled then create Intent for user to turn it on
//        if (viewModel.isBluetoothEnabled()) {
//            viewModel.initiateBluetoothProcess()
//        } else {
//            val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBTIntent, viewModel.getRequestCode())
//        }
    }

    override fun onStart() {
        super.onStart()
        // Set the connection parameters

        // Set the connection parameters
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")

                    // Now you can start interacting with App Remote
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    private fun connected() {
        // Play a playlist
        mSpotifyAppRemote?.getPlayerApi()?.play("spotify:playlist:01DbkmjFPYPeZyw7MxBal5?si=cjqhTLRZR5WbvylOHNbalA");
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }
}