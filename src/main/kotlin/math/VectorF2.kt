package kofeychi.math

import kotlin.math.sqrt

data class VectorF2(
    val x: Float,
    val y: Float
) : VectorF<VectorF2> {
    override fun components(): FloatArray = floatArrayOf(x, y)

    override operator fun plus(other: VectorF2) = VectorF2(x + other.x, y + other.y)
    override operator fun minus(other: VectorF2) = VectorF2(x - other.x, y - other.y)
    override operator fun unaryMinus() = VectorF2(-x, -y)

    override operator fun times(scalar: Float) = VectorF2(x * scalar, y * scalar)
    override operator fun div(scalar: Float) = VectorF2(x / scalar, y / scalar)

    override operator fun times(other: VectorF2) = VectorF2(x * other.x, y * other.y)
    override operator fun div(other: VectorF2) = VectorF2(x / other.x, y / other.y)

    override fun dot(other: VectorF2): Float = x * other.x + y * other.y

    override fun lenSqr(): Float = x * x + y * y

    override fun normalize(): VectorF2 {
        val len = len()
        return if (len > 1e-6f) this / len else VectorF2(0f, 0f)
    }

    override fun distanceSquaredTo(other: VectorF2): Float {
        val dx = x - other.x
        val dy = y - other.y
        return dx * dx + dy * dy
    }
    override fun distanceTo(other: VectorF2): Float = sqrt(distanceSquaredTo(other))
}