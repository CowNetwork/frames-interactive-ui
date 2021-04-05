package network.cow.frames.interactive.ui.theme

import java.awt.Color
import java.awt.Font

/**
 * @author Benedikt Wüller
 */
class DummyTheme : Theme() {

    override val fontName: String = Font.SANS_SERIF
    override val fontStyle: Int = Font.PLAIN

    override val textColorOnBackground: Color = Color.BLACK
    override val textColorOnAccent: Color = Color.BLACK
    override val textColorOnHighlight: Color = Color.BLACK

    override val textColorDisabled: Color = Color.LIGHT_GRAY
    override val textColorInfo: Color = Color.BLUE
    override val textColorSuccess: Color = Color.GREEN
    override val textColorWarning: Color = Color.ORANGE
    override val textColorError: Color = Color.RED

    override val backgroundColor: Color = Color.WHITE
    override val backgroundColorDark: Color = Color.LIGHT_GRAY

    override val highlightColor: Color = Color.GRAY
    override val highlightColorDark: Color = Color.DARK_GRAY

    override val accentColor: Color = Color.PINK
    override val accentColorDark: Color = Color.PINK

}