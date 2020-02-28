package net.dblsaiko.qcommon.cfg.core.cvar;

import net.dblsaiko.qcommon.cfg.core.ConfigApiImpl;
import net.dblsaiko.qcommon.cfg.core.api.CommandDescription;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceContext;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceListener;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CvarPersistenceListener implements PersistenceListener {

    private Map<String, Set<Pair<String, ConVar>>> cvars = new HashMap<>();
    private Map<String, CommandDescription> descriptions = new HashMap<>();
    private Map<String, CommandDescription> extDescriptions = new HashMap<>();

    @Override
    public void write(PersistenceContext ctx) {
        cvars.forEach((k, v) -> ctx.write(k, lp -> {
            boolean lastEntryHasComment = true;
            for (Pair<String, ConVar> entry : (Iterable<Pair<String, ConVar>>) cvars.get(k).stream().sorted(Comparator.comparing(Pair::getLeft))::iterator) {
                String name = entry.getLeft();
                ConVar cvar = entry.getRight();
                String desc = descriptions.get(name).getValue(name);
                String extDesc = extDescriptions.get(name).getValue(name);
                String command = Stream.concat(Stream.of(name), Arrays.stream(cvar.getAsStrings()).map(ConfigApiImpl.INSTANCE::escape)).collect(Collectors.joining(" "));

                boolean b = !desc.trim().isEmpty();
                boolean b1 = !extDesc.trim().isEmpty();
                if (b || b1 || lastEntryHasComment) lp.print();

                if (b) {
                    for (String s : desc.split("\n")) lp.print(String.format("// %s", s));
                }
                if (b1) {
                    for (String s : extDesc.split("\n")) lp.print(String.format("// %s", s));
                }
                lp.print(command);

                lastEntryHasComment = b || b1;
            }
        }));
    }

    public void register(String name, ConVar cvar, String file, CommandDescription desc, CommandDescription extendedDesc) {
        cvars.computeIfAbsent(file, _file -> new HashSet<>()).add(Pair.of(name, cvar));
        descriptions.put(name, desc);
        extDescriptions.put(name, extendedDesc);
    }

}
