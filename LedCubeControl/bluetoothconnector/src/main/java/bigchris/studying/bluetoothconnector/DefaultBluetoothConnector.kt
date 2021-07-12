package bigchris.studying.bluetoothconnector

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

internal object DefaultBluetoothConnector : BluetoothConnector {
    private val tag: String = "BluetoothConnector"
    private lateinit var bluetoothManager: BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter by lazy { bluetoothManager.adapter}
    private lateinit var bluetoothSocket: BluetoothSocket
    private val connectedLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    private val connectionErrorLiveData: MutableLiveData<String> = MutableLiveData("")

    override fun connectedLiveData(): LiveData<Boolean> = connectedLiveData

    override fun connectionErrorLiveData(): LiveData<String> = connectionErrorLiveData

    override fun setBluetoothManager(bluetoothManager: BluetoothManager) {
        this.bluetoothManager = bluetoothManager
    }

    override suspend fun makeConnection() = withContext(Dispatchers.IO) {
        if (!bluetoothAdapter.isEnabled)
            bluetoothAdapter.enable()
        bluetoothAdapter.bondedDevices.forEach {
            if (it.address == Constants.LEDCUBEMAC) {
                bluetoothSocket = it.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Constants.BLUETOOTHSERIALBOARDUUID))
                connect()
            }
        }
    }

    override suspend fun connect() = withContext(Dispatchers.IO) {
        try {
            bluetoothSocket.connect()
            Log.d(tag, "Connection established.")
            connectedLiveData.postValue(true)
            connectionErrorLiveData.postValue("")
        } catch (error: Exception) {
            Log.e(tag, error.message ?: error.toString())
            connectionErrorLiveData.postValue(error.message ?: error.toString())
            this.cancel()
        }
    }

    override fun isConnected(): Boolean {
        if (this::bluetoothSocket.isInitialized)
            return bluetoothSocket.isConnected
        return false
    }

    override suspend fun disconnect() = withContext(Dispatchers.IO) {
        if (isConnected()) {
            bluetoothSocket.close()
            connectedLiveData.postValue(false)
            connectionErrorLiveData.postValue("")
            Log.d(tag,"Disconnected.")
        }
    }

    override suspend fun send(data: String) = withContext(Dispatchers.IO) {
        if (isConnected()) {
            try {
                val byteArray = data.plus("\\0").toByteArray()
                bluetoothSocket.outputStream.write(byteArray)
                Log.d(tag, "${byteArray.size} bytes sent.")
            } catch (error: Exception) {
                if (error.message != null && error.message == "Broken pipe") {
                    connectedLiveData.postValue(false)
                    connectionErrorLiveData.postValue(error.message!!)
                } else {
                    //make livedata for toast etc
                    Log.e(tag, error.message ?: error.toString())
                }
            }
        }
    }

    override fun checkIfEnabled() : Boolean = bluetoothAdapter.isEnabled

}