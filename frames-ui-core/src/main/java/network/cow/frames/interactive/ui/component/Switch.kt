package network.cow.frames.interactive.ui.component

import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui.animate
import network.cow.frames.interactive.ui.easeOutQuad
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.properties.Delegates

/**
 * @author Benedikt Wüller
 */
class Switch(position: Point = Point(), dimensions: Dimension = Dimensions.matchParent()) : UIComponent(position, dimensions) {

    companion object {
        private const val INNER_RADIUS_PERCENTAGE = 0.74
        private const val HOVER_OFFSET_PERCENTAGE = 0.075
        private const val MAX_ANIMATION_DURATION = 1000L
    }

    private var animationStartedAt = 0L
    private var animationDuration = 0L
    private var sourcePosition = 0
    private var targetPosition = 0

    private var diameter = 0
    private var knobDiameter = 0
    private var knobOffset = 0
    private var knobPosition = -1

    private var backgroundColor = Color.WHITE
    private var knobColor = Color.BLACK

    var isActive: Boolean by Delegates.observable(false, ::observeProperty)

    override fun onUpdate() {
        this.dirty = true

        this.diameter = this.dimensions.height
        this.knobDiameter = ceil(diameter * INNER_RADIUS_PERCENTAGE).toInt()
        this.knobOffset = (this.diameter - this.knobDiameter) / 2

        val hoverOffset = if (!this.isDisabled && this.isHovered) floor(this.dimensions.width * HOVER_OFFSET_PERCENTAGE).toInt() else 0

        val targetPosition = when {
            this.isActive -> this.dimensions.width - this.knobDiameter - this.knobOffset - hoverOffset
            else -> this.knobOffset + hoverOffset
        }

        if (this.knobPosition < 0) {
            this.knobPosition = targetPosition
            this.targetPosition = targetPosition
            this.sourcePosition = targetPosition
        } else if (targetPosition != this.targetPosition) {
            this.sourcePosition = this.knobPosition
            this.animationStartedAt = this.currentTime
            this.targetPosition = targetPosition
            this.animationDuration = ((abs(this.targetPosition - this.sourcePosition) / this.dimensions.width.toDouble()) * MAX_ANIMATION_DURATION).roundToLong()
        }

        when {
            this.isDisabled -> {
                this.backgroundColor = this.theme.backgroundColorDark
                this.knobColor = this.theme.highlightColor
            }
            this.isActive -> {
                this.backgroundColor = this.theme.accentColor
                this.knobColor = this.theme.highlightColor
            }
            else -> {
                this.backgroundColor = this.theme.highlightColor
                this.knobColor = this.theme.accentColor
            }
        }
    }

    override fun onUpdateTime(currentTime: Long, delta: Long) {
        val knobPosition = animate(this.sourcePosition, this.targetPosition, this.animationDuration, this.animationStartedAt, currentTime, ::easeOutQuad).roundToInt()

        if (this.knobPosition != knobPosition) {
            this.dirty = true
            this.knobPosition = knobPosition
        }
    }

    override fun onRender(context: Graphics2D, bounds: Rectangle) {
        val image = BufferedImage(this.dimensions.width, this.dimensions.height, BufferedImage.TYPE_INT_ARGB)
        val imageGraphics = image.graphics as Graphics2D

        imageGraphics.color = this.backgroundColor
        imageGraphics.fillOval(0, 0, this.diameter, this.diameter)
        imageGraphics.fillOval(this.dimensions.width - this.diameter, 0, this.diameter, this.diameter)
        imageGraphics.fillRect(this.diameter / 2, 1, (this.dimensions.width - this.diameter), this.diameter - 1)

        imageGraphics.color = this.knobColor
        imageGraphics.fillOval(this.knobPosition, this.knobOffset, this.knobDiameter, this.knobDiameter)

        val minX = bounds.minX.toInt()
        val minY = bounds.minY.toInt()
        val maxX = bounds.maxX.toInt()
        val maxY = bounds.maxY.toInt()

        context.drawImage(image, minX, minY, maxX, maxY, minX, minY, maxX, maxY, null)
    }

    override fun onPropertyChanged(propertyName: String) {
        if (propertyName == this::isDisabled.name) {
            this.isActive = false
        }

        super.onPropertyChanged(propertyName)
    }

    override fun onMouseDown() {
        if (this.isDisabled) return
        this.isActive = !this.isActive
        super.onMouseDown()
    }

}
