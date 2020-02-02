package net.dblsaiko.qcommon.cfg.core.api.internals;

/**
 * Used to register qcommon-cfg extensions. You will most definitely not need
 * this unless you're writing an extension for qcommon-cfg directly.
 */
public interface InternalsApi {

    /**
     * Get the {@link InternalsApi} instance.
     *
     * @return the instance
     */
    static InternalsApi getInstance() {
        return net.dblsaiko.qcommon.cfg.core.api.impl.internals.InternalsApi.INSTANCE;
    }

}
