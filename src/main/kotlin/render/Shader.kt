package kofeychi.render

import kofeychi.math.VectorF2
import kofeychi.math.VectorF4

data class Program<V : Vertex>(
    val vertex: VertexShader<V>,
    val fragment: FragmentShader<V>
)

sealed interface Shader<V : Vertex>

interface VertexShader<V : Vertex> : Shader<V> {
    fun apply(
        vertex: V
    ): VectorF2
}

interface FragmentShader<V : Vertex> : Shader<V> {
    fun apply(
        v0: V,vv0: VectorF2,
        v1: V,vv1: VectorF2,
        v2: V,vv2: VectorF2,
        alpha: Float,
        beta: Float,
        gamma: Float
    ): VectorF4
}