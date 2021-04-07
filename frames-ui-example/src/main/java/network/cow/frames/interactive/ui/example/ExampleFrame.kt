package network.cow.frames.interactive.ui.example

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.color.ColorTransformer
import network.cow.frames.color.MinecraftColorPalette
import network.cow.frames.component.Component
import network.cow.frames.component.DummyComponent
import network.cow.frames.interactive.Input
import network.cow.frames.interactive.InteractiveFrame
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui.Positions
import network.cow.frames.interactive.ui.Window
import network.cow.frames.interactive.ui.component.Button
import network.cow.frames.interactive.ui.component.Group
import network.cow.frames.interactive.ui.component.ProgressBar
import network.cow.frames.interactive.ui.component.Select
import network.cow.frames.interactive.ui.component.Switch
import network.cow.frames.interactive.ui.component.TabView
import network.cow.frames.interactive.ui.component.TextButton
import network.cow.frames.interactive.ui.theme.CowTheme
import network.cow.frames.interactive.ui.theme.DefaultTheme
import java.awt.Dimension
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class ExampleFrame(canvasDimensions: Dimension, initialUpdateInterval: Long = 50L, transformer: ColorTransformer = MinecraftColorPalette()) :
    InteractiveFrame(canvasDimensions, canvasDimensions, initialUpdateInterval, transformer) {

    private val window = Window(this.canvasDimensions, CowTheme())

    private val progressBar = ProgressBar(Point(Positions.matchPercent(0.25), 125), Dimension(Dimensions.matchPercent(0.5), 22))

    init {
        var count = 0

        val group = Group()
        val otherGroup = Group()

        val select = Select(Point(Positions.matchPercent(0.25), 30), Dimension(Dimensions.matchPercent(0.5), 25))
        select.options = arrayOf("Milk Me At Night", "Ugly Mode")
        select.setListener(select::selectedIndex) { _, new -> this.window.theme = if (new == 0) CowTheme() else DefaultTheme() }

        val tabView = TabView()
        tabView.tabs = arrayOf(
            tabView.Tab("NSFW", DummyComponent(Point(), Dimensions.matchParent()), true),
            tabView.Tab("Theme", select),
            tabView.Tab("Demo", group),
        )

        val backButton = TextButton(Point(Positions.matchPercent(0.25), 30), Dimension(Dimensions.matchPercent(0.5), 25), "Go back", HorizontalAlignment.CENTER)
        backButton.setListener(backButton::isMouseDown) { _, new -> if (new) this.window.setParentView() }
        otherGroup.add(backButton)

        val button = Button(Point(Positions.matchPercent(0.25), 30), Dimension(Dimensions.matchPercent(0.5), 25), "$count", HorizontalAlignment.CENTER)
        button.setListener(button::isActive) { _, new -> if (new) button.content = "${++count}" }
        group.add(button)

        val switch = Switch(Point(Positions.matchPercent(0.25), 60), Dimension(Dimensions.matchPercent(0.5), 25))
        switch.setListener(switch::isActive) { _, new -> button.isDisabled = new }
        switch.isActive = true
        group.add(switch)

        val textButton = TextButton(Point(Positions.matchPercent(0.25), 90), Dimension(Dimensions.matchPercent(0.5), 25), "Text Button", HorizontalAlignment.CENTER)
        textButton.setListener(textButton::isMouseDown) { _, new -> if (new) this.window.setView(otherGroup) }
        group.add(textButton)

        this.progressBar.isLabelVisible = true
        group.add(this.progressBar)

        this.window.setView(tabView)
    }

    override fun getRenderComponent(): Component = this.window

    override fun onUpdate(currentTime: Long, delta: Long): Boolean {
        when {
            currentTime > 25000 -> this.progressBar.progress = 1.0
            currentTime > 18000 -> this.progressBar.progress = 0.99
            currentTime > 13000 -> this.progressBar.progress = 0.98
            currentTime > 10000 -> this.progressBar.progress = 0.97
            currentTime > 5000 -> this.progressBar.progress = 0.88
        }

        this.window.updateTime(currentTime, delta)
        return true
    }

    override fun onCursorMove(position: Point, currentTime: Long) {
        super.onCursorMove(position, currentTime)
        this.window.updateCursorPosition(position)
    }

    override fun onInputActivate(input: Input, currentTime: Long) {
        super.onInputActivate(input, currentTime)
        if (input == Input.INTERACT_PRIMARY) {
            this.window.setMouseState(true)
        }
    }

    override fun onInputDeactivate(input: Input, currentTime: Long) {
        super.onInputActivate(input, currentTime)
        if (input == Input.INTERACT_PRIMARY) {
            this.window.setMouseState(false)
        }
    }

}
