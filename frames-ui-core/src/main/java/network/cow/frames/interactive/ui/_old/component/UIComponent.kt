package network.cow.frames.interactive.ui._old.component

import network.cow.frames.component.Component
import network.cow.frames.interactive.ui._old.UICompanion
import network.cow.frames.interactive.ui._old.UserInterface
import network.cow.frames.interactive.ui.theme.DefaultTheme
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

/**
 * @author Benedikt Wüller
 */
abstract class UIComponent(position: Point, dimensions: Dimension) : Component(position, dimensions), UserInterface {

    var theme: Theme = DefaultTheme()
        set(value) {
            if (field == value) return
            field = value
            this.updateTheme(value)
        }

    private val companion = UICompanion()

    override fun getCompanion(): UICompanion = this.companion

    override fun getBounds(): Rectangle = this.calculateBounds()

}
