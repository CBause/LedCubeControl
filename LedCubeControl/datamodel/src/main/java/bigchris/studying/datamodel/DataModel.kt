package bigchris.studying.datamodel

import org.json.JSONObject

internal class DataModel : DataModelProxy {

    override fun setOn(on: Boolean, coordinateX: Int, coordinateY: Int, layer: Int) {
        LedCubeStateHolder.setOn(on, coordinateX, coordinateY, layer)
    }

    override fun isOn(coordinateX: Int, coordinateY: Int, layer: Int): Boolean
            = LedCubeStateHolder.isOn(coordinateX, coordinateY, layer)

    override fun setDuration(duration: Int) {
        LedCubeStateHolder.setStateDuration(duration)
    }

    override fun getDuration(): Int = LedCubeStateHolder.getStateDuration()

    override fun addNewLedCubeState() {
        LedCubeStateHolder.addNewLedCubeState()
    }

    override fun jumpToIndex(position: Int) {
        LedCubeStateHolder.jumpToIndex(position)
    }

    override fun stepBack() {
        LedCubeStateHolder.stepBack()
    }

    override fun stepForward() {
        LedCubeStateHolder.stepForward()
    }

    override fun removeLedCubeState(position: Int) {
        LedCubeStateHolder.removeLedCubeState(position)
    }

    override fun currentStateIndex(): Int = LedCubeStateHolder.currentStateIndex()

    override fun stateCount(): Int = LedCubeStateHolder.stateCount()

    override fun logCurrentState() {
        LedCubeStateHolder.logCurrentState()
    }

    override fun toJson(): JSONObject = LedCubeStateHolder.toJson()
}