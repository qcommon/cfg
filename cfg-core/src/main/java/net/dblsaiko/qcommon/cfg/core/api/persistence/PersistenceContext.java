package net.dblsaiko.qcommon.cfg.core.api.persistence;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;

import java.util.function.Consumer;

public interface PersistenceContext {

    /**
     * Add a new section to a config file. Note that the <code>function</code>
     * may not necessarily be called.
     *
     * @param file     the file to add to
     * @param function the function adding lines to the passed {@link LinePrinter}
     */
    void write(String file, Consumer<LinePrinter> function);

}
