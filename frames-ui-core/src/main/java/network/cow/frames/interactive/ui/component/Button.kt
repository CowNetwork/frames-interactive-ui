package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.component.ColorComponent
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui.Positions
import java.awt.Dimension
import java.awt.Point
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
class Button(
    position: Point = Point(),
    dimensions: Dimension = Dimensions.matchParent(),
    text: String,
    alignment: HorizontalAlignment = HorizontalAlignment.CENTER
) : Group(position, dimensions) {

    companion object {
        private const val UNDERLINE_PERCENTAGE = 0.08

        private const val TEXT_HORIZONTAL_PADDING = 0.1
        private const val TEXT_HEIGHT_PERCENTAGE = 0.925
    }

    var content: String by Delegates.observable(text, ::observeProperty)

    var alignment: HorizontalAlignment by Delegates.observable(alignment, ::observeProperty)

    var isToggle: Boolean by Delegates.observable(false, ::observeProperty)

    var isActive: Boolean by Delegates.observable(false, ::observeProperty)

    private val background = ColorComponent(Point(), Dimensions.matchParent(), this.theme.highlightColor)

    private val underline = ColorComponent(
        Point(0, Positions.matchParentPercent(1.0 - UNDERLINE_PERCENTAGE)),
        Dimension(Dimensions.MATCH_PARENT, Dimensions.matchParentPercent(UNDERLINE_PERCENTAGE)),
        this.theme.highlightColorDark
    )

    private val text = Text(
        Point(Positions.matchParentPercent(TEXT_HORIZONTAL_PADDING * 0.5), 0),
        Dimension(Dimensions.matchParentPercent(1.0 - TEXT_HORIZONTAL_PADDING), Dimensions.matchParentPercent(TEXT_HEIGHT_PERCENTAGE)),
        this.content, this.theme.textColorOnHighlight, this.alignment
    )

    init {
        this.add(this.background)
        this.add(this.underline)
        this.add(this.text)
    }

    override fun onUpdate() {
        when {
            this.isDisabled -> {
                this.background.color = this.theme.highlightColorDark
                this.underline.color = this.theme.highlightColor
                this.text.color = this.theme.textColorDisabled
            }
            this.isActive -> {
                this.background.color = this.theme.accentColor
                this.underline.color = this.theme.accentColorDark
                this.text.color = this.theme.textColorOnAccent
            }
            this.isHovered -> {
                this.background.color = this.theme.highlightColor
                this.underline.color = this.theme.accentColor
                this.text.color = this.theme.textColorOnHighlight
            }
            else -> {
                this.background.color = this.theme.highlightColor
                this.underline.color = this.theme.highlightColorDark
                this.text.color = this.theme.textColorOnHighlight
            }
        }

        this.text.content = this.content
        this.text.alignment = this.alignment
        super.onUpdate()
    }

    override fun onMouseDown() {
        if (this.isToggle) {
            this.isActive = !this.isActive
        } else {
            this.isActive = true
        }
        super.onMouseDown()
    }

    override fun onMouseUp() {
        if (!this.isToggle) {
            this.isActive = false
        }
        super.onMouseUp()
    }

    override fun getObservedProperties(): Array<KProperty<*>> {
        return arrayOf(*super.getObservedProperties(), this::content, this::alignment, this::isActive, this::isToggle)
    }

}
