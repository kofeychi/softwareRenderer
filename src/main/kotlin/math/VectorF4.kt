package kofeychi.math

import kotlin.math.sqrt

data class VectorF4(
    val x: Float,
    val y: Float,
    val z: Float,
    val w: Float
) : VectorF<VectorF4> {
    override fun components(): FloatArray = floatArrayOf(x, y, z, w)

    override operator fun plus(other: VectorF4) = VectorF4(x + other.x, y + other.y, z + other.z, w + other.w)
    override operator fun minus(other: VectorF4) = VectorF4(x - other.x, y - other.y, z - other.z, w - other.w)
    override operator fun unaryMinus() = VectorF4(-x, -y, -z, -w)

    override operator fun times(scalar: Float) = VectorF4(x * scalar, y * scalar, z * scalar, w * scalar)
    override operator fun div(scalar: Float) = VectorF4(x / scalar, y / scalar, z / scalar, w / scalar)

    override operator fun times(other: VectorF4) = VectorF4(x * other.x, y * other.y, z * other.z, w * other.w)
    override operator fun div(other: VectorF4) = VectorF4(x / other.x, y / other.y, z / other.z, w / other.w)

    override fun dot(other: VectorF4): Float = x * other.x + y * other.y + z * other.z + w * other.w

    fun quaternionMultiply(other: VectorF4): VectorF4 = VectorF4(
        w * other.x + x * other.w + y * other.z - z * other.y,
        w * other.y - x * other.z + y * other.w + z * other.x,
        w * other.z + x * other.y - y * other.x + z * other.w,
        w * other.w - x * other.x - y * other.y - z * other.z
    )

    override fun lenSqr(): Float = x * x + y * y + z * z + w * w

    override fun normalize(): VectorF4 {
        val len = len()
        return if (len > 1e-6f) this / len else VectorF4(0f, 0f, 0f, 0f)
    }

    override fun distanceSquaredTo(other: VectorF4): Float {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        val dw = w - other.w
        return dx * dx + dy * dy + dz * dz + dw * dw
    }
    override fun distanceTo(other: VectorF4): Float = sqrt(distanceSquaredTo(other))
}