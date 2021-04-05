package network.cow.frames.interactive.ui.example

import network.cow.frames.color.ColorTransformer
import network.cow.frames.color.MinecraftColorPalette
import network.cow.frames.component.ColorComponent
import network.cow.frames.component.Component
import network.cow.frames.component.DummyComponent
import network.cow.frames.interactive.Input
import network.cow.frames.interactive.InteractiveFrame
import network.cow.frames.interactive.ui.component.TabView
import network.cow.frames.interactive.ui.component.Window
import network.cow.frames.interactive.ui.theme.CowTheme
import java.awt.Color
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
        val tabView = TabView(
            Point(), Dimension(this.window.contentDimensions.width, this.window.contentDimensions.height),
            arrayOf(
                TabView.Tab("Network", ColorComponent(Point(0, 100), Dimension(100, 50), Color.YELLOW)),
                TabView.Tab("Friends and Party", ColorComponent(Point(0, 100), Dimension(100, 50), Color.BLUE)),
                TabView.Tab("NSFW", DummyComponent(Point(), Dimension()), true)
            )
        )

        this.window.addComponent(tabView)

//        this.window.addComponent(ScrollingText(
//            Point(10, 10),
//            "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
//            Color.WHITE, Dimension(this.canvasDimensions.width - 20, 25), Font.SANS_SERIF, Font.BOLD
//        ))
//
//        val button = Button(Point(10, 40), Dimension(this.canvasDimensions.width - 20, 25), "This is a toggle button")
//        button.isActive = true
//        button.isToggle = true
//        this.window.addComponent(button)
//
//        val button2 = Button(Point(10, 75), Dimension(this.canvasDimensions.width - 20, 25), "This is a simple button with a really really really long label")
//        button2.isDisabled = true
//        this.window.addComponent(button2)

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
