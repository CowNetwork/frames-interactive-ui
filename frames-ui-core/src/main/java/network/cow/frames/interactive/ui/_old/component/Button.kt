package network.cow.frames.interactive.ui._old.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.component.ColorComponent
import network.cow.frames.interactive.Input
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class Button(
    position: Point, dimensions: Dimension,
    text: String, alignment: HorizontalAlignment = HorizontalAlignment.CENTER
) : Group(position, dimensions) {

    companion object {
        private const val UNDERLINE_PERCENTAGE = 0.08

        private const val TEXT_HEIGHT_PERCENTAGE = 0.9
        private const val TEXT_MIN_HEIGHT = 17
    }

    var onActivate: (() -> Unit)? = null
    var onDeactivate: (() -> Unit)? = null

    var isDisabled = false
        set(value) {
            if (field == value) return
            field = value

            if (value && this.isActive) {
                this.isActive = false
            }

            this.update()
        }

    var isToggle = false

    var isActive = false
        set(value) {
            if (field == value) return
            if (value && this.isDisabled) return
            field = value

            if (value) {
                this.onActivate?.let { it() }
            } else {
                this.onDeactivate?.let { it() }
            }

            this.update()
        }

    var alignment = alignment
        set(value) {
            field = value
            this.textComponent.alignment = value
        }

    private val backgroundComponent = ColorComponent(Point(), Dimension(), this.theme.highlightColor)
    private val underlineComponent = ColorComponent(Point(), Dimension(), this.theme.highlightColorDark)

    val textComponent: ScrollingText

    init {
        this.addComponent(this.backgroundComponent)
        this.addComponent(this.underlineComponent)

        val textHeight = maxOf((this.dimensions.height * TEXT_HEIGHT_PERCENTAGE).roundToInt(), TEXT_MIN_HEIGHT)

        val verticalPadding = (this.dimensions.height - this.underlineComponent.dimensions.height - textHeight) / 2
        val horizontalPadding = ((this.dimensions.height - this.underlineComponent.dimensions.height - (textHeight * ScrollingText.FONT_HEIGHT_PERCENTAGE)) / 2).roundToInt()

        this.textComponent = ScrollingText(
            Point(horizontalPadding, verticalPadding), text,
            this.theme.textColorOnHighlight,
            Dimension(this.dimensions.width - horizontalPadding * 2, textHeight),
            this.theme.fontName, this.theme.fontStyle,
            this.alignment
        )

        this.addComponent(this.textComponent)
    }

    override fun onShow() {
        this.backgroundComponent.dimensions.setSize(this.dimensions.width, this.dimensions.height)

        this.underlineComponent.position.location = Point(0, (this.dimensions.height * (1.0 - UNDERLINE_PERCENTAGE)).toInt())
        this.underlineComponent.dimensions.setSize(this.dimensions.width, ceil(this.dimensions.height * UNDERLINE_PERCENTAGE).toInt())
    }

    private fun update() {
        when {
            this.isDisabled -> {
                this.backgroundComponent.color = theme.highlightColorDark
                this.underlineComponent.color = theme.highlightColor
                this.textComponent.color = theme.textColorDisabled
            }
            this.isActive -> {
                this.backgroundComponent.color = theme.accentColor
                this.underlineComponent.color = theme.accentColorDark
                this.textComponent.color = theme.textColorOnAccent
            }
            this.isHovered() -> {
                this.backgroundComponent.color = theme.highlightColor
                this.underlineComponent.color = theme.accentColor
                this.textComponent.color = theme.textColorOnHighlight
            }
            else -> {
                this.backgroundComponent.color = theme.highlightColor
                this.underlineComponent.color = theme.highlightColorDark
                this.textComponent.color = theme.textColorOnHighlight
            }
        }

        this.textComponent.fontName = theme.fontName
        this.textComponent.fontStyle = theme.fontStyle
    }

    override fun onMouseEnter(position: Point, relativePosition: Point) = this.update()

    override fun onMouseLeave(position: Point, relativePosition: Point) = this.update()

    override fun onInputActivate(input: Input) {
        if (input != Input.INTERACT_PRIMARY) return
        if (this.isToggle) {
            this.isActive = !this.isActive
        } else {
            this.isActive = true
        }
    }

    override fun onInputDeactivate(input: Input) {
        if (input != Input.INTERACT_PRIMARY) return
        if (this.isToggle) return
        this.isActive = false
    }

    override fun onUpdateTheme(theme: Theme) {
        this.update()
    }

}
