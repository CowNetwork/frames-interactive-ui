package network.cow.frames.interactive.ui

import network.cow.frames.component.ColorComponent
import network.cow.frames.component.Component
import network.cow.frames.interactive.ui.component.Group
import network.cow.frames.interactive.ui.theme.DefaultTheme
import network.cow.frames.interactive.ui.theme.Theme
import java.awt.Dimension
import java.awt.Point
import java.util.Stack
import kotlin.properties.Delegates

/**
 * @author Benedikt Wüller
 */
class Window(dimensions: Dimension, initialTheme: Theme = DefaultTheme()) : Group(Point(), dimensions) {

    companion object {
        private const val NAVIGATION_HEIGHT_PERCENTAGE = 0.07
        private const val NAVIGATION_MIN_HEIGHT = 15
        private const val NAVIGATION_MAX_HEIGHT = 18
    }

    private val background = ColorComponent(Point(), Dimensions.matchParent(), this.theme.backgroundColor)
    private val contentView = Group(Point())

    private val views = Stack<Component>()

    private var hasParentView: Boolean by Delegates.observable(false) { _, old, new ->
        if (old == new) return@observable
//        if (old) {
//            this.add(this.backButton)
//        } else {
//            this.remove(this.backButton)
//        }
    }

    init {
        this.theme = initialTheme
        this.add(this.background)
        this.add(this.contentView)
    }

    fun setParentView() {
        if (!this.hasParentView) return
        this.setView(this.views[this.views.lastIndex - 1])
    }

    fun setView(component: Component) {
        if (this.views.isNotEmpty() && this.views.peek() == component) return

        if (this.views.contains(component)) {
            while (this.views.peek() != component) {
                this.views.pop()
            }
        }

        this.contentView.clear()
        this.views.push(component)
        this.contentView.add(component)

        this.hasParentView = this.views.size > 1
    }

    override fun onPropertyChanged(propertyName: String) {
        this.background.color = if (this.isDisabled) this.theme.backgroundColorDark else this.theme.backgroundColor
        super.onPropertyChanged(propertyName)
    }

}
