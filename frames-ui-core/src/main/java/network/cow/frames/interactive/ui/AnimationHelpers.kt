package network.cow.frames.interactive.ui

import kotlin.math.pow

/**
 * @author Benedikt Wüller
 */

fun animate(from: Number, to: Number, duration: Long, startedAt: Long, currentTime: Long, animator: (Double) -> Double = ::linear): Double {
    if (duration <= 0) return to.toDouble()
    val delta = maxOf(minOf((currentTime - startedAt) / duration.toDouble(), 1.0), 0.0)
    val difference = to.toDouble() - from.toDouble()
    return animator(delta) * difference + from.toDouble()
}

fun linear(delta: Double) = delta

fun easeInQuad(delta: Double) = easeIn(delta, 2)
fun easeInCubic(delta: Double) = easeIn(delta, 3)
fun easeInQuart(delta: Double) = easeIn(delta, 4)
fun easeInQuint(delta: Double) = easeIn(delta, 5)

fun easeOutQuad(delta: Double) = easeOut(delta, 2)
fun easeOutCubic(delta: Double) = easeOut(delta, 3)
fun easeOutQuart(delta: Double) = easeOut(delta, 4)
fun easeOutQuint(delta: Double) = easeOut(delta, 5)

fun easeInOutQuad(delta: Double) = if (delta < 0.5) 2 * delta.pow(2) else 1.0 - (-2.0 * delta + 2).pow(2) / 2.0
fun easeInOutCubic(delta: Double) = if (delta < 0.5) 4 * delta.pow(3) else 1.0 - (-2.0 * delta + 2).pow(3) / 2.0
fun easeInOutQuart(delta: Double) = if (delta < 0.5) 8 * delta.pow(4) else 1.0 - (-2.0 * delta + 2).pow(4) / 2.0
fun easeInOutQuint(delta: Double) = if (delta < 0.5) 16 * delta.pow(5) else 1.0 - (-2.0 * delta + 2).pow(5) / 2.0

private fun easeIn(delta: Double, power: Int) = delta.pow(power)
private fun easeOut(delta: Double, power: Int) = 1.0 - (1.0 - delta).pow(power)
