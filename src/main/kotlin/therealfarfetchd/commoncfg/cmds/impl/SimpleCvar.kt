package therealfarfetchd.commoncfg.cmds.impl

import therealfarfetchd.commoncfg.api.cmds.Command
import therealfarfetchd.commoncfg.api.cmds.ConVar
import therealfarfetchd.commoncfg.api.cmds.CvarField
import therealfarfetchd.commoncfg.api.cmds.CvarMapper
import therealfarfetchd.commoncfg.api.cmds.ExecContext
import therealfarfetchd.commoncfg.api.cmds.Persistable

class SimpleCvar<T>(override val name: String, override val desc: String?, val field: CvarField<T>, val mapper: CvarMapper<T>, override val category: String?) : Command, ConVar {

  override val default = mapper.toStr(field())

  override var value: String
    get() = mapper.toStr(field())
    set(value) = this.field.set(mapper.parse(value))

  override fun exec(ctx: ExecContext, vararg args: String) {
    if (args.isEmpty()) {
      if (desc != null) ctx.println(desc)
      ctx.println("$name = $value (default $default)")
    } else {
      value = args[0]
    }
  }

  override fun write(o: Output) {
    o.println("${Persistable.escape(name)} ${Persistable.escape(value)}")
  }

}