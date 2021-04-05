package network.cow.frames.interactive.ui.example

import network.cow.frames.color.ColorTransformer
import network.cow.frames.color.MinecraftColorPalette
import network.cow.frames.component.Component
import network.cow.frames.component.DummyComponent
import network.cow.frames.interactive.Input
import network.cow.frames.interactive.InteractiveFrame
import network.cow.frames.interactive.ui.component.Switch
import network.cow.frames.interactive.ui.component.TabView
import network.cow.frames.interactive.ui.component.Window
import network.cow.frames.interactive.ui.theme.CowTheme
import java.awt.Dimension
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class ExampleFrame(viewportDimension: Dimension, initialUpdateInterval: Long = 50L, transformer: ColorTransformer = MinecraftColorPalette()) :
    InteractiveFrame(Dimension(128, 128), viewportDimension, initialUpdateInterval, transformer) {

    private val cursorPosition = Point()
    private val window = Window(this.canvasDimensions, CowTheme())

    init {
        val tabView = TabView(
            Point(), Dimension(this.window.contentDimensions.width, this.window.contentDimensions.height),
            arrayOf(
                TabView.Tab("Network", Switch(Point(), Dimension(27, 17))), // switch: min height 17 - always odd is better
                TabView.Tab("Friends and Party", DummyComponent(Point(), Dimension(1, 1))),
                TabView.Tab("NSFW", DummyComponent(Point(), Dimension(1, 1)), true)
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
