import mod.project
import java.util.*

var indent: String = ""

abstract class Block(var message:String) {
  abstract fun addElement()
  abstract fun removeElement()
}

class ClassBlock(private var factories: MutableList<SpriteActionFactory>, message: String) : Block(message) {
  override fun addElement() {
    factories.add(0, selectSerializer(true))
    updateActions()
  }

  override fun removeElement() {
    factories.clear()
    updateActions()
  }
}

class FactoryBlock(private var factory: SpriteActionFactory, private var factories: MutableList<SpriteActionFactory>, message: String, private var discrete: Boolean) : Block(message) {
  override fun addElement() {
    factories.add(factories.indexOf(factory) + 1, selectSerializer(discrete))
    updateActions()
  }

  override fun removeElement() {
    factories.remove(factory)
    updateActions()
  }
}

class ButtonBlock(private var entries: MutableList<ActionEntry>, message: String, private val discrete: Boolean) : Block(message) {
  override fun addElement() {
    val action = selectSerializer(discrete).create()
    action.sprite = selectSprite().resolve()
    entries.add(0, ActionEntry(world, action))
    updateActions()
  }

  override fun removeElement() {
    entries.clear()
    updateActions()
  }
}

class ActionBlock(private var entry: ActionEntry, private var entries: MutableList<ActionEntry>, message: String, private val discrete: Boolean) : Block(message) {
  override fun addElement() {
    entries.add(entries.indexOf(entry) + 1, ActionEntry(world, selectSerializer(
      discrete
    ).create(selectSprite().resolve())))
    updateActions()
  }

  override fun removeElement() {
    entries.remove(entry)
    updateActions()
  }
}

class CollisionBlock(private val entry: CollisionEntry, message: String) : Block(message){
  override fun addElement() {
    entry.factories.add(0, selectSerializer(true))
    updateActions()
  }

  override fun removeElement() {
    entry.factories.clear()
    updateActions()
  }
}

val blocks = mutableListOf<Block>()
fun updateActions() {
  blocks.clear()

  for(button in buttons) {
    if(button.project != user) continue
    showButtonActions(button.onClickActions, "?????? ?????????? ???? $button", true)
    showButtonActions(button.onPressActions, "?????? ?????????????? ???? $button", false)
  }

  for(spriteClass in project.classes) {
    showClassActions(spriteClass, spriteClass.onCreate, "?????? ???????????????? $spriteClass", true)
    showClassActions(spriteClass, spriteClass.always, "???????????? ?????? $spriteClass", false)
    for(entry in spriteClass.onCollision) {
      if(entry.factories.isEmpty()) continue
      showCollisionActions(entry, "?????? ???????????????????????? $spriteClass ?? ${entry.spriteClass} ", true)
    }
  }
}

fun showClassActions(spriteClass: SpriteClass, factories: MutableList<SpriteActionFactory>, message: String, discrete: Boolean) {
  if(factories.isEmpty()) return
  blocks.add(ClassBlock(factories, message))
  indent += "  "
  for(factory in factories) {
    blocks.add(FactoryBlock(factory, factories, "$indent${factory.fullText()}", discrete))
    factory.addChildBlocks()
  }
  indent = indent.substring(2)
}

fun showButtonActions(actions: MutableList<ActionEntry>, message: String, discrete: Boolean) {
  if(actions.isEmpty()) return
  blocks.add(ButtonBlock(actions, message, discrete))
  indent += "  "
  for(entry in actions) {
    blocks.add(ActionBlock(entry, actions,"$indent$entry", discrete))
  }
  indent = indent.substring(2)
}

fun showCollisionActions(entry:CollisionEntry, message: String, discrete: Boolean) {
  blocks.add(CollisionBlock(entry, message))
  indent += "  "
  for(factory in entry.factories) {
    blocks.add(FactoryBlock(factory, entry.factories,"$indent${factory.fullText()}", discrete))
    factory.addChildBlocks()
  }
  indent = indent.substring(2)
}