package net.dblsaiko.qcommon.cfg.core.cmdproc;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import net.dblsaiko.qcommon.cfg.core.api.ExecSource;

public class ExecState {

    private final ExecSource source;

    private final Deque<SubroutineState> stack;

    public ExecState(ExecSource source, List<List<String>> script) {
        this.source = source;
        this.stack = new LinkedList<>();
        enterSubroutine(script);
    }

    public void enterSubroutine(List<List<String>> script) {
        stack.push(new SubroutineState(script));
    }

    public ExecSource getSource() {
        return source;
    }

    public Optional<List<String>> next() {
        feed();
        if (stack.isEmpty()) return Optional.empty();
        SubroutineState tos = stack.peek();
        Optional<List<String>> result = tos.next();
        if (!tos.hasNext()) stack.pop();
        return result;
    }

    public boolean hasNext() {
        feed();
        return !stack.isEmpty();
    }

    private void feed() {
        while (!stack.isEmpty() && !stack.peek().hasNext()) stack.pop();
    }

}
