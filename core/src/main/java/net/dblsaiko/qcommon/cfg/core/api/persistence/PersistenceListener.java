package net.dblsaiko.qcommon.cfg.core.api.persistence;

import java.util.Set;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;

/**
 * An interface for handling making configuration persistent.
 */
public interface PersistenceListener {

    /**
     * Write a script to the current file. This will be called for each entry
     * returned by {@link PersistenceListener#files()}
     *
     * @param file   the current file
     * @param output the output to write to
     */
    void write(String file, LinePrinter output);

    /**
     * The set of files to read from and write to.
     *
     * @return the set
     */
    Set<String> files();

}
