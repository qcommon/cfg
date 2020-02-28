package net.dblsaiko.qcommon.cfg.core.api.persistence;

/**
 * An interface for handling making configuration persistent.
 */
public interface PersistenceListener {

    /**
     * Writes to config files via the given {@link PersistenceContext}.
     *
     * @param ctx the persistence context
     */
    void write(PersistenceContext ctx);

}
