package network.cow.frames.interactive.ui.example

import network.cow.frames.interactive.standalone.FrameWindow
import java.awt.Dimension

/**
 * @author Benedikt Wüller
 */

fun main() {
    val window = FrameWindow({ ExampleFrame(Dimension(256, 256)) }, "ExampleFrame", 2.0)
    window.start()
}
