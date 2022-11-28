package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk

class SpriteMoveFactory: SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteMoveFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteMove(sprite)
  }

  override fun fullText(): String = "Перемещать"

  override fun getClassName(): String = "SpriteMoveFactory"

  override fun store(node: Node) {
  }

  override fun load(node: Node) {
  }
}
class SpriteMove(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX += fpsk * sprite.dx
    sprite.centerY += fpsk * sprite.dy
  }

  override fun toString(): String = "Перемещать"

  override fun getClassName(): String = "SpriteMove"

  override fun store(node: Node) {
    node.setField("sprite", sprite)
  }

  override fun load(node: Node) {
    sprite = node.getField("sprite") as Sprite
  }
}