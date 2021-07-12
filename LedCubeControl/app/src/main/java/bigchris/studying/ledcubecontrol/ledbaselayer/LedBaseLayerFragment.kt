package bigchris.studying.ledcubecontrol.ledbaselayer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.isDigitsOnly
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import bigchris.studying.bluetoothconnector.BluetoothConnector
import bigchris.studying.ledcubecontrol.MainViewModel
import bigchris.studying.ledcubecontrol.R
import bigchris.studying.ledcubecontrol.customwidgets.LedMatrixWidget
import bigchris.studying.ledcubecontrol.getViewModelFactory
import bigchris.studying.ledcubecontrol.ledbaselayer.adapter.FrameRecyclerviewAdapter
import com.google.android.material.snackbar.Snackbar

class LedBaseLayerFragment : Fragment(), LedMatrixWidget.LedToggleListener, TextWatcher, FrameRecyclerviewAdapter.RecyclerViewSelectionListener {
    private val frameLimit = 20
    private lateinit var ledMatrixHolderLayout: LinearLayoutCompat
    private lateinit var frameDurationEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var connectButton: Button
    private lateinit var frameRecyclerView: RecyclerView
    private lateinit var createFrameButton: Button
    private val viewModel by viewModels<LedBaseLayerViewModel> {getViewModelFactory()}
    private val mainViewModel by viewModels<MainViewModel>{getViewModelFactory(true)}
    private var ignoreLedToggle: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.led_base_layer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupConnectionObservation()
        setupConnectionErrorObservation()
        setupLedMatrixHolder(view)
        setupFrameDurationEditText(view)
        setupSendButton(view)
        setupConnectButton(view)
        setupFrameList(view)
        setupCreateFrameButton(view)
    }

    override fun onFrameMarkedToDeletion(position: Int) {
        viewModel.removeFrame(position)
        frameRecyclerView.adapter?.notifyDataSetChanged()
        (frameRecyclerView.adapter as? FrameRecyclerviewAdapter)?.currentlySelected = viewModel.currentStateIndex()
        loadCurrentState()
        Snackbar.make(requireView(), resources.getString(R.string.msg_frame_deleted, position), Snackbar.LENGTH_SHORT).show()
    }

    override fun onFrameSelected(position: Int) {
        viewModel.jumpToFrame(position)
        loadCurrentState()
    }

    override fun onLedToggle(toggleState: Boolean, coordinateX: Int, coordinateY: Int, layer: Int) {
        if (!ignoreLedToggle)
            viewModel.setLedState(toggleState, coordinateX, coordinateY, layer)
        viewModel.logJson()
    }

    override fun onLedState(coordinateX: Int, coordinateY: Int, layer: Int): Boolean
        = viewModel.getLedState(coordinateX, coordinateY, layer)

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        s?.let {
            if (it.isEmpty() || it.toString().toInt() <= 0 || !it.isDigitsOnly()) {
                frameDurationEditText.setText(viewModel.getFrameDuration().toString())
            } else {
                viewModel.setFrameDuration(it.toString().toInt())
            }
        }
    }

    private fun setupConnectionErrorObservation() {
        mainViewModel.getConnectionErrorLiveData().observe(this.viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                sendButton.isEnabled = false
                connectButton.setText(R.string.connect)
                Snackbar.make(requireView(), R.string.msg_connection_failed, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupConnectionObservation() {
        mainViewModel.getConnectionLiveData().observe(this.viewLifecycleOwner) {
            if (it) {
                sendButton.isEnabled = true
                connectButton.setText(R.string.disconnect)
                Snackbar.make(requireView(), R.string.msg_connected, Snackbar.LENGTH_SHORT).show()
            } else {
                sendButton.isEnabled = false
                connectButton.setText(R.string.connect)
                Snackbar.make(requireView(), R.string.msg_disconnected, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupLedMatrixHolder(view: View) {
        ledMatrixHolderLayout = view.findViewById(R.id.ledMatrixHolderLayout)
        ledMatrixHolderLayout.forEach {
            (it as LedMatrixWidget).registerLedToggleListener(this)
        }
    }

    private fun setupFrameDurationEditText(view: View) {
        frameDurationEditText = view.findViewById(R.id.durationEditText)
        frameDurationEditText.setText(viewModel.getFrameDuration().toString())
        frameDurationEditText.addTextChangedListener(this)
    }

    private fun setupSendButton(view: View) {
        sendButton = view.findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            mainViewModel.send(viewModel.getData())
            Snackbar.make(requireView(), resources.getString(R.string.msg_data_sent), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupConnectButton(view: View) {
        connectButton = view.findViewById(R.id.connectButton)
        connectButton.setOnClickListener {
            if (mainViewModel.isConnected()) {
                mainViewModel.disconnect()
            } else {
                mainViewModel.makeBluetoothConnection()
            }
        }
    }

    private fun setupFrameList(view: View) {
        frameRecyclerView = view.findViewById(R.id.frameRecyclerView)
        frameRecyclerView.adapter = FrameRecyclerviewAdapter(viewModel.frameDummyArray, this)
    }

    private fun setupCreateFrameButton(view: View) {
        createFrameButton = view.findViewById(R.id.createFrameButton)
        createFrameButton.setOnClickListener {
            if (viewModel.frameCount() < frameLimit) {
                viewModel.createNewFrame()
                viewModel.jumpToFrame(viewModel.frameCount() - 1)
                frameRecyclerView.adapter?.notifyDataSetChanged()
                (frameRecyclerView.adapter as? FrameRecyclerviewAdapter)?.currentlySelected =
                    viewModel.currentStateIndex()
                loadCurrentState()
            }
        }
    }

    private fun loadCurrentState() {
        ignoreLedToggle = true
        ledMatrixHolderLayout.forEach {
            (it as LedMatrixWidget).updateLeds()
            frameDurationEditText.setText(viewModel.getFrameDuration().toString())
        }
        ignoreLedToggle = false
    }
}