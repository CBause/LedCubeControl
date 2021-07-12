package bigchris.studying.ledcubecontrol

import android.bluetooth.BluetoothManager
import android.util.Log
import androidx.lifecycle.*
import bigchris.studying.bluetoothconnector.BluetoothConnector
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val tag: String = "MainViewModel"
    private val bluetoothConnector: BluetoothConnector = BluetoothConnector.instance()

    fun setBluetoothManager(bluetoothManager: BluetoothManager) {
        bluetoothConnector.setBluetoothManager(bluetoothManager)
    }

    fun makeBluetoothConnection() {
        viewModelScope.launch {
            bluetoothConnector.makeConnection()
        }
    }

    fun getConnectionLiveData() : LiveData<Boolean> = bluetoothConnector.connectedLiveData()

    fun getConnectionErrorLiveData() : LiveData<String> = bluetoothConnector.connectionErrorLiveData()

    fun checkIfBluetoothEnabled() : Boolean = bluetoothConnector.checkIfEnabled()

    fun isConnected() : Boolean = bluetoothConnector.isConnected()

    fun send(data: String) {
        viewModelScope.launch {
            bluetoothConnector.send(data)
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            bluetoothConnector.disconnect()
        }
    }
}