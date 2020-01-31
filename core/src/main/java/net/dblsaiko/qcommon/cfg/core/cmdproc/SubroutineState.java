package net.dblsaiko.qcommon.cfg.core.cmdproc;

import java.util.List;
import java.util.Optional;

public class SubroutineState {

    private int line;

    private final List<List<String>> script;

    public SubroutineState(List<List<String>> script) {
        this.line = 0;
        this.script = script;
    }

    public Optional<List<String>> next() {
        if (hasNext()) {
            Optional<List<String>> line = Optional.of(script.get(this.line));
            this.line += 1;
            return line;
        } else {
            return Optional.empty();
        }
    }

    public boolean hasNext() {
        return pos() < length();
    }

    public int length() {
        return script.size();
    }

    public int pos() {
        return line;
    }

}
