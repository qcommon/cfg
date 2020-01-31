package net.dblsaiko.qcommon.cfg.core.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;

public class CombinedConsoleOutput implements ConsoleOutput {

    private List<ConsoleOutput> listeners = new ArrayList<>();

    public void addListener(ConsoleOutput output) {
        listeners.add(output);
    }

    @Override
    public void print(@NotNull String s) {
        listeners.forEach($ -> $.print(s));
    }

}
