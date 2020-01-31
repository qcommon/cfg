package therealfarfetchd.commoncfg.api.cmds

interface ConVar : Persistable {

  val name: String
  val desc: String?

  val default: String
  var value: String

}