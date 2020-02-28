package net.dblsaiko.qcommon.cfg.keys;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BindManager {

    private final Set<Key> pressedKeys = new HashSet<>();
    private final Map<Key, KeyCombo> pressedKeyCombos = new HashMap<>();
    private final Map<KeyCombo, String> availableCombos = new HashMap<>();

    public void onKeyPressed(Key key) {
        KeyCombo current = pressedKeyCombos.get(key);
        if (current != null) {
            current.getKeys().forEach(pressedKeyCombos::remove);
            onKeyComboReleased(current);
        }

        availableCombos.keySet().stream()
            .filter($ -> $.getKeys().stream().allMatch(k1 -> k1.equals(key) || pressedKeys.contains(k1)))
            .filter($ -> $.getKeys().stream().anyMatch(k1 -> k1.equals(key)))
            .findAny()
            .ifPresent(kc -> {
                kc.getKeys().forEach(k -> pressedKeyCombos.put(k, kc));
                onKeyComboPressed(kc);
            });

        pressedKeys.add(key);
    }

    public void onKeyReleased(Key key) {
        pressedKeys.remove(key);
        KeyCombo kc = pressedKeyCombos.get(key);
        if (kc != null) {
            kc.getKeys().forEach(pressedKeyCombos::remove);
            onKeyComboReleased(kc);
        }
    }

    public void bind(KeyCombo kc, String command) {
        unbind(kc);
        availableCombos.put(kc, command);
    }

    public void unbind(KeyCombo kc) {
        if (pressedKeyCombos.containsValue(kc)) {
            kc.getKeys().forEach(pressedKeyCombos::remove);
            onKeyComboReleased(kc);
        }
        availableCombos.remove(kc);
    }

    private void onKeyComboReleased(KeyCombo kc) {
        String command = availableCombos.get(kc);
        if (command == null) return;
        if (command.startsWith("+")) {
            // TODO use the tokenizer, this will break in many cases
            ConfigApi.getInstance().exec("-" + command.substring(1), ExecSource.KEY);
        }
    }

    private void onKeyComboPressed(KeyCombo kc) {
        String command = availableCombos.get(kc);
        if (command == null) return;
        ConfigApi.getInstance().exec(command, ExecSource.KEY);
    }

}
