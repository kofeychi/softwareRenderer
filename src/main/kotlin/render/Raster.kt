package kofeychi.render

import kofeychi.math.VectorF2
import kotlin.math.max
import kotlin.math.min

object Raster {
    fun cross(v0: VectorF2, v1: VectorF2, p: VectorF2): Float {
        return (p.x - v0.x) * (v1.y - v0.y) - (p.y - v0.y) * (v1.x - v0.x)
    }

    fun <V : Vertex> raster(
        program: Program<V>,
        target: RenderTarget,
        v0: V,
        v1: V,
        v2: V
    ) {
        val vert = program.vertex
        val vv0 = vert.apply(v0)
        val vv1 = vert.apply(v1)
        val vv2 = vert.apply(v2)

        val minX = max(0, min(vv0.x, min(vv1.x, vv2.x)).toInt())
        val maxX = min(target.width - 1, max(vv0.x, max(vv1.x, vv2.x)).toInt())
        val minY = max(0, min(vv0.y, min(vv1.y, vv2.y)).toInt())
        val maxY = min(target.height - 1, max(vv0.y, max(vv1.y, vv2.y)).toInt())

        val totalAreaX2 = cross(vv0, vv1, vv2)
        if (totalAreaX2 == 0f) return

        for (y in minY..maxY) {
            for (x in minX..maxX) {

                val pixelCenter = VectorF2(x + 0.5f, y + 0.5f)

                val w2 = cross(vv0, vv1, pixelCenter)
                val w0 = cross(vv1, vv2, pixelCenter)
                val w1 = cross(vv2, vv0, pixelCenter)

                if (w0 < 0 || w1 < 0 || w2 < 0) continue

                val alpha = w0 / totalAreaX2
                val beta  = w1 / totalAreaX2
                val gamma = w2 / totalAreaX2

                val color = program.fragment.apply(
                    v0, vv0,
                    v1, vv1,
                    v2, vv2,
                    alpha, beta, gamma
                )

                target.setPixel(x, y, color)
            }
        }
    }

    fun <V : Vertex> quad(
        program: Program<V>,
        target: RenderTarget,
        tl: V, // Top-Left     (e.g., 0, 0)
        bl: V, // Bottom-Left  (e.g., 0, 600)
        tr: V, // Top-Right    (e.g., 600, 0)
        br: V  // Bottom-Right (e.g., 600, 600)
    ) {
        raster(program, target, tl, bl, br)
        raster(program, target, tl, br, tr)
    }
}