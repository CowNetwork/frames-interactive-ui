package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.component.ColorComponent
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui.animate
import network.cow.frames.interactive.ui.easeOutQuad
import java.awt.Dimension
import java.awt.Point
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
class ProgressBar(position: Point, dimensions: Dimension = Dimensions.matchParent()) : Group(position, dimensions) {

    companion object {
        private const val MAX_ANIMATION_DURATION = 1500L
    }

    var progress: Double by Delegates.observable(0.0, ::observeProperty)

    var isLabelVisible: Boolean by Delegates.observable(false, ::observeProperty)

    private var lastProgress: Double = 0.0
    private var animationStartProgress: Double = 0.0
    private var animationStartedAt: Long = 0
    private var animationDuration: Long = 0

    private val background = ColorComponent(Point(), Dimensions.matchParent(), this.theme.backgroundColorDark)
    private val bar = ColorComponent(Point(), Dimensions.withParentHeight(0), this.theme.accentColor)
    private val label = Text(Point(), Dimensions.matchParent(), "0 %", this.theme.textColorOnAccent, HorizontalAlignment.CENTER)

    init {
        this.add(this.background)
        this.add(this.bar)
        this.add(this.label)
    }

    override fun onUpdateTime(currentTime: Long, delta: Long) {
        val progress = animate(this.animationStartProgress, this.progress, this.animationDuration, this.animationStartedAt, currentTime, ::easeOutQuad)

        this.bar.dimensions.width = (progress * this.dimensions.width).roundToInt()
        this.label.content = "${(progress * 100).roundToInt()} %"

        this.label.color = if (progress <= 0.5) this.theme.textColorOnHighlight else this.theme.textColorOnAccent

        super.onUpdateTime(currentTime, delta)
    }

    override fun onUpdate() {
        this.background.color = this.theme.backgroundColorDark
        this.bar.color = this.theme.accentColor
        super.onUpdate()
    }

    override fun onPropertyChanged(propertyName: String) {
        if (propertyName == this::progress.name) {
            this.animationStartedAt = this.currentTime
            this.animationStartProgress = this.lastProgress
            this.animationDuration = ((this.progress - this.animationStartProgress) * MAX_ANIMATION_DURATION).roundToLong()
            this.lastProgress = this.progress
        }

        if (propertyName == this::isLabelVisible.name) {
            if (this.isLabelVisible) {
                this.add(this.label)
            } else {
                this.remove(this.label)
            }
        }

        super.onPropertyChanged(propertyName)
    }

    override fun getObservedProperties(): Array<KProperty<*>> = arrayOf(*super.getObservedProperties(), this::progress, this::isLabelVisible)

}
