package network.cow.frames.interactive.ui.theme

import java.awt.Color
import java.awt.Font

/**
 * @author Benedikt Wüller
 */
class CowTheme : Theme() {

    override val fontName: String = Font.SANS_SERIF
    override val fontStyle: Int = Font.BOLD

    override val textColorOnBackground: Color = Color(240, 240, 240)
    override val textColorOnAccent: Color = Color(42, 42, 42)
    override val textColorOnHighlight: Color = Color(248, 151, 217)

    override val textColorDisabled: Color = Color(90, 90, 90)
    override val textColorInfo: Color = Color(23, 162, 184)
    override val textColorSuccess: Color = Color(40, 167, 69)
    override val textColorWarning: Color = Color(255, 193, 7)
    override val textColorError: Color = Color(220, 53, 69)

    override val backgroundColor: Color = Color(47, 47, 47)
    override val backgroundColorDark: Color = Color(38, 38, 38)

    override val highlightColor: Color = Color(75, 75, 75)
    override val highlightColorDark: Color = Color(	60, 60, 60)

    override val accentColor: Color = Color(248, 151, 217)
    override val accentColorDark: Color = Color(165, 101, 143)

}
