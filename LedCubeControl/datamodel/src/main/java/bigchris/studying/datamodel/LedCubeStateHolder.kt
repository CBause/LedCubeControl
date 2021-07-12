package bigchris.studying.datamodel

import android.util.Base64
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

internal object LedCubeStateHolder {
    private val ledCubeStates: ArrayList<LedCubeState> = arrayListOf()
    private var currentPosition = 0

    init {
        ledCubeStates.add(LedCubeState())
    }

    private fun currentLedCubeState() : LedCubeState = ledCubeStates.get(currentPosition)

    fun setStateDuration(duration: Int) {
        currentLedCubeState().stateDuration = duration
    }

    fun getStateDuration() = currentLedCubeState().stateDuration

    fun isOn(coordinateX: Int, coordinateY: Int, layer: Int) : Boolean
            = currentLedCubeState().isOn(coordinateX, coordinateY, layer)

    fun setOn(on: Boolean, coordinateX: Int, coordinateY: Int, layer: Int) {
        currentLedCubeState().setOn(on, coordinateX, coordinateY, layer)
    }

    fun currentStateIndex() : Int = currentPosition

    fun stateCount() = ledCubeStates.size

    fun addNewLedCubeState() {
        ledCubeStates.add(ledCubeStates[currentPosition].copy())
        currentPosition = ledCubeStates.size - 1
        Log.d("add", "Position: $currentPosition, size: ${ledCubeStates.size} ")
    }

    fun removeLedCubeState(position: Int) {
        if (ledCubeStates.size > 1) {
            ledCubeStates.removeAt(position)
            if (position in 0..currentPosition && currentPosition > 0)
                stepBack()
            Log.d("remove", "Position: $currentPosition, size: ${ledCubeStates.size} ")
        }
    }

    fun jumpToIndex(position: Int) {
        currentPosition = position
    }

    fun stepBack() {
        if (currentPosition > 0) {
            currentPosition--
        }
    }

    fun stepForward() {
        if (currentPosition < ledCubeStates.size - 2) {
            currentPosition++
        }
    }

    fun logCurrentState() {
        currentLedCubeState().log()
    }

    private fun jsonWithChecksum(json: JSONObject) = with(json) {
        val encoded = Base64.encodeToString(json.toString().toByteArray(), Base64.DEFAULT)
        this.put("checksum", encoded)
    }

    fun toJson(): JSONObject {
        val result = JSONObject();
        val states = JSONArray();
        var objectCount = 0
        var memberCount = 0
        for ((index, currentState) in ledCubeStates.withIndex()) {
            val stateJson = JSONObject()
            stateJson.put("duration", currentState.stateDuration)
            val onArray = JSONArray()
            for (i in 0..5) {
                for (j in 0..5) {
                    for (k in 0..5) {
                        if (currentState.isOn(i,j,k)) {
                            val onObject = JSONObject()
                            onObject.put("x", i)
                            onObject.put("y", j)
                            onObject.put("z", k)
                            onArray.put(onObject)
                        }
                    }
                }
            }
            stateJson.put("powered", onArray)
            states.put(stateJson)
        }
        result.put("states", states)
        return result
    }
}