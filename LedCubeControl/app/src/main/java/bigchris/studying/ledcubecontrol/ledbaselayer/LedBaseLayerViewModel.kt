package bigchris.studying.ledcubecontrol.ledbaselayer

import android.util.Log
import androidx.lifecycle.ViewModel
import bigchris.studying.datamodel.DataModelProxy

class LedBaseLayerViewModel(private val dataModel: DataModelProxy) : ViewModel() {
    val tag: String = "LedBaseLayerViewModel"
    val frameDummyArray: ArrayList<FrameDummy> = arrayListOf()

    init {
        for (i in 1..dataModel.stateCount())
            frameDummyArray.add(FrameDummy())
    }

    fun setLedState(on: Boolean, coordinateX: Int, coordinateY: Int, layer: Int) {
        dataModel.setOn(on, coordinateX, coordinateY, layer)
    }

    fun getLedState(coordinateX: Int, coordinateY: Int, layer: Int) : Boolean
        = dataModel.isOn(coordinateX, coordinateY, layer)

    fun jumpToFrame(position: Int) {
        if (position < dataModel.stateCount())
            dataModel.jumpToIndex(position)
    }

    fun frameUp() {
        dataModel.stepForward()
    }

    fun frameDown() {
        dataModel.stepBack()
    }

    fun currentStateIndex() : Int = dataModel.currentStateIndex()

    fun getFrameDuration() : Int = dataModel.getDuration()

    fun setFrameDuration(duration: Int) {
        dataModel.setDuration(duration)
    }

    fun createNewFrame() {
        frameDummyArray.add(FrameDummy())
        dataModel.addNewLedCubeState()
    }

    fun removeFrame(position: Int) {
        if (frameCount() > 1) {
            frameDummyArray.removeAt(0)
            dataModel.removeLedCubeState(position)
        }
    }

    fun frameCount() : Int = dataModel.stateCount()

    fun logCurrentState() {
        dataModel.logCurrentState()
    }

    fun getData() = dataModel.toJson().toString()

    fun logJson() {
        Log.d(tag, dataModel.toJson().toString())
    }

}