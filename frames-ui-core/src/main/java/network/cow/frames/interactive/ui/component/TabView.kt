package network.cow.frames.interactive.ui.component

import network.cow.frames.component.ColorComponent
import network.cow.frames.component.Component
import network.cow.frames.interactive.ui.theme.Theme
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

    private val tabCompoundView: Group

    private val contentViewBackground: ColorComponent
    private val contentView: Group

    private val buttons: Array<Button>

    private var currentTab: Int = -1

    init {
        val padding = (this.dimensions.width * PADDING_PERCENTAGE).roundToInt()
        val buttonWidth = (this.dimensions.width - (this.tabs.size - 1) * padding) / this.tabs.size
        val buttonHeight = maxOf((this.dimensions.width * BUTTON_HEIGHT_PERCENTAGE).roundToInt(), BUTTON_MIN_HEIGHT)

        this.tabCompoundView = Group(Point(0, 0), Dimension(this.dimensions.width, buttonHeight))

        this.contentView = Group(Point(0, buttonHeight + padding), Dimension(this.dimensions.width, this.dimensions.height - buttonHeight - padding))
        this.contentViewBackground = ColorComponent(Point(0, 0), this.contentView.dimensions, this.theme.backgroundColorDark)
        this.contentView.addComponent(this.contentViewBackground)

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
                this.contentView.addComponent(tab.content)
            }

            button.onDeactivate = {
                if (this.currentTab == index) {
                    button.isActive = true
                } else {
                    this.contentView.removeComponent(tab.content)
                }
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

        this.addComponent(tabCompoundView)
        this.addComponent(contentView)

        this.setTab(0)
    }

    private fun setTab(index: Int) {
        if (this.currentTab == index) return

        val lastButton = this.currentTab
        this.currentTab = index

        if (lastButton >= 0) {
            this.buttons[lastButton].isActive = false
        }

        this.buttons[index].isActive = true
    }

    override fun onUpdateTheme(theme: Theme) {
        this.contentViewBackground.color = this.theme.backgroundColorDark
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
