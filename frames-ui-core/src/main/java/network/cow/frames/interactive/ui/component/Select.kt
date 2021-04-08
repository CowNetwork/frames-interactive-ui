package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.interactive.ui.Dimensions
import java.awt.Dimension
import java.awt.Point
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
class Select(position: Point = Point(), dimensions: Dimension = Dimensions.matchParent()) : Group(position, dimensions) {

    companion object {
        private const val DISPLAY_WIDTH_PERCENTAGE = 0.7
        private const val PADDING_PERCENTAGE = 0.025
        private const val EMPTY_CONTENT = "-"
    }

    var options: Array<String> by Delegates.observable(emptyArray(), ::observeProperty)

    var selectedIndex: Int by Delegates.observable(-1, ::observeProperty)

    private val left: TextButton
    private val display: TextButton
    private val right: TextButton

    init {
        val buttonWidth = (1.0 - DISPLAY_WIDTH_PERCENTAGE) / 2 - PADDING_PERCENTAGE

        this.left = TextButton(Point(), Dimension(Dimensions.matchParentPercent(buttonWidth), Dimensions.MATCH_PARENT), "<", HorizontalAlignment.CENTER)

        this.display = TextButton(
            Point(Dimensions.matchParentPercent(buttonWidth + PADDING_PERCENTAGE), 0),
            Dimension(Dimensions.matchParentPercent(DISPLAY_WIDTH_PERCENTAGE), Dimensions.MATCH_PARENT),
            EMPTY_CONTENT, HorizontalAlignment.CENTER
        )

        this.right = TextButton(Point(Dimensions.matchParentPercent(1.0 - buttonWidth), 0), Dimension(Dimensions.matchParentPercent(buttonWidth), Dimensions.MATCH_PARENT), ">", HorizontalAlignment.CENTER)

        val next = { value: Boolean ->
            if (value) {
                val index = this.selectedIndex + 1
                if (index > this.options.lastIndex) {
                    this.selectedIndex = 0
                } else {
                    this.selectedIndex = index
                }
            }
        }

        this.left.setListener(this.left::isMouseDown) { _, new ->
            if (new) {
                val index = this.selectedIndex - 1
                if (index < 0) {
                    this.selectedIndex = this.options.lastIndex
                } else {
                    this.selectedIndex = index
                }
            }
        }
        this.display.setListener(this.left::isMouseDown) { _, new -> next(new) }
        this.right.setListener(this.left::isMouseDown) { _, new -> next(new) }

        this.add(this.left)
        this.add(this.display)
        this.add(this.right)
    }

    override fun onUpdate() {
        if (this.options.isNotEmpty()) {
            if (this.selectedIndex < 0 || this.selectedIndex > this.options.lastIndex) {
                this.selectedIndex = 0
                return
            }

            this.display.content = this.options[this.selectedIndex]
        } else {
            this.display.content = EMPTY_CONTENT
        }

        super.onUpdate()
    }

    override fun getObservedProperties(): Array<KProperty<*>> {
        return arrayOf(*super.getObservedProperties(), this::options, this::selectedIndex)
    }

}
