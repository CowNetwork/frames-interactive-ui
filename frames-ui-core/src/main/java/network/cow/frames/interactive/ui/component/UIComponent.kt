package network.cow.frames.interactive.ui.component

import network.cow.frames.component.Component
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui.theme.DefaultTheme
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
abstract class UIComponent(position: Point = Point(), dimensions: Dimension = Dimensions.matchParent()) : Component(position, dimensions) {

    private var currentDimensions: Dimension by Delegates.observable(Dimension(dimensions)) { _, old, new -> this.onDimensionsChanged(old, new) }

    private val propertyChangeListeners = mutableMapOf<String, (Any?, Any?) -> Unit>()

    protected var currentTime: Long = 0; private set
    protected var cursorPosition: Point? = null

    internal var isVisible: Boolean by Delegates.observable(false) { _, old, new -> when {
        old == new -> return@observable
        new -> this.onInitialize()
        else -> this.onDestroy()
    } }

    var isHovered: Boolean by Delegates.observable(false) { property, old, new ->
        when {
            old == new -> return@observable
            new -> this.onMouseEnter()
            else -> {
                this.setMouseState(false)
                this.onMouseLeave()
            }
        }
        this.callListener(property, old, new)
    }; private set

    var isMouseDown: Boolean by Delegates.observable(false, { property, old, new ->
        when {
            old == new -> return@observable
            new -> this.onMouseDown()
            else -> this.onMouseUp()
        }
        this.callListener(property, old, new)
    }); private set

    var isDisabled: Boolean by Delegates.observable(false, ::observeProperty)

    var theme: Theme by Delegates.observable(DefaultTheme(), ::observeProperty)

    fun updateTime(currentTime: Long, delta: Long) {
        this.currentTime = currentTime

        if (this.currentDimensions != this.dimensions) {
            this.currentDimensions = Dimension(this.dimensions)
        }

        if (!this.isVisible) {
            this.isVisible = true
        }

        this.onUpdateTime(currentTime, delta)
    }

    fun <T> setListener(property: KProperty<T>, listener: ((T, T) -> Unit)?) {
        if (listener == null) {
            this.propertyChangeListeners.remove(property.name)
        } else {
            this.propertyChangeListeners[property.name] = listener as (Any?, Any?) -> Unit
        }
    }

    protected open fun <T> observeProperty(property: KProperty<T>, oldValue: T, newValue: T) {
        if (oldValue == newValue) return

        if (property.name == this::isDisabled.name && newValue == true) {
            this.updateCursorPosition(Point(-1, -1))
        }

        // Check if the property should trigger the callback.
        if (property.name in this.getObservedProperties().map { it.name }) {
            this.onPropertyChanged(property.name)
        }

        this.callListener(property, oldValue, newValue)
    }

    protected fun <T> callListener(property: KProperty<T>, oldValue: T, newValue: T) = this.propertyChangeListeners[property.name]?.let { it(oldValue, newValue) }

    open fun updateCursorPosition(position: Point) {
        val relativePosition = Point(position.x - this.position.x, position.y - this.position.y)
        this.isHovered = !this.isDisabled && relativePosition.x >= 0 && relativePosition.x < this.dimensions.width && relativePosition.y >= 0 && relativePosition.y < this.dimensions.height
        this.cursorPosition = if (this.isHovered) relativePosition else null
    }

    open fun setMouseState(isPressed: Boolean) {
        this.isMouseDown = if (this.isHovered) isPressed else false
    }

    protected fun update() = this.onUpdate()

    protected abstract fun onUpdate()

    protected open fun onInitialize() = this.update()

    protected open fun onDimensionsChanged(oldDimensions: Dimension, newDimensions: Dimension) = this.update()

    protected open fun onMouseEnter() = this.update()

    protected open fun onMouseLeave() = this.update()

    protected open fun onMouseDown() = this.update()

    protected open fun onMouseUp() = this.update()

    protected open fun onDestroy() = Unit

    protected abstract fun onUpdateTime(currentTime: Long, delta: Long)

    protected open fun onPropertyChanged(propertyName: String) = this.update()

    protected open fun getObservedProperties(): Array<KProperty<*>> = arrayOf(this::theme, this::isDisabled)

}
