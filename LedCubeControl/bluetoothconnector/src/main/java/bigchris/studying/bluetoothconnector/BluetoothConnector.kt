package bigchris.studying.bluetoothconnector

import android.bluetooth.BluetoothManager
import androidx.lifecycle.LiveData

interface BluetoothConnector {


    companion object {
        fun instance() : BluetoothConnector = DefaultBluetoothConnector
    }

    fun connectedLiveData() : LiveData<Boolean>

    fun connectionErrorLiveData() : LiveData<String>

    fun setBluetoothManager(bluetoothManager: BluetoothManager)

    fun checkIfEnabled() : Boolean

    fun isConnected() : Boolean

    suspend fun makeConnection()

    suspend fun connect()

    suspend fun disconnect()

    suspend fun send(data: String)

}