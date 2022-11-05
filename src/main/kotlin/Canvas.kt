import mod.dragging.Drawing
import mod.dragging.zk
import java.awt.Graphics
import java.awt.Graphics2D
import java.util.LinkedList
import kotlin.math.pow

class Canvas(fx: Int, fy:Int, fwidth: Int, fheight:Int, scale: Double)
  : Shape(0.0, 0.0, fwidth.toDouble() / 2.0 / scale, fheight.toDouble() / 2.0 / scale) {
  var vdx: Double = 1.0
  var vdy: Double = 1.0
  var k: Double = 1.0

  class Area(var leftX: Int, var topY:Int, var width: Int, var height:Int) {
    var rightX: Int
      inline get() = leftX + width
      inline set(value) {
        leftX = value - width
      }
    var bottomY: Int
      inline get() = topY + height
      inline set(value) {
        topY = value - height
      }
    fun hasPoint(px: Int, py: Int): Boolean {
      return px >= leftX && px < leftX + width && py >= topY && py < topY + height
    }
  }

  val viewport: Area
  init {
    viewport = Area(fx, fy, fwidth, fheight)
    update()
  }

  val drawingModules = LinkedList<Drawing>()
  fun add(obj: Drawing) {
    drawingModules.add(obj)
  }

  override fun draw(g: Graphics2D) {
    val oldCanvas = currentCanvas
    currentCanvas = this
    update()
    g.setClip(viewport.leftX, viewport.topY, viewport.width, viewport.height)
    g.clearRect(viewport.leftX, viewport.topY, viewport.width, viewport.height)
    for(module in drawingModules) {
      module.draw(g)
    }
    if(currentDraggingCanvas == this && currentDraggingAction != null) {
      currentDraggingAction!!.drawWhileDragging(g)
    }
    currentCanvas = oldCanvas
  }

  fun update() {
    k = 1.0 * viewport.width / width
    height = 1.0 * viewport.height / k
    vdx = centerX * k - (viewport.leftX + 0.5 * viewport.width)
    vdy = centerY * k - (viewport.topY + 0.5 * viewport.height)
  }

  fun setZoom(zoom: Int) {
    width = viewport.width * zk.pow(zoom.toDouble())
    update()
  }

  fun setZoom(zoom: Int, x: Int, y: Int) {
    val fx1 = xFromScreen(x)
    val fy1 = yFromScreen(y)
    setZoom(zoom)
    val fx2 = xFromScreen(x)
    val fy2 = yFromScreen(y)
    centerX += fx1 - fx2
    centerY += fy1 - fy2
    update()
  }

  fun hasPoint(x: Int, y: Int): Boolean {
    return viewport.hasPoint(x, y)
  }
}
fun xToScreen(fieldX: Double): Int = (fieldX * currentCanvas.k - currentCanvas.vdx).toInt()
fun yToScreen(fieldY: Double): Int = (fieldY * currentCanvas.k - currentCanvas.vdy).toInt()
fun distToScreen(fieldDist: Double): Int = (fieldDist * currentCanvas.k).toInt()

fun xFromScreen(screenX: Int): Double = (screenX + currentCanvas.vdx) / currentCanvas.k
fun yFromScreen(screenY: Int): Double = (screenY + currentCanvas.vdy) / currentCanvas.k
fun distFromScreen(screenDist: Int): Double = screenDist / currentCanvas.k