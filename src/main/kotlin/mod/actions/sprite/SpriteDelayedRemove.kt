package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import actionsToRemove
import fpsk
import mod.dragging.enterDouble
import spritesToRemove


class SpriteDelayedRemoveFactory(private val delay: Double = 0.0): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteDelayedRemoveFactory(enterDouble("Введите задержку:").get())
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDelayedRemove(sprite, delay)
  }

  override fun toString(): String = "Удалить позже"
  override fun fullText(): String = "Удалить через $delay сек."
}

class SpriteDelayedRemove(sprite: Sprite, var delay: Double = 0.0): SpriteAction(sprite) {
  override fun execute() {
    delay -= fpsk
    if(delay <= 0) {
      spritesToRemove.add(sprite)
      actionsToRemove.add(this)
    }
  }

  override fun toString(): String = "Удалить через $delay сек."
}