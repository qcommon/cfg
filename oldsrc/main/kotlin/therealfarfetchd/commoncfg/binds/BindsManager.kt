package therealfarfetchd.commoncfg.binds

import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.api.cmds.Persistable
import therealfarfetchd.commoncfg.cmds.impl.Output

class BindsManager : CommandInitializer, Persistable {

  override val category = "user"

  private val binds = mutableMapOf<String, String>()

  override fun write(o: Output) {
    o.println("unbindall")
  }

  override fun onInitialize(api: CommonCfgApi.Mutable) {
    with(api.commandRegistry) {
      registerSimple("unbindall") { ctx, args ->

      }

      registerSimple("bind") { ctx, args ->

      }

      registerSimple("unbind") { ctx, args ->

      }

      registerSimple("key_listboundkeys") { ctx, args ->

      }

      registerSimple("key_findbinding") { ctx, args ->

      }

      registerSimple("+key_wrapper") { ctx, args ->

      }

      registerSimple("-key_wrapper") { ctx, args ->

      }
    }
  }

}