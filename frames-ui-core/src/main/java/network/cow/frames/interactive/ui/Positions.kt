package network.cow.frames.interactive.ui

/**
 * @author Benedikt Wüller
 */
object Positions {

    fun matchPercent(factor: Double) = Dimensions.matchPercent(factor)

    fun isInPercentRange(value: Int) = Dimensions.isInPercentRange(value)

    fun getFactor(value: Int) = Dimensions.getFactor(value)

}
