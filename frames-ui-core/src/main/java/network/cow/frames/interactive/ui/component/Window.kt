package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.component.ColorComponent
import network.cow.frames.component.Component
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class Window(dimensions: Dimension, initialTheme: Theme) : Group(Point(), dimensions) {

    companion object {
        private const val NAVIGATION_HEIGHT_PERCENTAGE = 0.07
        private const val NAVIGATION_MIN_HEIGHT = 15
        private const val NAVIGATION_MAX_HEIGHT = 18

        private const val NAVIGATION_PADDING_PERCENTAGE = 0.015625 // = 2 / 128
        private const val PADDING_PERCENTAGE = 0.0390625 // = 5 / 128
    }

    var parent: Window? = null

    private val backgroundComponent = ColorComponent(Point(), this.dimensions, this.theme.backgroundColor)

    // TODO: replace with cursor image
    private val cursorComponent = ColorComponent(Point(), Dimension(5, 8), Color.WHITE)

    private val contentView: Group

    val contentDimensions: Dimension; get() = this.contentView.dimensions

    init {
        val navigationHeight = minOf(maxOf((NAVIGATION_HEIGHT_PERCENTAGE * this.dimensions.width).roundToInt(), NAVIGATION_MIN_HEIGHT), NAVIGATION_MAX_HEIGHT)
        val navigationPadding = (NAVIGATION_PADDING_PERCENTAGE * this.dimensions.width).roundToInt()
        val padding = (PADDING_PERCENTAGE * this.dimensions.width).roundToInt()

        this.contentView = Group(
            Point(padding, navigationHeight + navigationPadding * 2),
            Dimension(this.dimensions.width - padding * 2, this.dimensions.height - (navigationHeight + navigationPadding * 2) - padding)
        )

        val buttonWidth = (this.dimensions.width - navigationPadding) / 2 - padding

        val backButton = LabelButton(Point(padding, navigationPadding), Dimension(buttonWidth, navigationHeight), "⮪ BACK")
        backButton.isDisabled = true

        val closeButton = LabelButton(Point(this.dimensions.width - buttonWidth - padding, navigationPadding), Dimension(buttonWidth, navigationHeight), "CLOSE", HorizontalAlignment.RIGHT)

        super.addComponent(this.backgroundComponent)
        super.addComponent(backButton)
        super.addComponent(closeButton)
        super.addComponent(this.contentView)
        super.addComponent(this.cursorComponent)

        this.theme = initialTheme
    }

    override fun addComponent(component: Component?) {
        this.contentView.addComponent(component)
    }

    override fun addComponentBelow(component: Component?, other: Component) {
        this.contentView.addComponentBelow(component, other)
    }

    override fun updateCursor(previousPosition: Point, position: Point) {
        super.updateCursor(previousPosition, position)
        this.cursorComponent.position.location = position
    }

    override fun onUpdateTheme(theme: Theme) {
        this.backgroundComponent.color = theme.backgroundColor
    }

}
