package therealfarfetchd.commoncfg

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.api.cmds.Persistable
import java.nio.file.Files
import java.nio.file.Paths

const val ModID = "common-cfg"

object CommonCfg : ModInitializer {

  val logger = LogManager.getLogger(ModID)

  val api = ApiImpl()

  override fun onInitialize() {
    CommonCfgApi.instance = api
    Files.createDirectories(Paths.get("config", "ccfg"))
    setupEntrypoints(FabricLoader.getInstance().getEntrypoints("commands", CommandInitializer::class.java))
  }

  @JvmSynthetic
  internal fun setupEntrypoints(l: List<CommandInitializer>) {
    var errors = 0
    for (entrypoint in l) {
      try {
        entrypoint.onInitialize(api)
      } catch (e: Exception) {
        e.printStackTrace()
        errors += 1
      }
    }
    if (errors > 0) {
      error("$errors errors while initializing commands/cvars. Aborting.")
    }
  }

  fun setupEnv() {
    api.persistRegistry.elements
      .mapNotNull(Persistable::category)
      .distinct()
      .forEach(api.dispatcher::execFile)
    api.dispatcher.execFile("autoexec")
  }

}