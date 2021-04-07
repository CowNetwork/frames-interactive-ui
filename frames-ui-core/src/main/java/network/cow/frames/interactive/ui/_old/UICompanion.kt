package network.cow.frames.interactive.ui._old

import network.cow.frames.interactive.Input
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class UICompanion(var cursorPosition: Point? = null) {

    var currentTime: Long = 0

    val activeInputs = mutableSetOf<Input>()

    fun reset() {
        this.cursorPosition = null
        this.currentTime = 0
    }

}
