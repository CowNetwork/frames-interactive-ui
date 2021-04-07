package network.cow.frames.interactive.ui.theme

import java.awt.Color
import java.awt.Font

/**
 * @author Benedikt Wüller
 */
class DefaultTheme : Theme() {

    override val fontName: String = Font.SANS_SERIF
    override val fontStyle: Int = Font.PLAIN

    override val textColor: Color = Color.BLACK
    override val textColorDisabled: Color = Color.LIGHT_GRAY
    override val textColorAccent: Color = Color.CYAN

    override val textColorOnAccent: Color = Color.BLACK
    override val textColorOnHighlight: Color = Color.BLACK

    override val backgroundColor: Color = Color.WHITE
    override val backgroundColorDark: Color = Color.LIGHT_GRAY

    override val highlightColor: Color = Color.GRAY.brighter()
    override val highlightColorDark: Color = Color.GRAY

    override val accentColor: Color = Color.CYAN.darker()
    override val accentColorDark: Color = Color.CYAN.darker().darker()

}
