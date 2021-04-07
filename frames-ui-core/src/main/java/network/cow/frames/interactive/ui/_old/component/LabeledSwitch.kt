package network.cow.frames.interactive.ui._old.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class LabeledSwitch(position: Point, dimensions: Dimension, text: String) : Group(position, dimensions) {

    companion object {
        private const val SWITCH_HEIGHT_PERCENTAGE = 0.9
        private const val SWITCH_WIDTH_PERCENTAGE = 0.15
        private const val SWITCH_MIN_WIDTH = 25

        private const val TEXT_WIDTH_PERCENTAGE = 0.6
    }

    var isDisabled
        set(value) {
            this.switchComponent.isDisabled = value
            this.update()
        }
        get() = this.switchComponent.isDisabled

    var isActive: Boolean
        set(value) {
            this.switchComponent.isActive = value
            this.update()
        }
        get() = this.switchComponent.isActive

    var onToggle: ((Boolean) -> Unit)?
        set(value) { this.switchComponent.onToggle = value }
        get() = this.switchComponent.onToggle

    val textComponent: ScrollingText

    private val switchComponent: Switch

    init {
        this.textComponent = ScrollingText(
            Point(), text, this.theme.textColor, Dimension(),
            this.theme.fontName, this.theme.fontStyle, HorizontalAlignment.LEFT
        )

        this.switchComponent = Switch(Point(), Dimension())
    }

    override fun onShow() {
        this.update()

        this.clear()
        this.addComponent(this.textComponent)
        this.addComponent(this.switchComponent)
    }

    private fun update() {
        val switchWidth = maxOf((this.dimensions.width * SWITCH_WIDTH_PERCENTAGE).roundToInt(), SWITCH_MIN_WIDTH)
        val switchHeight = (this.dimensions.height * SWITCH_HEIGHT_PERCENTAGE).roundToInt()
        val textWidth = (this.dimensions.width * TEXT_WIDTH_PERCENTAGE).roundToInt()

        this.textComponent.dimensions.setSize(textWidth, this.dimensions.height)
        this.textComponent.color = if (!this.isDisabled) this.theme.textColor else this.theme.textColorDisabled
        this.textComponent.fontName = theme.fontName
        this.textComponent.fontStyle = theme.fontStyle

        this.switchComponent.position.location = Point(this.dimensions.width - switchWidth, (this.dimensions.height - switchHeight) / 2)
        this.switchComponent.dimensions.setSize(switchWidth, switchHeight)
    }

    override fun onUpdateTheme(theme: Theme) = this.update()

}
