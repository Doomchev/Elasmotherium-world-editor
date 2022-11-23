import mod.dragging.*
import java.awt.Graphics2D
import java.util.LinkedList

val classes = LinkedList<SpriteClass>()
val emptyClass = SpriteClass("")

class SpriteClass(var name: String): SceneElement() {
  private val sprites = LinkedList<Sprite>()
  val onCreate = LinkedList<SpriteFactory>()
  val always = LinkedList<SpriteFactory>()
  fun add(sprite: Sprite) {
    sprites.add(sprite)
  }

  override fun select(selection: Sprite, selected: LinkedList<Sprite>) {
    for(sprite in sprites.descendingIterator()) {
      sprite.select(selection, selected)
    }
  }

  override fun remove(shape: Shape) {
    sprites.remove(shape)
  }

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? {
    return spriteUnderCursor(sprites, fx, fy)
  }

  override fun draw(g: Graphics2D) {
    for(sprite in sprites) {
      sprite.draw(g)
    }
  }

  override fun toString(): String = name
}