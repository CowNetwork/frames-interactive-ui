package network.cow.frames.interactive.ui.example

import network.cow.frames.color.ColorTransformer
import network.cow.frames.color.MinecraftColorPalette
import network.cow.frames.component.Component
import network.cow.frames.component.DummyComponent
import network.cow.frames.interactive.Input
import network.cow.frames.interactive.InteractiveFrame
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui.component.Group
import network.cow.frames.interactive.ui.component.LabeledSelect
import network.cow.frames.interactive.ui.component.LabeledSwitch
import network.cow.frames.interactive.ui.component.Select
import network.cow.frames.interactive.ui.component.TabView
import network.cow.frames.interactive.ui.component.Window
import network.cow.frames.interactive.ui.theme.CowTheme
import java.awt.Dimension
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class ExampleFrame(viewportDimension: Dimension, initialUpdateInterval: Long = 50L, transformer: ColorTransformer = MinecraftColorPalette()) :
    InteractiveFrame(Dimension(256, 256), viewportDimension, initialUpdateInterval, transformer) {

    private val cursorPosition = Point()
    private val window = Window(this.canvasDimensions, CowTheme())

    init {
        val group = Group(Point(), this.window.contentDimensions)

        val cockSwitch = LabeledSwitch(Point(), Dimensions.matchParentWidth(19), "Enable my long coqq")
        cockSwitch.isDisabled = true
        cockSwitch.isActive = true

        val languageSelect = LabeledSelect(Point(0, 60), Dimensions.matchParentWidth(19), "Language", arrayOf("English", "German"))

        val languageSwitch = LabeledSwitch(Point(0, 40), Dimensions.matchParentWidth(19), "Use client language")
        languageSwitch.onToggle = { languageSelect.isDisabled = it }
        languageSwitch.isActive = true

        group.addComponent(cockSwitch)
        group.addComponent(LabeledSwitch(Point(0, 20), Dimensions.matchParentWidth(19), "Enable profanity filter"))
        group.addComponent(languageSwitch)
        group.addComponent(languageSelect)

        val tabView = TabView(
            Point(), Dimension(this.window.contentDimensions.width, this.window.contentDimensions.height),
            arrayOf(
                TabView.Tab("Network", group), // switch: min height 17 - always odd is better
                TabView.Tab("Friends and Party", DummyComponent(Point(), Dimension(1, 1))),
//                TabView.Tab("NSFW", DummyComponent(Point(), Dimension(1, 1)), true)
            )
        )

        this.window.addComponent(tabView)
    }

    override fun onInputActivate(input: Input, currentTime: Long) {
        this.window.activateInput(input)
    }

    override fun onInputDeactivate(input: Input, currentTime: Long) {
        this.window.deactivateInput(input)
    }

    override fun getRenderComponent(): Component = this.window

    override fun onUpdate(currentTime: Long, delta: Long): Boolean {
        this.window.update(currentTime, delta)
        return true
    }

    override fun onCursorMove(position: Point, currentTime: Long) {
        this.window.updateCursor(this.cursorPosition, position)
        this.cursorPosition.location = position
    }

}
