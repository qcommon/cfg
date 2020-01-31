package net.dblsaiko.qcommon.cfg.core.cmdproc;

import net.dblsaiko.qcommon.cfg.core.api.ExecSource;

public interface CommandScheduler {

    void exec(String script, ExecSource source);

}
