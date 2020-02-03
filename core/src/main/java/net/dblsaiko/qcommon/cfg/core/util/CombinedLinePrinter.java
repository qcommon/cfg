package net.dblsaiko.qcommon.cfg.core.util;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
