package net.dblsaiko.qcommon.cfg.core.cmdproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;

public class CommandDispatcher implements CommandScheduler {

    private final Object lock = new Object();

    private List<ExecState> scheduled = new ArrayList<>();

    private Map<String, String> aliases = new HashMap<>();

    private final CommandRegistry commandRegistry;
    private final ConsoleOutput output;

    public CommandDispatcher(CommandRegistry commandRegistry, ConsoleOutput output) {
        this.commandRegistry = commandRegistry;
        this.output = output;
    }

    @Override
    public void exec(String script, ExecSource source) {
        synchronized (lock) {
            scheduled.add(new ExecState(source, tokenize(script)));
        }
    }

    public void resume() {
        List<ExecState> scheduled;
        synchronized (lock) {
            scheduled = new ArrayList<>(this.scheduled);
            this.scheduled.clear();
        }
        for (ExecState execState : scheduled) {
            step(execState);
        }
        scheduled.removeIf(el -> !el.hasNext());
        synchronized (lock) {
            this.scheduled.addAll(scheduled);
        }
    }

    private static final String[] dummy = new String[0];

    private void step(ExecState script) {
        Optional<List<String>> next;
        while ((next = script.next()).isPresent()) {
            List<String> cmd = next.get();
            if (cmd.size() > 0) {
                NextAction act = exec(cmd.get(0), cmd.subList(1, cmd.size()).toArray(dummy), script.getSource());
                if (act == NextAction.SUSPEND) return;
            }
        }
    }

    private NextAction exec(String command, String[] args, ExecSource source) {
        switch (command) {
            case "alias":
                if (source == ExecSource.REMOTE) break;
                if (args.length > 1) {
                    aliases.put(args[0], args[1]);
                } else if (args.length > 0) {
                    String value = aliases.get(args[0]);
                    if (value == null) {
                        output.printf("'%s' is not an alias", args[0]);
                    } else {
                        output.printf("'%s' = '%s'", args[0], value);
                    }
                }
                break;
            case "unalias":
                if (source == ExecSource.REMOTE) break;
                if (args.length > 0) {
                    aliases.remove(args[0]);
                }
                break;
            case "wait":
                return NextAction.SUSPEND;
            default:
                ConVar cvar = commandRegistry.findCvar(command);
                if (cvar != null) {
                    // TODO disallow setting server-side cvars on client
                    // TODO allow the server to sync cvars
                    if (source != ExecSource.REMOTE) {
                        if (args.length > 0) {
                            cvar.setFromString(args);
                        } else {
                            cvar.printState(command, output);
                        }
                    }
                } else {
                    Command cmd = commandRegistry.findCommand(command);
                    if (cmd != null) {
                        if (source != ExecSource.REMOTE || cmd.allowRemoteExec()) {
                            cmd.exec(args, source, output);
                        }
                    } else {
                        output.printf("Command not found: %s", command);
                    }
                }
        }
        return NextAction.CONTINUE;
    }

    private static List<List<String>> tokenize(String source) {
        boolean esc = false;
        boolean quoted = false;
        List<List<String>> commands = new ArrayList<>();
        List<String> current = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        Runnable nextToken = () -> {
            if (!sb.toString().trim().isEmpty()) {
                current.add(sb.toString());
            }
            sb.delete(0, sb.length());
        };

        Runnable nextCommand = () -> {
            nextToken.run();
            if (!current.isEmpty()) {
                commands.add(new ArrayList<>(current));
            }
            current.clear();
        };

        for (String line : source.split("(\r?\n|\r)")) {
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (esc) {
                    sb.append(c);
                    esc = false;
                } else if (!quoted && c == '/' && i < chars.length - 1 && chars[i + 1] == '/') {
                    break;
                } else if (!quoted && c == ';') {
                    nextCommand.run();
                } else if (!quoted && c == ' ') {
                    nextToken.run();
                } else if (c == '"') {
                    quoted = !quoted;
                } else if (c == '\\') {
                    esc = true;
                } else {
                    sb.append(c);
                }
            }

            nextCommand.run();
        }

        return commands;
    }

}
