package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.interactive.ui.UICompanion
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class ScrollingText(
    position: Point,
    text: String?,
    color: Color,
    dimension: Dimension,
    fontName: String,
    fontStyle: Int = Font.PLAIN,
    alignment: HorizontalAlignment = HorizontalAlignment.LEFT
) : UIComponent(position, dimension) {

    companion object {
        const val FONT_HEIGHT_PERCENTAGE = 0.5
    }

    var text = text; set(value) {
        this.dirty = this.dirty || field != value
        field = value
        this.update()
    }

    var color = color; set(value) {
        this.dirty = this.dirty || field != value
        field = value
        this.update()
    }

    var fontName = fontName
        set(value) {
            this.dirty = this.dirty || field != value
            field = value
            this.updateFont()
        }

    var fontStyle = fontStyle
        set(value) {
            this.dirty = this.dirty || field != value
            field = value
            this.updateFont()
        }

    var alignment = alignment
        set(value) {
            this.dirty = this.dirty || field != value
            field = value
            this.updateFont()
        }

    private var font = Font(fontName, fontStyle, (this.dimensions.height * FONT_HEIGHT_PERCENTAGE).roundToInt()); set(value) {
        this.dirty = this.dirty || field != value
        field = value
        this.update()
    }

    private val companion = UICompanion()

    private var heightOffset = 0
    private var image: BufferedImage? = null
    private val textDimensions = Dimension()

    private var currentWidthCursor = 0

    private var steps = 0
    private var remainingDelta = 0.0

    var delayPerPixel: Double = 25.0
    var extraWidthFactor: Double = 0.5

    init {
        this.update()
    }

    private fun updateFont() {
        if (!this.dirty) return
        this.font = Font(fontName, fontStyle, (this.dimensions.height * FONT_HEIGHT_PERCENTAGE).roundToInt())
    }

    private fun update() {
        if (!this.dirty) return
        this.image = null
    }

    override fun getCompanion(): UICompanion = companion

    override fun update(currentTime: Long, delta: Long) {
        super.update(currentTime, delta)

        if (this.image == null) {
            this.dirty = true
            return
        }

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
        val stringWidth = this.text?.let { metrics.stringWidth(it) } ?: 0

        this.heightOffset = (metrics.height * 0.2).toInt()

        this.textDimensions.setSize(stringWidth, metrics.height + this.heightOffset)
        this.currentWidthCursor = 0
    }

    override fun onRender(context: Graphics2D, bounds: Rectangle) {
        if (this.image == null) {
            this.updateImageDimensions(context)

            this.image = BufferedImage(this.textDimensions.width, this.textDimensions.height, BufferedImage.TYPE_INT_ARGB)
            val sectionContext = this.image!!.createGraphics()
            sectionContext.font = this.font
            sectionContext.color = this.color
            sectionContext.drawString(this.text ?: return, 0, this.textDimensions.height - this.heightOffset)
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

    fun isTextHovered(): Boolean {
        if (!this.isHovered()) return false
        if (this.textDimensions.width > this.dimensions.width) return true

        val cursorPosition = this.companion.cursorPosition ?: return false

        return when (this.alignment) {
            HorizontalAlignment.LEFT -> cursorPosition.x >= 0 && cursorPosition.x < this.textDimensions.width
            HorizontalAlignment.RIGHT -> cursorPosition.x >= (this.dimensions.width - this.textDimensions.width) && cursorPosition.x < this.dimensions.width
            HorizontalAlignment.CENTER -> {
                val padding = (this.dimensions.width - this.textDimensions.width) / 2
                cursorPosition.x >= padding && cursorPosition.x < (this.dimensions.width - padding)
            }
        }
    }

    override fun getBounds(): Rectangle = this.calculateBounds()

    override fun reset() {
        this.currentWidthCursor = 0
        this.update()
    }

}
