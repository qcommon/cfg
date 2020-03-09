package net.dblsaiko.qcommon.cfg.core.cmd;

import net.dblsaiko.qcommon.cfg.core.api.CommandDescription;
import net.dblsaiko.qcommon.cfg.core.api.cmd.CommandOptions;
import org.jetbrains.annotations.NotNull;

public class CommandOptionsImpl implements CommandOptions {

    public static final CommandDescription DEFAULT_DESC = CommandDescription.commandBased(name -> String.format("cfg.desc.%s", name));
    public static final CommandDescription DEFAULT_EXT_DESC = CommandDescription.commandBased(name -> String.format("cfg.desc.%s.ext", name));

    private final CommandDescription desc;
    private final CommandDescription extendedDesc;

    private CommandOptionsImpl(CommandDescription desc, CommandDescription extendedDesc) {
        this.desc = desc;
        this.extendedDesc = extendedDesc;
    }

    @Override
    public CommandOptions desc(@NotNull CommandDescription description) {
        return new CommandOptionsImpl(description, extendedDesc);
    }

    @Override
    public CommandOptions extendedDesc(@NotNull CommandDescription description) {
        return new CommandOptionsImpl(desc, description);
    }

    public CommandDescription getDesc() {
        return desc;
    }

    public CommandDescription getExtendedDesc() {
        return extendedDesc;
    }

    public static CommandOptionsImpl create() {
        return new CommandOptionsImpl(DEFAULT_DESC, DEFAULT_EXT_DESC);
    }

}
