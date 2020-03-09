package net.dblsaiko.qcommon.cfg.keys;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.keys.ext.KeyBindingExt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VanillaKeyWrapper {

    public static final VanillaKeyWrapper INSTANCE = new VanillaKeyWrapper();

    private VanillaKeyWrapper() {
    }

    public void register(ConfigApi.Mutable api) {
        api.addCommand("kw_press", (args, src, output, cf) -> {
            if (args.length == 1) {
                KeyBinding kb = findBinding(args[0]);
                if (kb == null) return;
                KeyBindingExt.from(kb).incrTimesPressed();
            }
        });
        api.addCommand("+kw_hold", (args, src, output, cf) -> {
            if (args.length == 1) {
                KeyBinding kb = findBinding(args[0]);
                if (kb == null) return;
                KeyBindingExt.from(kb).incrTimesPressed();
                kb.setPressed(true);
            }
        });
        api.addCommand("-kw_hold", (args, src, output, cf) -> {
            if (args.length == 1) {
                KeyBinding kb = findBinding(args[0]);
                if (kb == null) return;
                kb.setPressed(false);
            }
        });
        api.addCommand("kw_list", (args, src, output, cf) -> {
            output.print(
                Arrays.stream(MinecraftClient.getInstance().options.keysAll)
                    .map($ -> $.getId().substring(4))
                    .collect(Collectors.joining("', '", "'", "'"))
            );
        });
    }

    public void registerWrapCommands(ConfigApi.Mutable api) {
        MinecraftClient client = MinecraftClient.getInstance();
        GameOptions opts = client.options;
        wrapHold(api, "walk", opts.keyForward);
        wrapHold(api, "left", opts.keyLeft);
        wrapHold(api, "back", opts.keyBack);
        wrapHold(api, "right", opts.keyRight);
        wrapHold(api, "jump", opts.keyJump);
        wrapHold(api, "crouch", opts.keySneak);
        wrapHold(api, "run_on", opts.keySprint);
        wrap(api, "open_inv", opts.keyInventory);
        wrap(api, "swap_hands", opts.keySwapHands);
        wrap(api, "drop_item", opts.keyDrop);
        wrapHold(api, "use", opts.keyUse);
        wrapHold(api, "attack", opts.keyAttack);
        wrap(api, "pick_item", opts.keyPickItem);
        wrapHold(api, "show_player_list", opts.keyPlayerList);
        wrap(api, "screenshot", opts.keyScreenshot);
        wrap(api, "toggle_perspective", opts.keyTogglePerspective);
        wrap(api, "toggle_cinematic_cam", opts.keySmoothCamera);
        wrap(api, "toggle_spectator_outline", opts.keySpectatorOutlines);
        wrap(api, "open_advancements", opts.keyAdvancements);

        for (int i = 0; i < opts.keysHotbar.length; i++) {
            int i1 = i;

            wrap(api, String.format("slot%d", i + 1), opts.keysHotbar[i]);

            api.addCommand(String.format("save_toolbar_%d", i + 1), (args, src, output, cf) -> {
                if (client.player != null && client.player.isCreative())
                    CreativeInventoryScreen.onHotbarKeyPress(client, i1, false, true);
            });

            api.addCommand(String.format("load_toolbar_%d", i + 1), (args, src, output, cf) -> {
                if (client.player != null && client.player.isCreative())
                    CreativeInventoryScreen.onHotbarKeyPress(client, i1, true, false);
            });
        }

        api.addCommand("menu", (args, src, output, cf) -> client.openPauseMenu(false));
        api.addCommand("pause", (args, src, output, cf) -> client.openPauseMenu(true));
        api.addCommand("open_chat", (args, src, output, cf) -> client.openScreen(new ChatScreen(String.join(" ", args))));
        api.addCommand("say", (args, src, output, cf) -> {
            if (client.player != null) client.player.sendChatMessage(String.join(" ", args));
        });
        api.addCommand("echo_chat", (args, src, output, cf) -> {
            if (client.player != null) client.player.addChatMessage(new LiteralText(String.join(" ", args)), false);
        });
        api.addCommand("info", (args, src, output, cf) -> {
            if (client.player != null) client.player.addChatMessage(new LiteralText(String.join(" ", args)), true);
        });
        api.addCommand("next_slot", (args, src, output, cf) -> scroll(-1));
        api.addCommand("prev_slot", (args, src, output, cf) -> scroll(1));
        api.addCommand("clear_chat", (args, src, output, cf) -> client.inGameHud.getChatHud().clear(false));
    }

    private void scroll(int i) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.player.isSpectator()) {
            if (client.inGameHud.getSpectatorHud().isOpen()) {
                client.inGameHud.getSpectatorHud().cycleSlot((-i));
            } else {
                float j = MathHelper.clamp(client.player.abilities.getFlySpeed() + i * 0.005f, 0.0f, 0.2f);
                client.player.abilities.setFlySpeed(j);
            }
        } else {
            client.player.inventory.scrollInHotbar(i);
        }
    }

    private void wrap(ConfigApi.Mutable api, String name, KeyBinding kb) {
        KeyBindingExt ext = KeyBindingExt.from(kb);
        api.addCommand(name, (args, src, output, cf) -> ext.incrTimesPressed());
    }

    private void wrapHold(ConfigApi.Mutable api, String name, KeyBinding kb) {
        KeyBindingExt ext = KeyBindingExt.from(kb);
        api.addCommand(String.format("+%s", name), (args, src, output, cf) -> {
            ext.incrTimesPressed();
            kb.setPressed(true);
        });
        api.addCommand(String.format("-%s", name), (args, src, output, cf) -> kb.setPressed(false));
    }

    private KeyBinding findBinding(String name) {
        List<KeyBinding> list = Arrays.stream(MinecraftClient.getInstance().options.keysAll)
            .filter($ -> $.getId().equals(String.format("key.%s", name)))
            .collect(Collectors.toList());
        return list.size() == 1 ? list.get(0) : null;
    }

}
