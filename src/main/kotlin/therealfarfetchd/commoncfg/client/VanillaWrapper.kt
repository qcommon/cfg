package therealfarfetchd.commoncfg.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen
import net.minecraft.client.options.BooleanOption
import net.minecraft.client.options.DoubleOption
import net.minecraft.client.options.KeyBinding
import net.minecraft.client.options.Option
import net.minecraft.text.LiteralText
import net.minecraft.util.math.MathHelper
import therealfarfetchd.commoncfg.CommonCfgClient
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.BooleanMapper
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.api.cmds.CvarField
import therealfarfetchd.commoncfg.api.cmds.DoubleMapper
import therealfarfetchd.commoncfg.api.cmds.provide
import therealfarfetchd.commoncfg.client.binds.incrTimesPressed
import therealfarfetchd.commoncfg.client.ext.step

class VanillaWrapper : CommandInitializer {

  override fun onInitialize(api: CommonCfgApi.Mutable) {
    val mc = MinecraftClient.getInstance()

    fun wrapKb(name: String, kb: KeyBinding) =
      api.commandRegistry.registerSimple(name) { _, _ -> kb.incrTimesPressed() }

    fun wrapKbHold(name: String, kb: KeyBinding) {
      api.commandRegistry.registerSimple("+$name") { _, _ -> kb.isPressed = true; kb.incrTimesPressed() }
      api.commandRegistry.registerSimple("-$name") { _, _ -> kb.isPressed = false }
    }

    fun wrapOption(name: String, opt: DoubleOption, persistFile: String? = null) {
      api.cvarRegistry.provide(name, CvarField.from({ opt.get(mc.options) }, { opt.set(mc.options, it) }),
        persistFile, DoubleMapper.limit(opt.min, opt.max, opt.step))
    }

    fun wrapOption(name: String, opt: BooleanOption, persistFile: String? = null) {
      api.cvarRegistry.provide(name, CvarField.from({ opt.get(mc.options) }, { opt.set(mc.options, if (it) "true" else null) }),
        persistFile, BooleanMapper)
    }

    wrapKb("console", CommonCfgClient.kbConsole)

    wrapKbHold("walk", mc.options.keyForward)
    wrapKbHold("left", mc.options.keyLeft)
    wrapKbHold("back", mc.options.keyBack)
    wrapKbHold("right", mc.options.keyRight)
    wrapKbHold("jump", mc.options.keyJump)
    wrapKbHold("crouch", mc.options.keySneak)
    wrapKbHold("run_on", mc.options.keySprint)
    wrapKb("open_inventory", mc.options.keyInventory)
    wrapKb("swap_hands", mc.options.keySwapHands)
    wrapKb("drop_item", mc.options.keyDrop)
    wrapKbHold("use", mc.options.keyUse)
    wrapKbHold("attack", mc.options.keyAttack)
    wrapKb("pick_block", mc.options.keyPickItem)
    // wrapKb("", mc.options.keyChat)
    wrapKbHold("playerlist", mc.options.keyPlayerList)
    // wrapKb("", mc.options.keyCommand)
    wrapKb("screenshot", mc.options.keyScreenshot)
    wrapKb("cycle_perspective", mc.options.keyTogglePerspective)
    wrapKb("toggle_cinematic_cam", mc.options.keySmoothCamera)
    wrapKb("fullscreen", mc.options.keyFullscreen)
    wrapKb("spectator_outlines", mc.options.keySpectatorOutlines)
    wrapKb("open_advancements", mc.options.keyAdvancements)

    for (i in 0 until 9) {
      wrapKb("slot${i + 1}", mc.options.keysHotbar[i])

      api.commandRegistry.registerSimple("save_toolbar_${i + 1}") { _, _ ->
        if (mc.player?.isCreative == true) CreativeInventoryScreen.onHotbarKeyPress(mc, i, false, true)
      }

      api.commandRegistry.registerSimple("load_toolbar_${i + 1}") { _, _ ->
        if (mc.player?.isCreative == true) CreativeInventoryScreen.onHotbarKeyPress(mc, i, true, false)
      }
    }

    with(api.commandRegistry) {
      registerSimple("menu") { _, _ -> mc.openPauseMenu(false) }
      registerSimple("pause") { _, _ -> mc.openPauseMenu(true) }
      registerSimple("open_chat") { _, args -> mc.openScreen(ChatScreen(args.getOrNull(0).orEmpty())) }
      registerSimple("say") { _, args -> if (args.isNotEmpty()) mc.player?.sendChatMessage(args[0]) }
      registerSimple("echo_chat") { _, args -> mc.player?.addChatMessage(LiteralText(args.getOrNull(0).orEmpty()), false) }
      registerSimple("info") { _, args -> mc.player?.addChatMessage(LiteralText(args.getOrNull(0).orEmpty()), true) }
      registerSimple("next_slot") { _, _ -> scroll(-1) }
      registerSimple("prev_slot") { _, _ -> scroll(1) }
    }

    with(api.cvarRegistry) {
      provide("cl_showdebug", mc.options::debugEnabled)
      provide("cl_showprofiler", mc.options::debugProfilerEnabled)
      provide("cl_showtps", mc.options::debugTpsEnabled)
    }

    wrapOption("r_fov", Option.FOV, "user")
    wrapOption("r_vsync", Option.VSYNC, "user")
    wrapOption("r_gamma", Option.GAMMA, "user")
    wrapOption("r_viewbob", Option.VIEW_BOBBING, "user")
    wrapOption("r_viewdist", Option.RENDER_DISTANCE, "user")
    wrapOption("auto_jump", Option.AUTO_JUMP, "user")
    wrapOption("lookspeed", Option.SENSITIVITY, "user")
  }

  private fun scroll(i: Int) {
    val mc = MinecraftClient.getInstance()
    val player = mc.player ?: return
    if (player.isSpectator) {
      if (mc.inGameHud.spectatorHud.isOpen) {
        mc.inGameHud.spectatorHud.cycleSlot((-i).toDouble())
      } else {
        val j = MathHelper.clamp(player.abilities.flySpeed + i * 0.005f, 0.0f, 0.2f)
        player.abilities.flySpeed = j
      }
    } else {
      player.inventory.scrollInHotbar(i.toDouble())
    }
  }

}