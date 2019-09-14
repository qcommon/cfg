package therealfarfetchd.commoncfg.cmds.impl

import therealfarfetchd.commoncfg.api.cmds.CommandRegistry
import therealfarfetchd.commoncfg.api.cmds.Persistable
import therealfarfetchd.commoncfg.api.cmds.PersistableRegistry
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class PersistableRegistryImpl(reg: CommandRegistry.Mutable) : PersistableRegistry.Mutable {

  override val elements: MutableSet<Persistable> = mutableSetOf()

  override fun register(p: Persistable) {
    elements += p
  }

  init {
    reg.registerSimple("save") { _, _ -> save() }
  }

  override fun save() {
    val store = mutableMapOf<String, MutableList<String>>()
    for (value in elements) {
      value.category?.also {
        val list = store.computeIfAbsent(it) { mutableListOf("// File: $it.cfg", "// Generated by common-cfg.", "") }
        value.write(ListOutput(list))
      }
    }
    for ((file, strs) in store) {
      val path = Paths.get("config", "$file.cfg")
      try {
        Files.write(path, strs)
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

}