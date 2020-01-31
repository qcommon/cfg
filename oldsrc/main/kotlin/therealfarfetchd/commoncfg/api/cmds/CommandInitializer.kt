package therealfarfetchd.commoncfg.api.cmds

import therealfarfetchd.commoncfg.api.CommonCfgApi

interface CommandInitializer {

  fun onInitialize(api: CommonCfgApi.Mutable)

}