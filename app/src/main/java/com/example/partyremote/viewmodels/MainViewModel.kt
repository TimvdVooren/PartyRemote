package com.example.partyremote.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.partyremote.BaseApplication

class MainViewModel : ViewModel() {

    lateinit var context: Context
    val isBtConnected = MutableLiveData<Boolean>(false)

    fun initiateBluetoothProcess() {
        val btConnectionStatus = (context as BaseApplication).btHandler.initiateBluetoothProcess()
        isBtConnected.postValue(btConnectionStatus)
    }

    fun isBluetoothEnabled(): Boolean {
        return (context as BaseApplication).btHandler.btAdapter.isEnabled
    }

    fun getRequestCode(): Int {
        return (context as BaseApplication).btHandler.REQUEST_ENABLE_BT
    }
}