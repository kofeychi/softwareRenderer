package kofeychi.math

import kotlin.math.sqrt

interface VectorF<S : VectorF<S>> {
    fun components(): FloatArray

    operator fun plus(other: S): S
    operator fun minus(other: S): S
    operator fun unaryMinus(): S

    operator fun times(scalar: Float): S
    operator fun div(scalar: Float): S

    operator fun times(other: S): S
    operator fun div(other: S): S

    fun dot(other: S): Float

    fun lenSqr(): Float
    fun len(): Float = sqrt(lenSqr())

    fun normalize(): S

    fun distanceSquaredTo(other: S): Float
    fun distanceTo(other: S): Float = sqrt(distanceSquaredTo(other))
}