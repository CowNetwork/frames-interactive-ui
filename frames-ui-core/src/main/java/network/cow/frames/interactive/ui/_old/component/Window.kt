package network.cow.frames.interactive.ui._old.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.component.ColorComponent
import network.cow.frames.component.Component
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui._old.WindowManager
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class Window(dimensions: Dimension = Dimensions.matchParent()) : Group(Point(), dimensions) {

    companion object {
        private const val NAVIGATION_HEIGHT_PERCENTAGE = 0.07
        private const val NAVIGATION_MIN_HEIGHT = 15
        private const val NAVIGATION_MAX_HEIGHT = 18

        private const val NAVIGATION_PADDING_PERCENTAGE = 0.015625 // = 2 / 128
        private const val PADDING_PERCENTAGE = 0.0390625 // = 5 / 128
    }

    lateinit var manager: WindowManager

    private val backgroundComponent = ColorComponent(Point(), this.dimensions, this.theme.backgroundColor)

    private val backButton = LabelButton(Point(), Dimension(), "⮪ BACK")
    private val closeButton = LabelButton(Point(), Dimension(), "CLOSE", HorizontalAlignment.RIGHT)

    // TODO: replace with cursor image
    private val cursorComponent = ColorComponent(Point(), Dimension(5, 8), Color.WHITE)

    private val contentView = Group(Point(), Dimension())

    init {
        super.addComponent(this.backgroundComponent)
    }

    override fun onShow() {
        val navigationHeight = minOf(maxOf((NAVIGATION_HEIGHT_PERCENTAGE * this.dimensions.width).roundToInt(), NAVIGATION_MIN_HEIGHT), NAVIGATION_MAX_HEIGHT)
        val navigationPadding = (NAVIGATION_PADDING_PERCENTAGE * this.dimensions.width).roundToInt()
        val padding = (PADDING_PERCENTAGE * this.dimensions.width).roundToInt()

        this.contentView.position.location = Point(padding, navigationHeight + navigationPadding * 2)
        this.contentView.dimensions.setSize(this.dimensions.width - padding * 2, this.dimensions.height - (navigationHeight + navigationPadding * 2) - padding)

        val buttonWidth = (this.dimensions.width - navigationPadding) / 2 - padding

        backButton.position.location = Point(padding, navigationPadding)
        backButton.dimensions.setSize(buttonWidth, navigationHeight)
        backButton.isDisabled = !this.manager.canNavigateUp()
        backButton.onActivate = { this.manager.navigateUp() }

        closeButton.position.location = Point(this.dimensions.width - buttonWidth - padding, navigationPadding)
        closeButton.dimensions.setSize(buttonWidth, navigationHeight)

        super.addComponent(this.backButton)
        super.addComponent(this.closeButton)
        super.addComponent(this.contentView)
    }

    override fun addComponent(component: Component?) {
        if (component is Window) {
            throw IllegalArgumentException("Windows must not be added inside of windows.")
        }
        this.contentView.addComponent(component)
    }

    override fun addComponentBelow(component: Component?, other: Component) {
        if (component is Window) {
            throw IllegalArgumentException("Windows must not be added inside of windows.")
        }
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
