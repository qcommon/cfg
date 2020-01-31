package therealfarfetchd.commoncfg.api.cmds

interface PersistableRegistry {

  val elements: Collection<Persistable>

  fun save()

  interface Mutable : PersistableRegistry {

    fun register(p: Persistable)

  }

}