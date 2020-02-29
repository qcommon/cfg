package net.dblsaiko.qcommon.cfg.core.cmdproc;

import net.dblsaiko.qcommon.cfg.core.api.ExecSource;

import java.util.List;

public interface CommandScheduler {

    void exec(String script, ExecSource source);

    void exec(List<List<String>> script, ExecSource source);

}
