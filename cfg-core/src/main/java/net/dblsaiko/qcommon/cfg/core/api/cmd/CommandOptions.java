package net.dblsaiko.qcommon.cfg.core.api.cmd;

import net.dblsaiko.qcommon.cfg.core.api.CommandDescription;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.cmd.CommandOptionsImpl;
import org.jetbrains.annotations.NotNull;

public interface CommandOptions {

    /**
     * Set the description for this command. This will show up in help texts
     * and in generated configuration files.
     * <p>
     * The default value is <code>translated("cfg.desc.${command}")</code>
     *
     * @param description the description to set
     * @return a new {@link CvarOptions} instance with this option changed
     */
    CommandOptions desc(@NotNull CommandDescription description);

    /**
     * Set the extended description for this command. This can span across
     * multiple lines and should explain what this command and the available
     * values do in detail.
     *
     * <p>
     * The default value is <code>translated("cfg.desc.${command}.ext")</code>
     *
     * @param description the description to set
     * @return a new {@link CvarOptions} instance with this option changed
     */
    CommandOptions extendedDesc(@NotNull CommandDescription description);

    /**
     * Create a new {@link CommandOptions} instance with default settings
     *
     * @return a new {@link CommandOptions} instance
     */
    @NotNull
    static CommandOptions create() {
        return CommandOptionsImpl.create();
    }

}
