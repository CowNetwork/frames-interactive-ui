package network.cow.frames.interactive.ui

import network.cow.frames.interactive.Input
import network.cow.frames.interactive.ui.component.Group
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Point
import java.awt.Rectangle

/**
 * @author Benedikt Wüller
 */
interface UserInterface {

    fun getCompanion() : UICompanion

    fun updateCursor(previousPosition: Point, position: Point) {
        val prevRelative = this.getRelativePosition(previousPosition)
        val newRelative = this.getRelativePosition(position)

        if (this.isInside(prevRelative) && !this.isInside(newRelative)) {
            this.getCompanion().cursorPosition = null
            this.getCompanion().activeInputs.forEach(this::deactivateInput)
            this.onMouseMove(position, newRelative)
            this.onMouseLeave(position, newRelative)
        } else if (!this.isInside(prevRelative) && this.isInside(newRelative)) {
            this.getCompanion().cursorPosition = newRelative
            this.onMouseEnter(position, newRelative)
            this.onMouseMove(position, newRelative)
        } else {
            this.getCompanion().cursorPosition = newRelative
            this.onMouseMove(position, newRelative)
        }
    }

    fun isHovered() = this.getCompanion().cursorPosition?.let { this.isInside(it) } ?: false

    fun isInside(position: Point): Boolean {
        val bounds = this.getBounds()
        if (position.x < 0 || position.x >= bounds.width) return false
        if (position.y < 0 || position.y >= bounds.height) return false
        return true
    }

    fun getRelativePosition(position: Point): Point {
        val bounds = this.getBounds()
        return Point(position.x - bounds.x, position.y - bounds.y)
    }

    fun getBounds(): Rectangle

    fun activateInput(input: Input) {
        if (!this.isHovered()) return
        this.getCompanion().activeInputs.add(input)
        this.onInputActivate(input)
    }

    fun deactivateInput(input: Input) {
        if (!this.getCompanion().activeInputs.remove(input)) return
        this.onInputDeactivate(input)
    }

    fun onInputActivate(input: Input) = Unit

    fun onInputDeactivate(input: Input) = Unit

    fun onMouseEnter(position: Point, relativePosition: Point) = Unit

    fun onMouseMove(position: Point, relativePosition: Point) = Unit

    fun onMouseLeave(position: Point, relativePosition: Point) = Unit

    fun update(currentTime: Long, delta: Long) {
        this.getCompanion().currentTime = currentTime
    }

    fun updateTheme(theme: Theme) {
        this.onUpdateTheme(theme)
    }

    fun onUpdateTheme(theme: Theme) = Unit

    fun reset() {
        this.getCompanion().reset()
    }

}
