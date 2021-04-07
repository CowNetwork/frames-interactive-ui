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
class TextButton(position: Point = Point(), dimensions: Dimension = Dimensions.matchParent(), text: String, alignment: HorizontalAlignment = HorizontalAlignment.LEFT) : Group(position, dimensions) {

    var content: String by Delegates.observable(text, ::observeProperty)

    var alignment: HorizontalAlignment by Delegates.observable(alignment, ::observeProperty)

    private val text = Text(Point(), Dimensions.matchParent(), this.content, this.theme.textColor, this.alignment)

    init {
        this.add(this.text)
    }

    override fun onUpdate() {
        this.text.color = when {
            this.isDisabled -> this.theme.textColorDisabled
            this.isHovered -> this.theme.textColorAccent
            else -> this.theme.textColor
        }

        this.text.content = this.content
        this.text.alignment = this.alignment

        super.onUpdate()
    }

    override fun getObservedProperties(): Array<KProperty<*>> = arrayOf(*super.getObservedProperties(), this::content, this::alignment)

}
