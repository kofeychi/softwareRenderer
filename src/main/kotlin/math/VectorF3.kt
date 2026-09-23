package kofeychi.math

import kotlin.math.sqrt

data class VectorF3(
    val x: Float,
    val y: Float,
    val z: Float
) : VectorF<VectorF3> {
    override fun components(): FloatArray = floatArrayOf(x, y, z)

    override operator fun plus(other: VectorF3) = VectorF3(x + other.x, y + other.y, z + other.z)
    override operator fun minus(other: VectorF3) = VectorF3(x - other.x, y - other.y, z - other.z)
    override operator fun unaryMinus() = VectorF3(-x, -y, -z)

    override operator fun times(scalar: Float) = VectorF3(x * scalar, y * scalar, z * scalar)
    override operator fun div(scalar: Float) = VectorF3(x / scalar, y / scalar, z / scalar)

    override operator fun times(other: VectorF3) = VectorF3(x * other.x, y * other.y, z * other.z)
    override operator fun div(other: VectorF3) = VectorF3(x / other.x, y / other.y, z / other.z)

    override fun dot(other: VectorF3): Float = x * other.x + y * other.y + z * other.z

    fun cross(other: VectorF3): VectorF3 = VectorF3(
        x = y * other.z - z * other.y,
        y = z * other.x - x * other.z,
        z = x * other.y - y * other.x
    )

    override fun lenSqr(): Float = x * x + y * y + z * z

    override fun normalize(): VectorF3 {
        val len = len()
        return if (len > 1e-6f) this / len else VectorF3(0f, 0f, 0f)
    }

    override fun distanceSquaredTo(other: VectorF3): Float {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return dx * dx + dy * dy + dz * dz
    }
    override fun distanceTo(other: VectorF3): Float = sqrt(distanceSquaredTo(other))
}