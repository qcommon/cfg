package net.dblsaiko.qcommon.cfg.core.api.impl;

import net.dblsaiko.qcommon.cfg.core.api.ValidationContext;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ValidationContextImpl implements ValidationContext {

    private final List<Entry> entries = new ArrayList<>();

    @Override
    public void warning(String fmt, Object... args) {
        entries.add(new WarningEntry(String.format(fmt, args)));
    }

    @Override
    public void error(String fmt, Object... args) {
        entries.add(new ErrorEntry(String.format(fmt, args)));
    }

    public boolean tryContinue() {
        boolean result = entries.stream().noneMatch(Entry::blocksContinue);
        Logger logger = ConfigApi.logger;
        entries.forEach($ -> $.log(logger));
        entries.clear();
        return result;
    }

    private static abstract class Entry {
        protected final String text;

        protected Entry(String text) {
            this.text = text;
        }

        public abstract void log(Logger logger);

        public abstract boolean blocksContinue();
    }

    private static class WarningEntry extends Entry {
        public WarningEntry(String text) {
            super(text);
        }

        @Override
        public void log(Logger logger) {
            logger.warn("warning: {}", text);
        }

        @Override
        public boolean blocksContinue() {
            return false;
        }
    }

    private static class ErrorEntry extends Entry {
        public ErrorEntry(String text) {
            super(text);
        }

        @Override
        public void log(Logger logger) {
            logger.error("error: {}", text);
        }

        @Override
        public boolean blocksContinue() {
            return true;
        }
    }
}
