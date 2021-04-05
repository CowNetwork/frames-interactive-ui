package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class LabeledSwitch(position: Point, dimensions: Dimension, private val text: String) : Group(position, dimensions) {

    companion object {
        private const val SWITCH_HEIGHT_PERCENTAGE = 0.9
        private const val SWITCH_WIDTH_PERCENTAGE = 0.15
        private const val SWITCH_MIN_WIDTH = 25
        private const val PADDING_PERCENTAGE = 0.03
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
            Point(), text, this.theme.textColorOnBackground, Dimension(),
            this.theme.fontName, this.theme.fontStyle, HorizontalAlignment.LEFT
        )

        this.switchComponent = Switch(Point(), Dimension())

        this.addComponent(this.textComponent)
        this.addComponent(this.switchComponent)
    }

    override fun onEnable() {
        this.update()
    }

    private fun update() {
        val switchWidth = maxOf((this.dimensions.width * SWITCH_WIDTH_PERCENTAGE).roundToInt(), SWITCH_MIN_WIDTH)
        val switchHeight = (this.dimensions.height * SWITCH_HEIGHT_PERCENTAGE).roundToInt()
        val padding = (this.dimensions.width * PADDING_PERCENTAGE).roundToInt()

        this.textComponent.dimensions.setSize(this.dimensions.width - padding - switchWidth, this.dimensions.height)
        this.textComponent.color = if (!this.isDisabled) this.theme.textColorOnBackground else this.theme.textColorDisabled
        this.textComponent.fontName = theme.fontName
        this.textComponent.fontStyle = theme.fontStyle

        this.switchComponent.position.location = Point(this.dimensions.width - switchWidth, (this.dimensions.height - switchHeight) / 2)
        this.switchComponent.dimensions.setSize(switchWidth, switchHeight)
    }

    override fun onUpdateTheme(theme: Theme) = this.update()

}
