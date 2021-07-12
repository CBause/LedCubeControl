package bigchris.studying.datamodel

internal class LedCubeState(private val ledMatrix: LedMatrix = LedMatrix()) {
    var stateDuration: Int = 100

    fun isOn(coordinateX: Int, coordinateY: Int, layer: Int)
            : Boolean = ledMatrix.isOn(coordinateX, coordinateY, layer)

    fun setOn(on: Boolean, coordinateX: Int, coordinateY: Int, layer: Int)
            = ledMatrix.setOn(on, coordinateX, coordinateY, layer)

    fun copy() : LedCubeState {
        val result = LedCubeState(ledMatrix.copy())
        result.stateDuration = stateDuration
        return result
    }

    fun log() {
        ledMatrix.log()
    }

}