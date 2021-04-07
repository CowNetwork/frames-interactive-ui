package network.cow.frames.interactive.ui.theme

import java.awt.Color
import java.awt.Font

/**
 * @author Benedikt Wüller
 */
class CowTheme : Theme() {

    override val fontName: String = Font.SANS_SERIF
    override val fontStyle: Int = Font.PLAIN

    override val textColor: Color = Color(240, 240, 240)
    override val textColorDisabled: Color = Color(90, 90, 90)
    override val textColorAccent: Color = Color(248, 151, 217)

    override val textColorOnAccent: Color = Color(42, 42, 42)
    override val textColorOnHighlight: Color = Color(248, 151, 217)

    override val backgroundColor: Color = Color(47, 47, 47)
    override val backgroundColorDark: Color = Color(38, 38, 38)

    override val highlightColor: Color = Color(80, 80, 80)
    override val highlightColorDark: Color = Color(	63, 63, 63)

    override val accentColor: Color = Color(248, 151, 217)
    override val accentColorDark: Color = Color(165, 101, 143)

}
