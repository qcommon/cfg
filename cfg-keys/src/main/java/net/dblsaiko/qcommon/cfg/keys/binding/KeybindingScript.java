package net.dblsaiko.qcommon.cfg.keys.binding;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface KeybindingScript {

    @NotNull
    String script();

    @NotNull
    List<List<String>> pressed();

    List<List<String>> released();

    boolean repeatEvents();

    static KeybindingScript parse(String script) {
        List<List<String>> tokenizedScript = ConfigApi.getInstance().tokenize(script);
        if (tokenizedScript.size() == 1) {
            List<String> command = tokenizedScript.get(0);
            if (command.size() > 0 && command.get(0).startsWith("+")) {
                String releasedArg0 = String.format("-%s", command.get(0).substring(1));
                List<String> released = Stream.concat(Stream.of(releasedArg0), command.stream().skip(1)).collect(Collectors.toList());
                return new KeybindingScript.Releasing(script, Collections.singletonList(command), Collections.singletonList(released));
            }
        }
        return new KeybindingScript.Simple(script, tokenizedScript);
    }

    class Simple implements KeybindingScript {

        private final String script;
        private final List<List<String>> pressed;

        Simple(String script, List<List<String>> pressed) {
            this.script = script;
            this.pressed = pressed;
        }

        @Override
        @NotNull
        public String script() {
            return script;
        }

        @Override
        @NotNull
        public List<List<String>> pressed() {
            return pressed;
        }

        @Override
        public List<List<String>> released() {
            return null;
        }

        @Override
        public boolean repeatEvents() {
            return true;
        }

    }

    class Releasing implements KeybindingScript {

        private final String script;
        private final List<List<String>> pressed;
        private final List<List<String>> released;

        Releasing(String script, List<List<String>> pressed, List<List<String>> released) {
            this.script = script;
            this.pressed = pressed;
            this.released = released;
        }

        @Override
        @NotNull
        public String script() {
            return script;
        }

        @Override
        @NotNull
        public List<List<String>> pressed() {
            return pressed;
        }

        @Override
        @NotNull
        public List<List<String>> released() {
            return released;
        }

        @Override
        public boolean repeatEvents() {
            return false;
        }
    }

}
