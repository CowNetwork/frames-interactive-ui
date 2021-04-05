package network.cow.frames.interactive.ui.component

import network.cow.frames.component.Component
import network.cow.frames.component.CompoundComponent
import network.cow.frames.interactive.Input
import network.cow.frames.interactive.ui.UICompanion
import network.cow.frames.interactive.ui.UserInterface
import network.cow.frames.interactive.ui.theme.DummyTheme
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

/**
 * @author Benedikt Wüller
 */
open class Group(position: Point, dimensions: Dimension) : CompoundComponent(position, dimensions), UserInterface {

    private val companion = UICompanion()

    var theme: Theme = DummyTheme()
        set(value) {
            field = value
            this.updateTheme(value)
        }

    override fun addComponent(component: Component?) {
        if (component is UIComponent) {
            component.theme = this.theme
        } else if (component is Group) {
            component.theme = this.theme
        }
        super.addComponent(component)
    }

    override fun addComponentBelow(component: Component?, other: Component) {
        if (component is UIComponent) {
            component.theme = this.theme
        } else if (component is Group) {
            component.theme = this.theme
        }
        super.addComponentBelow(component, other)
    }

    override fun getCompanion(): UICompanion = this.companion

    override fun getBounds(): Rectangle = this.calculateBounds()

    override fun onInputActivate(input: Input) {
        this.components.filter {
            !this.removedComponents.contains(it)
        }.forEach {
            if (it !is UserInterface) return@forEach
            it.activateInput(input)
        }
    }

    override fun onInputDeactivate(input: Input) {
        this.components.filter {
            !this.removedComponents.contains(it)
        }.forEach {
            if (it !is UserInterface) return@forEach
            it.deactivateInput(input)
        }
    }

    override fun update(currentTime: Long, delta: Long) {
        super.update(currentTime, delta)
        this.components.filter {
            !this.removedComponents.contains(it)
        }.forEach {
            if (it !is UserInterface) return@forEach
            it.update(currentTime, delta)
        }
    }

    override fun updateCursor(previousPosition: Point, position: Point) {
        super.updateCursor(previousPosition, position)

        val prevRelative = this.getRelativePosition(previousPosition)
        val newRelative = this.getRelativePosition(position)

        this.components.filter {
            !this.removedComponents.contains(it)
        }.forEach {
            if (it !is UserInterface) return@forEach
            it.updateCursor(prevRelative, newRelative)
        }
    }

    override fun reset() {
        this.components.forEach {
            if (it !is UserInterface) return@forEach
            it.reset()
        }
    }

    override fun updateTheme(theme: Theme) {
        this.components.forEach {
            if (it is UIComponent) {
                it.theme = this.theme
            } else if (it is Group) {
                it.theme = this.theme
            }
        }

        super.updateTheme(theme)
    }

}
