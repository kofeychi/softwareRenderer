package kofeychi.render

import kofeychi.math.VectorF4

interface RenderTarget {
    val width: Int
    val height: Int

    fun setPixel(x: Int, y: Int, color: VectorF4)
}