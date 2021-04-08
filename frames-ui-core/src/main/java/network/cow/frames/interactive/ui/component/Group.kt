package network.cow.frames.interactive.ui.component

import network.cow.frames.component.Component
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui.Positions
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
open class Group(position: Point = Point(), dimensions: Dimension = Dimensions.matchParent()) : UIComponent(position, dimensions) {

    private val initialDimensions = mutableMapOf<Component, Dimension>()
    private val initialPositions = mutableMapOf<Component, Point>()

    protected open val newComponents = mutableListOf<Component>()
    protected open val components = mutableListOf<Component>()
    protected open val removedComponents = mutableListOf<Component>()

    override var dirty: Boolean
        set(value) = this.components.forEach { it.dirty = value }
        get() = this.components.any { it.dirty }

    open fun add(component: Component) {
        if (component in this.newComponents || component in this.components) return
        this.removedComponents.remove(component)
        this.newComponents.add(component)
        component.dirty = true
    }

    open fun remove(component: Component) {
        if (component !in this.newComponents && component !in this.components) return
        this.newComponents.remove(component)
        this.removedComponents.add(component)
        component.dirty = true
    }

    fun clear() = this.components.forEach(this::remove)

    override fun onUpdateTime(currentTime: Long, delta: Long) {
        this.newComponents.forEach {
            this.dirty = true
            this.components.add(it)

            this.initialDimensions[it] = Dimension(it.dimensions)
            this.initialPositions[it] = Point(it.position)
            this.resize(it)

            if (it !is UIComponent) return@forEach
            it.isVisible = true
            it.theme = this.theme
        }

        this.newComponents.clear()

        this.components.forEach {
            if (it !is UIComponent) return@forEach
            it.updateTime(currentTime, delta)
        }
    }

    private fun resize(component: Component) {
        val dimensions = this.initialDimensions[component] ?: return
        val position = this.initialPositions[component] ?: return

        if (Positions.isInPercentRange(position.x)) {
            component.position.x = ceil(this.dimensions.width * Positions.getFactor(position.x)).toInt()
        }

        if (Positions.isInPercentRange(position.y)) {
            component.position.y = ceil(this.dimensions.height * Positions.getFactor(position.y)).toInt()
        }

        if (Dimensions.isInPercentRange(dimensions.width)) {
            component.dimensions.width = ceil(this.dimensions.width * Dimensions.getFactor(dimensions.width)).toInt()
        } else if (dimensions.width == Dimensions.MATCH_PARENT) {
            component.dimensions.width = this.dimensions.width
        } else if (dimensions.width == Dimensions.MATCH_PARENT_MIN) {
            component.dimensions.width = minOf(this.dimensions.width, this.dimensions.height)
        } else if (dimensions.width == Dimensions.MATCH_PARENT_MAX) {
            component.dimensions.width = maxOf(this.dimensions.width, this.dimensions.height)
        }

        if (Dimensions.isInPercentRange(dimensions.height)) {
            component.dimensions.height = ceil(this.dimensions.height * Dimensions.getFactor(dimensions.height)).toInt()
        } else if (dimensions.height == Dimensions.MATCH_PARENT) {
            component.dimensions.height = this.dimensions.height
        } else if (dimensions.height == Dimensions.MATCH_PARENT_MIN) {
            component.dimensions.height = minOf(this.dimensions.width, this.dimensions.height)
        } else if (dimensions.height == Dimensions.MATCH_PARENT_MAX) {
            component.dimensions.height = maxOf(this.dimensions.width, this.dimensions.height)
        }

        if (dimensions.width == Dimensions.MATCH_THIS_HEIGHT) {
            component.dimensions.width = component.dimensions.height
        }

        if (dimensions.height == Dimensions.MATCH_THIS_WIDTH) {
            component.dimensions.height = component.dimensions.width
        }
    }

    override fun onDimensionsChanged(oldDimensions: Dimension, newDimensions: Dimension) {
        super.onDimensionsChanged(oldDimensions, newDimensions)
        this.components.forEach(this::resize)
    }

    override fun onRender(context: Graphics2D, bounds: Rectangle) {
        this.components
            .filterNot { this.removedComponents.contains(it) }
            .forEach { it.render(context.create() as Graphics2D, bounds) }
    }

    override fun updateCursorPosition(position: Point) {
        super.updateCursorPosition(position)
        val relativePosition = this.cursorPosition ?: Point(-1, -1)
        this.components.forEach {
            if (it !is UIComponent) return@forEach
            it.updateCursorPosition(relativePosition)
        }
    }

    override fun setMouseState(isPressed: Boolean) {
        super.setMouseState(isPressed)
        this.components.forEach {
            if (it !is UIComponent) return@forEach
            it.setMouseState(isPressed)
        }
    }

    override fun calculateSectionsToRender(dry: Boolean): List<Rectangle> {
        if (!this.dirty) return emptyList()
        if (components.isEmpty()) return emptyList()

        val sections = this.components
            .map { it.calculateSectionsToRender(dry) }
            .reduce { a, b -> a.union(b).toList() }
            .map { Rectangle(it.x + this.position.x, it.y + this.position.y, it.width, it.height) }

        if (!dry) {
            this.removedComponents.forEach {
                this.components.remove(it)
                this.initialDimensions.remove(it)
                this.initialPositions.remove(it)

                if (it !is UIComponent) return@forEach
                it.updateCursorPosition(Point(-1, -1))
                it.setMouseState(false)
                it.isVisible = false
            }
            this.removedComponents.clear()
        }

        return sections
    }

    override fun resetPreviousBounds() = this.components.forEach { it.resetPreviousBounds() }

    override fun onPropertyChanged(propertyName: String) {
        when (propertyName) {
            this::theme.name -> this.components.forEach {
                if (it !is UIComponent) return@forEach
                it.theme = this.theme
            }
            this::isDisabled.name -> this.components.forEach {
                if (it !is UIComponent) return@forEach
                it.isDisabled = this.isDisabled
            }
        }
        super.onPropertyChanged(propertyName)
    }

    override fun onUpdate() = Unit

    override fun onInitialize() {
        super.onInitialize()

        this.components.forEach {
            if (it !is UIComponent) return@forEach
            it.isVisible = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        this.components.forEach {
            if (it !is UIComponent) return@forEach
            it.isVisible = false
        }
    }

}
