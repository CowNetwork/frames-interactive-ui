package network.cow.frames.interactive.ui

import java.awt.Dimension
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
object Dimensions {

    private const val MATCH_PARENT_PERCENT_MIN = -1000
    private const val MATCH_PARENT_PERCENT_MAX = -10999 // inclusive, leaves 10_000 magic values (0.0000 to 1.0000).

    const val MATCH_PARENT = -1

    const val MATCH_PARENT_MIN = -2
    const val MATCH_PARENT_MAX = -3

    const val MATCH_THIS_WIDTH = -4
    const val MATCH_THIS_HEIGHT = -5

    fun matchParentPercent(factor: Double): Int {
        val factor = maxOf(0.0, minOf(factor, 1.0))
        val difference = MATCH_PARENT_PERCENT_MAX - MATCH_PARENT_PERCENT_MIN
        return (difference * factor).roundToInt() + MATCH_PARENT_PERCENT_MIN
    }

    fun isInPercentRange(value: Int) = value in MATCH_PARENT_PERCENT_MIN downTo MATCH_PARENT_PERCENT_MAX

    fun getFactor(value: Int): Double {
        val difference = MATCH_PARENT_PERCENT_MAX - MATCH_PARENT_PERCENT_MIN
        val factor = (value - MATCH_PARENT_PERCENT_MIN) / difference.toDouble()
        return maxOf(0.0, minOf(factor, 1.0))
    }

    fun matchParent() = Dimension(MATCH_PARENT, MATCH_PARENT)

    fun withParentWidth(height: Int) = Dimension(MATCH_PARENT, height)

    fun withParentHeight(width: Int) = Dimension(width, MATCH_PARENT)

}
