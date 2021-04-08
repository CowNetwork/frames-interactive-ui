package network.cow.frames.interactive.ui

/**
 * @author Benedikt Wüller
 */
object Positions {

    fun matchParentPercent(factor: Double) = Dimensions.matchParentPercent(factor)

    fun isInPercentRange(value: Int) = Dimensions.isInPercentRange(value)

    fun getFactor(value: Int) = Dimensions.getFactor(value)

}
