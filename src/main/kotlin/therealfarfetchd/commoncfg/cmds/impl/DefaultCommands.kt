package therealfarfetchd.commoncfg.cmds.impl

import therealfarfetchd.commoncfg.api.cmds.CommandRegistry

internal fun registerDefaultCommands(r: CommandRegistry.Mutable) {
  r.registerSimple("exec") { ctx, args ->
    ctx.disp.execFile(args[0])
  }

  r.registerSimple("echo") { ctx, args ->
    ctx.println(args.joinToString(" "))
  }

  r.registerSimple("list") { ctx, args ->
    val nameColLen = ctx.registry.commands.map { it.name.length }.max() ?: 0
    val descColLen = ctx.registry.commands.map { it.desc?.length ?: 0 }.max() ?: 0
    ctx.registry.commands
      .sortedBy { it.name }
      .map { "${it.name.padEnd(nameColLen)} ${it.desc.orEmpty().padEnd(descColLen)}" }
      .forEach(ctx::println)
  }
}