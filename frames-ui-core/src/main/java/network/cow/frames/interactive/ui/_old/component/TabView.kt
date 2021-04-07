package network.cow.frames.interactive.ui._old.component

import network.cow.frames.component.Component
import java.awt.Dimension
import java.awt.Point
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class TabView(position: Point, dimensions: Dimension, private val tabs: Array<Tab>) : Group(position, dimensions) {

    companion object {
        private const val PADDING_PERCENTAGE = 0.02

        private const val BUTTON_HEIGHT_PERCENTAGE = 0.1035
        private const val BUTTON_MIN_HEIGHT = 14
    }

    private val tabCompoundView = Group(Point(), Dimension())

    private val contentView = Group(Point(), Dimension())

    private lateinit var buttons: Array<Button>

    private var currentTab: Int = -1

    override fun onShow() {
        this.clear()

        this.addComponent(tabCompoundView)
        this.addComponent(contentView)

        val padding = (this.dimensions.width * PADDING_PERCENTAGE).roundToInt()
        val buttonWidth = (this.dimensions.width - (this.tabs.size - 1) * padding) / this.tabs.size
        val buttonHeight = maxOf((this.dimensions.width * BUTTON_HEIGHT_PERCENTAGE).roundToInt(), BUTTON_MIN_HEIGHT)

        this.tabCompoundView.dimensions.setSize(this.dimensions.width, buttonHeight)

        this.contentView.position.location = Point(0, buttonHeight + padding)
        this.contentView.dimensions.setSize(this.dimensions.width, this.dimensions.height - buttonHeight - padding)

        val buttons = mutableListOf<Button>()
        this.tabs.forEachIndexed { index, tab ->
            val button = Button(
                Point(index * (buttonWidth + padding), 0),
                Dimension(buttonWidth, buttonHeight),
                tab.label
            )

            button.textComponent.delayPerPixel = 40.0
            button.isToggle = true
            button.isDisabled = tab.isDisabled

            button.onActivate = {
                if (this.currentTab != index) this.setTab(index)
            }

            button.onDeactivate = {
                if (this.currentTab == index) button.isActive = true
            }

            tab.onUpdateContent = { old, new ->
                this.contentView.removeComponent(old)
                if (this.currentTab == index) {
                    this.contentView.addComponent(new)
                }
            }

            tab.onUpdateLabel = { _, new -> button.textComponent.text = new }

            tab.onUpdateDisabled = { old, new ->
                if (old && !new) {
                    val nextIndex = if (index + 1 >= this.tabs.size) 0 else index + 1
                    this.setTab(nextIndex)
                    button.isDisabled = true
                } else {
                    button.isDisabled = new
                }
            }

            this.tabCompoundView.addComponent(button)
            buttons.add(button)
        }

        this.buttons = buttons.toTypedArray()

        this.setTab(0)
    }

    private fun setTab(index: Int) {
        if (this.currentTab == index) return

        val lastButton = this.currentTab
        this.currentTab = index

        if (lastButton >= 0) {
            this.buttons[lastButton].isActive = false
            this.contentView.removeComponent(this.tabs[lastButton].content)
        }

        this.buttons[index].isActive = true
        this.contentView.addComponent(this.tabs[index].content)
    }

    class Tab(label: String, content: Component, disabled: Boolean = false) {

        var label = label
            set(value) {
                if (field == value) return
                this.onUpdateLabel(field, value)
                field = value
            }

        var content = content
            set(value) {
                if (field == value) return
                this.onUpdateContent(field, value)
                field = value
            }

        var isDisabled = disabled
            set(value) {
                if (field == value) return
                this.onUpdateDisabled(field, value)
                field = value
            }

        internal lateinit var onUpdateDisabled: (Boolean, Boolean) -> Unit
        internal lateinit var onUpdateLabel: (String, String) -> Unit
        internal lateinit var onUpdateContent: (Component, Component) -> Unit
    }

}
