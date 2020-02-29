package net.dblsaiko.qcommon.cfg.keys.binding;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceContext;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceListener;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BindManager implements PersistenceListener {

    public static final BindManager INSTANCE = new BindManager();

    private final Set<Key> pressedKeys = new HashSet<>();
    private final Map<Key, KeyCombo> pressedKeyCombos = new HashMap<>();
    private final Map<KeyCombo, KeybindingScript> availableCombos = new HashMap<>();

    private BindManager() {
    }

    public void onKeyPressed(Key key) {
        KeyCombo current = pressedKeyCombos.get(key);
        if (current != null) {
            current.getKeys().forEach(pressedKeyCombos::remove);
            onKeyComboReleased(current);
        }

        availableCombos.keySet().stream()
            .sorted(Comparator.comparingInt(kc -> -kc.getKeys().size()))
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

    public void onKeyRepeat(KeyboardKey key) {
        // TODO
    }

    public void bind(@NotNull KeyCombo kc, @NotNull KeybindingScript script) {
        unbind(kc);
        availableCombos.put(kc, script);
    }

    public void unbind(@NotNull KeyCombo kc) {
        if (pressedKeyCombos.containsValue(kc)) {
            kc.getKeys().forEach(pressedKeyCombos::remove);
            onKeyComboReleased(kc);
        }
        availableCombos.remove(kc);
    }

    public void clear() {
        while (!availableCombos.isEmpty()) {
            unbind(availableCombos.keySet().iterator().next());
        }
    }

    private void onKeyComboReleased(KeyCombo kc) {
        KeybindingScript sc = availableCombos.get(kc);
        List<List<String>> released = sc.released();
        if (released != null) {
            ConfigApi.getInstance().exec(released, ExecSource.KEY);
        }
    }

    private void onKeyComboPressed(KeyCombo kc) {
        KeybindingScript command = availableCombos.get(kc);
        if (command == null) return;
        ConfigApi.getInstance().exec(command.pressed(), ExecSource.KEY);
    }

    @Override
    public void write(PersistenceContext ctx) {
        ctx.write("user", lp -> {
            ConfigApi api = ConfigApi.getInstance();
            lp.print("unbindall");
            availableCombos.entrySet().stream().sorted(Comparator.comparing($ -> $.getKey().asString())).forEach(entry -> {
                KeyCombo kc = entry.getKey();
                KeybindingScript ks = entry.getValue();
                lp.print(String.format("bind %s %s", api.escape(kc.asString()), api.escape(ks.script())));
            });
        });
    }

    public void register(ConfigApi.Mutable api) {
        api.registerPersistenceListener(this);
        api.addCommand("unbindall", (args, src, output, cf) -> clear());
        api.addCommand("bind", (args, src, output, cf) -> {
            if (args.length == 1) {
                KeyCombo kc = KeyCombo.parse(args[0]);
                if (kc == null) {
                    output.printf("Invalid key combination '%s'", args[0]);
                    return;
                }
                KeybindingScript ks = availableCombos.get(kc);
                if (ks != null) {
                    output.printf("'%s' is bound to '%s'", kc.asString(), ks.script());
                } else {
                    output.printf("'%s' is not bound", kc.asString());
                }
            } else if (args.length == 2) {
                KeyCombo kc = KeyCombo.parse(args[0]);
                if (kc == null) return;
                KeybindingScript ks = KeybindingScript.parse(args[1]);
                bind(kc, ks);
            }
        });
        api.addCommand("unbind", (args, src, output, cf) -> {
            if (args.length == 1) {
                KeyCombo kc = KeyCombo.parse(args[0]);
                if (kc == null) {
                    output.printf("Invalid key combination '%s'", args[0]);
                    return;
                }
                unbind(kc);
            }
        });
    }
}
