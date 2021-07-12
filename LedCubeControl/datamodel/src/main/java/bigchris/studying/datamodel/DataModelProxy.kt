package bigchris.studying.datamodel

import org.json.JSONObject

interface DataModelProxy {

    fun setOn(on: Boolean, coordinateX: Int, coordinateY: Int, layer: Int)

    fun isOn(coordinateX: Int, coordinateY: Int, layer: Int) : Boolean

    fun setDuration(duration: Int)

    fun getDuration() : Int

    fun addNewLedCubeState()

    fun removeLedCubeState(position: Int)

    fun stateCount() : Int

    fun currentStateIndex() : Int

    fun jumpToIndex(position: Int)

    fun stepBack()

    fun stepForward()

    fun logCurrentState()

    fun toJson() : JSONObject
}