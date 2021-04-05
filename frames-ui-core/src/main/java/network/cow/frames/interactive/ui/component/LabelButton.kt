package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.interactive.Input
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class LabelButton(
    position: Point, dimensions: Dimension,
    text: String, alignment: HorizontalAlignment = HorizontalAlignment.LEFT
) : Group(position, dimensions) {

    var isActive = false; private set

    var isDisabled = false
        set(value) {
            if (field == value) return

            if (field && !value) {
                this.isActive = false
                this.onDeactivate?.let { it() }
            }

            field = value

            this.update()
        }

    var alignment = alignment
        set(value) {
            field = value
            textComponent.alignment = value
        }

    val textComponent: ScrollingText = ScrollingText(
        Point(0, 0), text,
        this.theme.textColorOnHighlight,
        this.dimensions,
        this.theme.fontName, this.theme.fontStyle,
        this.alignment
    )

    var onActivate: (() -> Unit)? = null
    var onDeactivate: (() -> Unit)? = null

    init {
        this.addComponent(this.textComponent)
    }

    private fun update() {
        when {
            this.isDisabled -> this.textComponent.color = this.theme.textColorDisabled
            this.textComponent.isTextHovered() -> this.textComponent.color = this.theme.textColorOnHighlight
            else -> this.textComponent.color = this.theme.textColorOnBackground
        }

        this.textComponent.fontName = this.theme.fontName
        this.textComponent.fontStyle = this.theme.fontStyle
    }

    override fun onInputActivate(input: Input) {
        if (input != Input.INTERACT_PRIMARY) return
        if (!this.textComponent.isTextHovered()) return

        if (this.isActive) return
        if (this.isDisabled) return
        this.isActive = true

        this.onActivate?.let { it() }
    }

    override fun onInputDeactivate(input: Input) {
        if (input != Input.INTERACT_PRIMARY) return
        if (!this.textComponent.isTextHovered()) return

        if (!this.isActive) return
        this.isActive = false

        this.onDeactivate?.let { it() }
    }

    override fun onMouseMove(position: Point, relativePosition: Point) = this.update()

    override fun onMouseLeave(position: Point, relativePosition: Point) = this.update()

    override fun onMouseEnter(position: Point, relativePosition: Point) = this.update()

    override fun onUpdateTheme(theme: Theme) {
        this.update()
    }

}
