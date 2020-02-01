package net.dblsaiko.qcommon.cfg.core.cmdproc;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.cmd.ControlFlow;

public class ControlFlowImpl implements ControlFlow {

    private Type type = Type.PROCEED;
    private String script = null;

    @Override
    public void proceed() {
        this.type = Type.PROCEED;
        this.script = null;
    }

    @Override
    public void suspend() {
        this.type = Type.SUSPEND;
        this.script = null;
    }

    @Override
    public void enterSubroutine(String script) {
        this.type = Type.ENTER_SUBROUTINE;
        this.script = script;
    }

    public Type getType() {
        return this.type;
    }

    @NotNull
    public String getScriptSource() {
        if (this.type != Type.ENTER_SUBROUTINE) throw new IllegalStateException("type != ENTER_SUBROUTINE");
        return this.script;
    }

    public enum Type {
        PROCEED,
        SUSPEND,
        ENTER_SUBROUTINE,
    }

}
