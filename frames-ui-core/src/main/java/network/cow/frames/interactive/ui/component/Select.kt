package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import java.awt.Dimension
import java.awt.Point
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class Select(position: Point, dimensions: Dimension, private val options: Array<String>) : Group(position, dimensions) {

    companion object {
        private const val DISPLAY_WIDTH_PERCENTAGE = 0.7
        private const val PADDING_PERCENTAGE = 0.025
    }

    private val leftButton = LabelButton(Point(), Dimension(), "«", HorizontalAlignment.CENTER)
    private val rightButton = LabelButton(Point(), Dimension(), "»", HorizontalAlignment.CENTER)

    // TODO: this should be a scrolling text
    private val textDisplay = LabelButton(Point(),  Dimension(), this.options.first(), HorizontalAlignment.CENTER)

    private var currentIndex = 0

    var onSelect: ((String) -> Unit)? = null

    var isDisabled = false
        set(value) {
            if (field == value) return
            field = value
            this.leftButton.isDisabled = value
            this.rightButton.isDisabled = value
            this.textDisplay.isDisabled = value
        }

    init {
        this.leftButton.onActivate = {
            this.currentIndex -= 1
            if (this.currentIndex < 0) this.currentIndex = this.options.lastIndex

            val value = this.options[this.currentIndex]
            this.textDisplay.textComponent.text = value
            this.onSelect?.let { it(value) }
        }

        this.rightButton.onActivate = {
            this.currentIndex += 1
            if (this.currentIndex > this.options.lastIndex) this.currentIndex = 0

            val value = this.options[this.currentIndex]
            this.textDisplay.textComponent.text = value
            this.onSelect?.let { it(value) }
        }
    }

    override fun onEnable() {
        val displayWidth = (this.dimensions.width * DISPLAY_WIDTH_PERCENTAGE).roundToInt()
        val padding = floor(this.dimensions.width * PADDING_PERCENTAGE).toInt()
        val buttonWidth = (this.dimensions.width - displayWidth) / 2 - padding

        this.leftButton.dimensions.setSize(buttonWidth, this.dimensions.height)

        this.textDisplay.dimensions.setSize(displayWidth, this.dimensions.height)
        this.textDisplay.position.x = this.leftButton.dimensions.width + padding

        this.rightButton.dimensions.setSize(buttonWidth, this.dimensions.height)
        this.rightButton.position.x = this.textDisplay.dimensions.width + this.textDisplay.position.x + padding

        this.addComponent(this.leftButton)
        this.addComponent(this.textDisplay)
        this.addComponent(this.rightButton)
    }

}
