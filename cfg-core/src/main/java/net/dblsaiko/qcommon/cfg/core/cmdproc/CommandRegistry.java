package net.dblsaiko.qcommon.cfg.core.cmdproc;

import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandRegistry {

    private Map<String, Command> commands = new LinkedHashMap<>();
    private Map<String, ConVar> cvars = new LinkedHashMap<>();

    public void addCommand(String name, Command command) {
        if (commands.containsKey(name) || cvars.containsKey(name))
            throw new IllegalStateException(String.format("Command/cvar '%s' already exists!", name));
        commands.put(name, command);
    }

    public void addConVar(String name, ConVar cvar) {
        if (commands.containsKey(name) || cvars.containsKey(name))
            throw new IllegalStateException(String.format("Command/cvar '%s' already exists!", name));
        cvars.put(name, cvar);
    }

    public Command findCommand(String name) {
        return commands.get(name);
    }

    public ConVar findCvar(String name) {
        return cvars.get(name);
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(commands);
    }

    public Map<String, ConVar> getCvars() {
        return Collections.unmodifiableMap(cvars);
    }

}
