package network.cow.frames.interactive.ui.component

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.component.Component
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui.Positions
import java.awt.Dimension
import java.awt.Point
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
class TabView(position: Point = Point(), dimensions: Dimension = Dimensions.matchParent()) : Group(position, dimensions) {

    companion object {
        private const val BUTTON_HEIGHT_PERCENTAGE = 0.1075
        private const val BUTTON_HORIZONTAL_MARGIN = 0.014
        private const val BUTTON_VERTICAL_MARGIN = 0.025
    }

    var selectedIndex: Int by Delegates.observable(-1, ::observeProperty)

    var tabs: Array<Tab> by Delegates.observable(emptyArray(), ::observeProperty)

    private var buttons: Array<Button> = emptyArray()

    private var buttonView = Group(dimensions = Dimension(Dimensions.MATCH_PARENT, Dimensions.matchParentPercent(BUTTON_HEIGHT_PERCENTAGE)))

    private var contentView = Group(
        Point(0, Positions.matchParentPercent(BUTTON_HEIGHT_PERCENTAGE + BUTTON_VERTICAL_MARGIN)),
        Dimension(Dimensions.MATCH_PARENT, Dimensions.matchParentPercent(1.0 - BUTTON_HEIGHT_PERCENTAGE - BUTTON_VERTICAL_MARGIN))
    )

    init {
        this.add(this.buttonView)
        this.add(this.contentView)
    }

    override fun onInitialize() {
        super.onInitialize()
        if (this.selectedIndex < 0) {
            this.selectedIndex = this.getFirstEnabledTabIndex()
        }
    }

    private fun onTabUpdated(tab: Tab) {
        val index = this.tabs.indexOf(tab)
        val button = this.buttons[index]
        button.isDisabled = tab.isDisabled
        button.content = tab.label
    }

    private fun getFirstEnabledTabIndex() = this.tabs.firstOrNull { !it.isDisabled }?.let { this.tabs.indexOf(it) } ?: -1

    override fun onPropertyChanged(propertyName: String) {
        if (propertyName == this::tabs.name) {
            this.contentView.clear()
            this.buttonView.clear()
            this.buttons = Array(this.tabs.size) { index ->
                val tab = this.tabs[index]

                val buttonWidth = (1.0 - BUTTON_HORIZONTAL_MARGIN * (this.tabs.size - 1)) / this.tabs.size

                val button = Button(
                    Point(Positions.matchParentPercent(buttonWidth * index + BUTTON_HORIZONTAL_MARGIN * index), 0),
                    Dimension(Dimensions.matchParentPercent(buttonWidth), Dimensions.MATCH_PARENT),
                    tab.label, HorizontalAlignment.CENTER
                )
                button.isToggle = true
                button.isDisabled = tab.isDisabled

                button.setListener(button::isActive) { _, isActive ->
                    if (isActive) {
                        this.selectedIndex = index
                    } else if (this.selectedIndex == index) {
                        button.isActive = true
                    }
                }

                return@Array button
            }
            this.buttons.forEach(this.buttonView::add)
        }

        if (propertyName == this::selectedIndex.name && this.tabs.isNotEmpty()) {
            val index = minOf(maxOf(0, this.selectedIndex), this.tabs.lastIndex)
            val tab = this.tabs[index]
            this.contentView.clear()
            this.contentView.add(tab.content)
            this.buttons.forEachIndexed { i, it -> it.isActive = i == index }
        }

        super.onPropertyChanged(propertyName)
    }

    override fun getObservedProperties(): Array<KProperty<*>> = arrayOf(*super.getObservedProperties(), this::selectedIndex, this::tabs)

    inner class Tab(label: String, content: Component, disabled: Boolean = false) {
        var label: String by Delegates.observable(label, ::onUpdate)
        var content: Component by Delegates.observable(content, ::onUpdate)
        var isDisabled: Boolean by Delegates.observable(disabled, ::onUpdate)

        private fun onUpdate(ignored: KProperty<*>, oldValue: Any, newValue: Any) {
            if (oldValue == newValue) return
            this@TabView.onTabUpdated(this)
        }
    }

}
