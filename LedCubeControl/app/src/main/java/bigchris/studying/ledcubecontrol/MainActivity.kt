package bigchris.studying.ledcubecontrol

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bigchris.studying.ledcubecontrol.ledbaselayer.LedBaseLayerFragment

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>{getViewModelFactory(true)}
    private val permissionRequestLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        var granted = true
        it.forEach {
            if (it.value == false) {
                granted = false
            }
        }
        if (granted)
            setupBluetoothConnection()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, LedBaseLayerFragment()).commit()
        setupBluetoothConnection()
    }

    private fun setupBluetoothConnection() {
        if (checkBluetoothPermissions()) {
            viewModel.setBluetoothManager((baseContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager))
            viewModel.makeBluetoothConnection()
        } else {
            permissionRequestLauncher.launch(arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN))
        }
    }

    private fun checkBluetoothPermissions()
        =!(ContextCompat.checkSelfPermission(baseContext, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(baseContext, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_DENIED)
}