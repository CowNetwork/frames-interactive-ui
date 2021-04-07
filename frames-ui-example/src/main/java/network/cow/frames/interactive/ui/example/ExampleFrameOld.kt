package network.cow.frames.interactive.ui.example

import network.cow.frames.alignment.HorizontalAlignment
import network.cow.frames.color.ColorTransformer
import network.cow.frames.color.MinecraftColorPalette
import network.cow.frames.component.DummyComponent
import network.cow.frames.interactive.Input
import network.cow.frames.interactive.InteractiveFrame
import network.cow.frames.interactive.ui.Dimensions
import network.cow.frames.interactive.ui._old.WindowManager
import network.cow.frames.interactive.ui._old.component.Button
import network.cow.frames.interactive.ui._old.component.Group
import network.cow.frames.interactive.ui._old.component.LabeledSelect
import network.cow.frames.interactive.ui._old.component.LabeledSwitch
import network.cow.frames.interactive.ui._old.component.TabView
import network.cow.frames.interactive.ui._old.component.Window
import network.cow.frames.interactive.ui.theme.CowTheme
import network.cow.frames.interactive.ui.theme.DefaultTheme
import java.awt.Dimension
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class ExampleFrameOld(viewportDimension: Dimension, initialUpdateInterval: Long = 50L, transformer: ColorTransformer = MinecraftColorPalette()) :
    InteractiveFrame(Dimension(256, 256), viewportDimension, initialUpdateInterval, transformer) {

    private val cursorPosition = Point()

    private val window = Window()
    private val otherWindow = Window(this.canvasDimensions)
    private val windowManager = WindowManager(this.canvasDimensions, this.window, CowTheme())

    init {
        val networkTab = Group(Point(), Dimensions.matchParent())

        val cockSwitch = LabeledSwitch(Point(), Dimensions.withParentWidth(21), "Enable my long coqq")
        cockSwitch.isDisabled = true
        cockSwitch.isActive = true

        val languageSelect = LabeledSelect(Point(0, 69), Dimensions.withParentWidth(21), "Language", arrayOf("English", "German"))

        val languageSwitch = LabeledSwitch(Point(0, 46), Dimensions.withParentWidth(21), "Use client language")
        languageSwitch.onToggle = { languageSelect.isDisabled = it }
        languageSwitch.isActive = true

        val themeSelect = LabeledSelect(Point(0, 92), Dimensions.withParentWidth(21), "UI Theme", arrayOf("Milk Me At Night", "Ugly Mode"))
        themeSelect.onSelect = {
            this.windowManager.theme = when (it) {
                "Milk Me At Night" -> CowTheme()
                else -> DefaultTheme()
            }
        }

        val windowButton = Button(Point(0, 120), Dimensions.withParentWidth(25), "Navigate to another window", HorizontalAlignment.CENTER)
        windowButton.onActivate = { this.windowManager.navigateTo(this.otherWindow) }

        networkTab.addComponent(cockSwitch)
        networkTab.addComponent(LabeledSwitch(Point(0, 23), Dimensions.withParentWidth(21), "Enable profanity filter"))
        networkTab.addComponent(languageSwitch)
        networkTab.addComponent(languageSelect)
        networkTab.addComponent(themeSelect)
        networkTab.addComponent(windowButton)

        val tabView = TabView(
            Point(), Dimensions.matchParent(),
            arrayOf(
                TabView.Tab("Network", networkTab), // switch: min height 17 - always odd is better
                TabView.Tab("Friends and Party", DummyComponent(Point(), Dimension(1, 1))),
//                TabView.Tab("NSFW", DummyComponent(Point(), Dimension(1, 1)), true)
            )
        )

        this.window.addComponent(tabView)
    }

    override fun onInputActivate(input: Input, currentTime: Long) = this.windowManager.activateInput(input)

    override fun onInputDeactivate(input: Input, currentTime: Long) = this.windowManager.deactivateInput(input)

    override fun getRenderComponent() = this.windowManager

    override fun onUpdate(currentTime: Long, delta: Long): Boolean {
        this.windowManager.update(currentTime, delta)
        return true
    }

    override fun onCursorMove(position: Point, currentTime: Long) {
        this.windowManager.updateCursor(this.cursorPosition, position)
        this.cursorPosition.location = position
    }

}
