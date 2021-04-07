package network.cow.frames.interactive.ui._old.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class LabeledSelect(position: Point, dimensions: Dimension, text: String, options: Array<String>) : Group(position, dimensions) {

    companion object {
        private const val SELECT_HEIGHT_PERCENTAGE = 1.0
        private const val SELECT_WIDTH_PERCENTAGE = 0.375
        private const val SELECT_MIN_WIDTH = 45

        private const val TEXT_WIDTH_PERCENTAGE = 0.6
    }

    var isDisabled
        set(value) {
            this.selectComponent.isDisabled = value
            this.update()
        }
        get() = this.selectComponent.isDisabled

    var onSelect: ((String) -> Unit)?
        set(value) { this.selectComponent.onSelect = value }
        get() = this.selectComponent.onSelect

    val textComponent: ScrollingText

    private val selectComponent: Select

    init {
        this.textComponent = ScrollingText(
            Point(), text, this.theme.textColor, Dimension(),
            this.theme.fontName, this.theme.fontStyle, HorizontalAlignment.LEFT
        )

        this.selectComponent = Select(Point(), Dimension(), options)
    }

    override fun onShow() {
        this.update()

        this.clear()
        this.addComponent(this.textComponent)
        this.addComponent(this.selectComponent)
    }

    private fun update() {
        val switchWidth = maxOf((this.dimensions.width * SELECT_WIDTH_PERCENTAGE).roundToInt(), SELECT_MIN_WIDTH)
        val switchHeight = (this.dimensions.height * SELECT_HEIGHT_PERCENTAGE).roundToInt()
        val textWidth = (this.dimensions.width * TEXT_WIDTH_PERCENTAGE).roundToInt()

        this.textComponent.dimensions.setSize(textWidth, this.dimensions.height)
        this.textComponent.color = if (!this.isDisabled) this.theme.textColor else this.theme.textColorDisabled
        this.textComponent.fontName = theme.fontName
        this.textComponent.fontStyle = theme.fontStyle

        this.selectComponent.position.location = Point(this.dimensions.width - switchWidth, (this.dimensions.height - switchHeight) / 2)
        this.selectComponent.dimensions.setSize(switchWidth, switchHeight)
    }

    override fun onUpdateTheme(theme: Theme) = this.update()

}
