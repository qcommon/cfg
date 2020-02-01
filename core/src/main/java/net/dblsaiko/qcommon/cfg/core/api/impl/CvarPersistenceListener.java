package net.dblsaiko.qcommon.cfg.core.api.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceListener;

public class CvarPersistenceListener implements PersistenceListener {

    private Map<String, Set<Pair<String, ConVar>>> cvars = new HashMap<>();

    @Override
    public void write(String file, LinePrinter output) {
        cvars.get(file).stream()
            .map(cvar -> Stream.concat(Stream.of(cvar.getLeft()), Arrays.stream(cvar.getRight().getAsStrings()).map(ConfigApi.INSTANCE::escape)).collect(Collectors.joining(" ")))
            .forEach(output::print);
    }

    @Override
    public Set<String> files() {
        return cvars.keySet();
    }

    public void register(String name, ConVar cvar, String file) {
        cvars.computeIfAbsent(file, _file -> new HashSet<>()).add(Pair.of(name, cvar));
    }

}
