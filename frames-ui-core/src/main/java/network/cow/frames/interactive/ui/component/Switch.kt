package network.cow.frames.interactive.ui.component

import network.cow.frames.interactive.Input
import network.cow.frames.interactive.ui.animate
import network.cow.frames.interactive.ui.easeOutQuad
import network.cow.frames.interactive.ui.theme.Theme
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

/**
 * @author Benedikt Wüller
 */
class Switch(position: Point, dimensions: Dimension) : UIComponent(position, dimensions) {

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

    var isDisabled = false
        set(value) {
            if (field == value) return
            field = value
            this.update()
        }

    var isActive = false
        set(value) {
            if (field == value) return
            field = value
            this.onToggle?.let { it(value) }
            this.update()
        }

    var onToggle: ((Boolean) -> Unit)? = null

    init {
        this.update()
    }

    private fun update() {
        this.dirty = true

        this.diameter = this.dimensions.height
        this.knobDiameter = ceil(diameter * INNER_RADIUS_PERCENTAGE).toInt()
        this.knobOffset = (this.diameter - this.knobDiameter) / 2

        val hoverOffset = if (!this.isDisabled && this.isHovered()) floor(this.dimensions.width * HOVER_OFFSET_PERCENTAGE).toInt() else 0

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
            this.animationStartedAt = this.getCompanion().currentTime
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

    override fun update(currentTime: Long, delta: Long) {
        super.update(currentTime, delta)

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

    override fun onInputActivate(input: Input) {
        if (input != Input.INTERACT_PRIMARY) return
        if (this.isDisabled) return
        this.isActive = !this.isActive
    }

    override fun onMouseEnter(position: Point, relativePosition: Point)  = this.update()

    override fun onMouseLeave(position: Point, relativePosition: Point)  = this.update()

    override fun onUpdateTheme(theme: Theme) {
        this.update()
    }

}
