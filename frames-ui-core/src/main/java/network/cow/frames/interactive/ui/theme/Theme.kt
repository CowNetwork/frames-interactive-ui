package network.cow.frames.interactive.ui.theme

import java.awt.Color

/**
 * @author Benedikt Wüller
 */
abstract class Theme {

    abstract val fontName: String
    abstract val fontStyle: Int

    abstract val textColorOnBackground: Color
    abstract val textColorOnAccent: Color
    abstract val textColorOnHighlight: Color

    abstract val textColorDisabled: Color
    abstract val textColorInfo: Color
    abstract val textColorSuccess: Color
    abstract val textColorWarning: Color
    abstract val textColorError: Color

    abstract val backgroundColor: Color
    abstract val backgroundColorDark: Color

    abstract val highlightColor: Color
    abstract val highlightColorDark: Color

    abstract val accentColor: Color
    abstract val accentColorDark: Color

}
