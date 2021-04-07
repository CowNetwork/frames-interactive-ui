package network.cow.frames.interactive.ui._old

import network.cow.frames.interactive.ui._old.component.Group
import network.cow.frames.interactive.ui._old.component.Window
import network.cow.frames.interactive.ui.theme.DefaultTheme
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import java.util.Stack

/**
 * @author Benedikt Wüller
 */
class WindowManager(dimensions: Dimension, private val main: Window, initialTheme: Theme = DefaultTheme()) : Group(Point(), dimensions) {

    private val windows = Stack<Window>()

    init {
        this.theme = initialTheme
        this.navigateTo(this.main)
    }

    fun canNavigateUp() = this.windows.size > 1

    fun navigateUp() {
        this.removeComponent(this.windows.pop())
        this.addComponent(this.windows.peek())
    }

    fun navigateTo(window: Window) {
        if (this.windows.contains(window)) {
            while (this.windows.peek() != window) {
                this.navigateUp()
            }
            return
        }

        if (!this.windows.isEmpty()) {
            this.removeComponent(this.windows.peek())
        }

        window.manager = this

        this.windows.add(window)
        this.addComponent(window)
    }

}

