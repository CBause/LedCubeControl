package bigchris.studying.datamodel

import android.util.Log

internal class LedMatrix {
    private val tag: String = "LedMatrix"
    private val ledMatrix: ArrayList<ArrayList<ArrayList<Boolean>>> = arrayListOf(
        arrayListOf(
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
        ),
        arrayListOf(
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
        ),
        arrayListOf(
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
        ),
        arrayListOf(
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
        ),
        arrayListOf(
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
        ),
        arrayListOf(
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
            arrayListOf(false, false, false, false, false, false),
        ),
    )

    fun isOn(coordinateX: Int, coordinateY: Int, layer: Int) : Boolean = ledMatrix[layer][coordinateY][coordinateX]

    fun setOn(on: Boolean, coordinateX: Int, coordinateY: Int, layer: Int) {
        ledMatrix[layer][coordinateY][coordinateX] = on
    }

    fun copy() : LedMatrix = LedMatrix().also {
        for (layer in 0..5) {
            for (y in 0..5) {
                for (x in 0..5) {
                    it.setOn(this.isOn(layer, y, x), layer, y, x)
                }
            }
        }
    }

    fun log() {
        val xStateArray: Array<Boolean> = Array(6) {false}
        for (layer in 0..5) {
            for (y in 0..5) {
                for (x in 0..5) {
                    xStateArray[x] = isOn(x, y, layer)
                }
                Log.d(tag, "${xStateArray[0]} ${xStateArray[1]} ${xStateArray[2]} ${xStateArray[3]} ${xStateArray[4]} ${xStateArray[5]}")
            }
        }
    }
}