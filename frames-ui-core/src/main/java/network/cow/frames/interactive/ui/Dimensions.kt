package network.cow.frames.interactive.ui

import java.awt.Dimension

/**
 * @author Benedikt Wüller
 */
object Dimensions {

    val MATCH_PARENT = -1

    val MATCH_PARENT_DIMENSIONS = Dimension(MATCH_PARENT, MATCH_PARENT)

    fun matchParentWidth(height: Int) = Dimension(MATCH_PARENT, height)

    fun matchParentHeight(width: Int) = Dimension(width, MATCH_PARENT)

}
