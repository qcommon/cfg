package net.dblsaiko.qcommon.cfg.core.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;

public class CombinedLinePrinter implements LinePrinter {

    private List<LinePrinter> listeners = new ArrayList<>();

    public void addListener(LinePrinter output) {
        listeners.add(output);
    }

    @Override
    public void print(@NotNull String s) {
        listeners.forEach($ -> $.print(s));
    }

}
