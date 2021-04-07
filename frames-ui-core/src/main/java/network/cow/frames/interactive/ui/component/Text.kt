package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui._old.component.ScrollingText
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.roundToInt
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
class Text(position: Point, dimensions: Dimension = Dimensions.matchParent(), text: String, color: Color, alignment: HorizontalAlignment = HorizontalAlignment.LEFT) : UIComponent(position, dimensions) {

    companion object {
        const val FONT_HEIGHT_PERCENTAGE = 0.5
    }

    private var heightOffset = 0
    private var image: BufferedImage? = null
    private val textDimensions = Dimension()

    private var currentWidthCursor = 0

    private var steps = 0
    private var remainingDelta = 0.0

    private var font = Font(this.theme.fontName, this.theme.fontStyle, (this.dimensions.height * ScrollingText.FONT_HEIGHT_PERCENTAGE).roundToInt())

    var content: String by Delegates.observable(text, ::observeProperty)

    var color: Color by Delegates.observable(color, ::observeProperty)

    var alignment: HorizontalAlignment by Delegates.observable(alignment, ::observeProperty)

    var isTextHovered: Boolean by Delegates.observable(false, ::observeProperty)

    var delayPerPixel: Double = 25.0
    var extraWidthFactor: Double = 1.0

    override fun onUpdateTime(currentTime: Long, delta: Long) {
        if (this.textDimensions.width <= 0) return

        val stepSize = this.delayPerPixel
        val totalDelta = delta + this.remainingDelta
        this.remainingDelta = totalDelta % stepSize
        this.steps += (totalDelta / stepSize).toInt()

        val currentWidthCursor = this.currentWidthCursor
        val extraWidth = this.dimensions.width * this.extraWidthFactor
        this.currentWidthCursor = maxOf(minOf((this.steps % (this.textDimensions.width + extraWidth)).roundToInt(), this.textDimensions.width), this.dimensions.width)

        if (currentWidthCursor != this.currentWidthCursor) {
            this.dirty = true
        }
    }

    private fun updateImageDimensions(context: Graphics2D) {
        val metrics = context.getFontMetrics(this.font)
        val stringWidth = metrics.stringWidth(this.content)

        this.heightOffset = (metrics.height * 0.2).toInt()

        this.textDimensions.setSize(stringWidth, metrics.height + this.heightOffset)
        this.currentWidthCursor = 0
    }

    override fun onRender(context: Graphics2D, bounds: Rectangle) {
        if (this.font.size <= 0) return

        if (this.image == null) {
            this.updateImageDimensions(context)

            this.image = BufferedImage(this.textDimensions.width, this.textDimensions.height, BufferedImage.TYPE_INT_ARGB)
            val sectionContext = this.image!!.createGraphics()
            sectionContext.font = this.font
            sectionContext.color = this.color
            sectionContext.drawString(this.content, 0, this.textDimensions.height - this.heightOffset)
        }

        val minX = bounds.minX.toInt()
        val minY = bounds.minY.toInt()
        val maxX = bounds.maxX.toInt()
        val maxY = bounds.maxY.toInt()

        val srcMaxX = if (this.alignment == HorizontalAlignment.CENTER && this.textDimensions.width < this.dimensions.width) {
            this.textDimensions.width + (this.dimensions.width - this.textDimensions.width) / 2 - (this.dimensions.width - maxX)
        } else if (this.alignment == HorizontalAlignment.RIGHT && this.textDimensions.width < this.dimensions.width) {
            this.textDimensions.width - (this.dimensions.width - maxX)
        } else {
            this.currentWidthCursor - (this.dimensions.width - maxX)
        }

        val srcMinX = srcMaxX - (maxX - minX)

        context.drawImage(this.image!!, minX, minY, maxX, maxY, srcMinX, minY, srcMaxX, maxY, null)
    }

    private fun updateTextHovered() {
        if (!this.isHovered) {
            this.isTextHovered = false
            return
        }

        if (this.textDimensions.width > this.dimensions.width) {
            this.isTextHovered = true
            return
        }

        val cursorPosition = this.cursorPosition
        if (cursorPosition == null) {
            this.isTextHovered = false
            return
        }

        this.isTextHovered = when (this.alignment) {
            HorizontalAlignment.LEFT -> cursorPosition.x >= 0 && cursorPosition.x < this.textDimensions.width
            HorizontalAlignment.RIGHT -> cursorPosition.x >= (this.dimensions.width - this.textDimensions.width) && cursorPosition.x < this.dimensions.width
            HorizontalAlignment.CENTER -> {
                val padding = (this.dimensions.width - this.textDimensions.width) / 2
                cursorPosition.x >= padding && cursorPosition.x < (this.dimensions.width - padding)
            }
        }
    }

    override fun onUpdate() {
        this.dirty = true
        this.image = null
    }

    private fun updateFont() {
        this.font = Font(this.theme.fontName, this.theme.fontStyle, (this.dimensions.height * FONT_HEIGHT_PERCENTAGE).roundToInt())
    }

    override fun updateCursorPosition(position: Point) {
        super.updateCursorPosition(position)
        this.updateTextHovered()
    }

    override fun onPropertyChanged(propertyName: String) {
        if (propertyName == this::theme.name) {
            this.updateFont()
        }

        if (propertyName != this::color.name) {
            this.steps = 0
        }

        super.onPropertyChanged(propertyName)
    }

    override fun onDimensionsChanged(oldDimensions: Dimension, newDimensions: Dimension) {
        this.updateFont()
        super.onDimensionsChanged(oldDimensions, newDimensions)
    }

    override fun onMouseEnter() = Unit

    override fun onMouseLeave() = Unit

    override fun onMouseDown() = Unit

    override fun onMouseUp() = Unit

    override fun getObservedProperties(): Array<KProperty<*>> = arrayOf(*super.getObservedProperties(), this::content, this::alignment, this::color)

}
