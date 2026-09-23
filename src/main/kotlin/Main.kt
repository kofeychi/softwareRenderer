package kofeychi

import kofeychi.math.*
import kofeychi.render.*
import java.awt.*
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*
import kotlin.math.cos
import kotlin.math.sin

var time = 0f

data class Texture(val width: Int, val height: Int, val data: IntArray)

val tex by lazy {
    val originalImage = ImageIO.read(File("C:\\Users\\11\\Documents\\GitHub\\softwareRenderer\\src\\main\\resources\\texture.png"))

    val intImage = BufferedImage(
        originalImage.width,
        originalImage.height,
        BufferedImage.TYPE_INT_ARGB
    )

    val g = intImage.createGraphics()
    g.drawImage(originalImage, 0, 0, null)
    g.dispose()

    val data = (intImage.raster.dataBuffer as DataBufferInt).data
    Texture(originalImage.width, originalImage.height, data)
}

fun VectorF4.toARGB(): Int {
    val ir = (x.coerceIn(0.0f, 1.0f) * 255).toInt()
    val ig = (y.coerceIn(0.0f, 1.0f) * 255).toInt()
    val ib = (z.coerceIn(0.0f, 1.0f) * 255).toInt()
    val ia = (w.coerceIn(0.0f, 1.0f) * 255).toInt()
    return (ia shl 24) or (ir shl 16) or (ig shl 8) or ib
}

class Vert(
    override val pos: VectorF2,
    val uv: VectorF2
) : Vertex

class VertShader : VertexShader<Vert> {
    fun rotatePoint(point: VectorF2, pivot: VectorF2, angleRadians: Double): VectorF2 {
        val cosTheta = cos(angleRadians).toFloat()
        val sinTheta = sin(angleRadians).toFloat()

        val dx = point.x - pivot.x
        val dy = point.y - pivot.y

        val x = cosTheta * dx - sinTheta * dy + pivot.x
        val y = sinTheta * dx + cosTheta * dy + pivot.y

        return VectorF2(x, y)
    }
    override fun apply(vertex: Vert): VectorF2 = rotatePoint(vertex.pos, VectorF2(300f,300f),(time / 100).toDouble())
}

class FragShader : FragmentShader<Vert> {
    override fun apply(
        x: Int,
        y: Int,
        v0: Vert,
        vv0: VectorF2,
        v1: Vert,
        vv1: VectorF2,
        v2: Vert,
        vv2: VectorF2,
        alpha: Float,
        beta: Float,
        gamma: Float
    ): VectorF4 {

        val u = v0.uv.x * alpha + v1.uv.x * beta + v2.uv.x * gamma
        val v = v0.uv.y * alpha + v1.uv.y * beta + v2.uv.y * gamma
        
        val texX = (u * (tex.width - 1)).toInt().coerceIn(0, tex.width - 1)
        val texY = (v * (tex.height - 1)).toInt().coerceIn(0, tex.height - 1)

        val c = tex.data[texY * tex.width + texX]

        return VectorF4(
            ((c shr 16) and 0xFF) / 255f, // R -> x
            ((c shr 8)  and 0xFF) / 255f, // G -> y
            (c and 0xFF) / 255f,          // B -> z
            ((c shr 24) and 0xFF) / 255f  // A -> w
        )
    }
}

val program = Program(
    VertShader(),
    FragShader()
)

class ImageRenderTarget(
    override val width: Int,
    override val height: Int,
    private val buffer: IntArray
) : RenderTarget {
    override fun setPixel(x: Int, y: Int, color: VectorF4) {
        buffer[y * width + x] = color.toARGB()
    }
}

class Rasterrrrr(width: Int, height: Int) : JPanel() {
    private val canvasImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    init {
        preferredSize = Dimension(width, height)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val bgGraphics = canvasImage.createGraphics()
        bgGraphics.color = Color(30, 30, 35)
        bgGraphics.fillRect(0, 0, width, height)
        bgGraphics.dispose()

        val buf = (canvasImage.raster.dataBuffer as DataBufferInt).data
        val renderTarget = ImageRenderTarget(width, height, buf)

        Raster.quad(
            program,
            renderTarget,
            Vert(VectorF2(0f,0f),     VectorF2(0f, 0f)),
            Vert(VectorF2(0f,600f),   VectorF2(0f, 1f)),
            Vert(VectorF2(600f,0f),   VectorF2(1f, 0f)),
            Vert(VectorF2(600f,600f), VectorF2(1f, 1f))
        )
        g.drawImage(canvasImage, 0, 0, null)
    }
}

fun main() {
    val windowWidth = 600
    val windowHeight = 600

    val frame = JFrame("raster")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.add(Rasterrrrr(windowWidth, windowHeight))
    frame.pack()

    frame.setLocationRelativeTo(null)
    frame.isVisible = true

    while(true) {
        time += .2f
        frame.repaint()
        Thread.sleep(15)
    }
}