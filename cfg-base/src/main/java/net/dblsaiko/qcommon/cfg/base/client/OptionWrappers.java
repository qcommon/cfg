package net.dblsaiko.qcommon.cfg.base.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.AoMode;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.option.Option;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.util.Arm;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntSupplier;

import net.dblsaiko.qcommon.cfg.base.ext.CyclingOptionExt;
import net.dblsaiko.qcommon.cfg.base.ext.DoubleOptionExt;
import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.cvar.BoolConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.FloatConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar;
import net.dblsaiko.qcommon.cfg.core.api.ref.BoolRef;
import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef;
import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;

public class OptionWrappers {

    public static void register(ConfigApi.Mutable api) {
        MinecraftClient mc = MinecraftClient.getInstance();
        GameOptions d = new GameOptions(mc, new File("dummyfile-" + UUID.randomUUID()));
        wrapOption("r_biomeblendradius", Option.BIOME_BLEND_RADIUS, api, mc, d);
        wrapOption("cl_chat_height", Option.CHAT_HEIGHT_FOCUSED, api, mc, d);
        wrapOption("cl_chat_height_unfocused", Option.SATURATION, api, mc, d);
        wrapOption("cl_chat_opacity", Option.CHAT_OPACITY, api, mc, d);
        wrapOption("cl_chat_scale", Option.CHAT_SCALE, api, mc, d);
        wrapOption("cl_chat_width", Option.CHAT_WIDTH, api, mc, d);
        wrapOption("r_fov", Option.FOV, api, mc, d);
        wrapOption("r_fpslimit", Option.FRAMERATE_LIMIT, api, mc, d);
        wrapOption("r_gamma", Option.GAMMA, api, mc, d);
        wrapOption("r_mipmap", Option.MIPMAP_LEVELS, api, mc, d);
        wrapOption("cl_mwheelspeed", Option.MOUSE_WHEEL_SENSITIVITY, api, mc, d);
        wrapOption("cl_rawmouse", Option.RAW_MOUSE_INPUT, api, mc, d);
        wrapOption("r_chunkdist", Option.RENDER_DISTANCE, api, mc, d);
        wrapOption("cl_lookspeed", Option.SENSITIVITY, api, mc, d);
        wrapOption("cl_text_label_opacity", Option.TEXT_BACKGROUND_OPACITY, api, mc, d);
        wrapOption("r_ao", Option.AO, AoMode.values(), $ -> $.ao, api, mc, d);
        wrapOption("cl_attack_indicator_mode", Option.ATTACK_INDICATOR, AttackIndicator.values(), $ -> $.attackIndicator, api, mc, d);
        wrapOption("r_detail", Option.GRAPHICS, GraphicsMode.values(), $ -> $.graphicsMode, api, mc, d);
        api.addConVar("cl_gui_scale", IntConVar.wrap(IntRef.from(() -> mc.options.guiScale, value -> mc.options.guiScale = value), 0, IntConVar.Options.create().min(0)));
        api.addConVar("cl_main_hand", IntConVar.wrap(IntRef.from(() -> mc.options.mainArm.ordinal(), value -> mc.options.mainArm = Arm.values()[value % 2]), 1, IntConVar.Options.create().min(0).max(1)));
        if (NarratorManager.INSTANCE.isActive()) {
            wrapOption("cl_narrator", Option.NARRATOR, NarratorMode.values(), $ -> $.narrator, api, mc, d);
        }
        wrapOption("cl_particle_detail", Option.PARTICLES, ParticlesMode.values(), $ -> $.particles, api, mc, d);
        wrapOption("cl_clouds_detail", Option.CLOUDS, CloudRenderMode.values(), $ -> $.cloudRenderMode, api, mc, d);
        api.addConVar("cl_labels_only_chat", BoolConVar.wrap(BoolRef.from(() -> mc.options.backgroundForChatOnly, value -> mc.options.backgroundForChatOnly = value), true));
        wrapOption("cl_autojump", Option.AUTO_JUMP, api, mc, d);
        wrapOption("cl_cmd_autosuggestions", Option.AUTO_SUGGESTIONS, api, mc, d);
        wrapOption("cl_chat_colors", Option.CHAT_COLOR, api, mc, d);
        wrapOption("cl_chat_links", Option.CHAT_LINKS, api, mc, d);
        wrapOption("cl_chat_link_prompt", Option.CHAT_LINKS_PROMPT, api, mc, d);
        wrapOption("cl_scroll_limit", Option.DISCRETE_MOUSE_SCROLL, api, mc, d);
        wrapOption("r_vsync", Option.VSYNC, api, mc, d);
        wrapOption("r_entityshadows", Option.ENTITY_SHADOWS, api, mc, d);
        wrapOption("cl_always_use_unicode_font", Option.FORCE_UNICODE_FONT, api, mc, d);
        wrapOption("cl_invert_y", Option.INVERT_MOUSE, api, mc, d);
        wrapOption("cl_show_realms_notifications", Option.REALMS_NOTIFICATIONS, api, mc, d);
        wrapOption("cl_reduced_debug_info", Option.REDUCED_DEBUG_INFO, api, mc, d);
        wrapOption("cl_subtitles", Option.SUBTITLES, api, mc, d);
        api.addConVar("cl_sneak_toggle", BoolConVar.wrap(BoolRef.from(() -> mc.options.sneakToggled, value -> mc.options.sneakToggled = value), false));
        api.addConVar("cl_sprint_toggle", BoolConVar.wrap(BoolRef.from(() -> mc.options.sprintToggled, value -> mc.options.sprintToggled = value), false));
        wrapOption("cl_touchscreen_mode", Option.TOUCHSCREEN, api, mc, d);
        wrapOption("r_viewbob", Option.VIEW_BOBBING, api, mc, d);
    }

    private static void wrapOption(String name, DoubleOption option, ConfigApi.Mutable api, MinecraftClient mc, GameOptions d) {
        FloatConVar.Options options = FloatConVar.Options.create()
            .min((float) option.getMin())
            .max((float) option.getMax());

        double step = DoubleOptionExt.from(option).getStep();
        if (step > 0.0) {
            options = options.step((float) step);
        }

        api.addConVar(name, FloatConVar.wrap(FloatRef.from(() -> (float) option.get(mc.options), value -> option.set(mc.options, value)), (float) option.get(d), options));
    }

    private static void wrapOption(String name, CyclingOption<Boolean> option, ConfigApi.Mutable api, MinecraftClient mc, GameOptions d) {
        //api.addConVar(name, BoolConVar.wrap(BoolRef.from(() -> option.get(mc.options), value -> option.set(mc.options, value ? "true" : "false")), option.get(d)));
    }

    private static <T> void wrapOption(String name, CyclingOption<T> option, T[] values, Function<GameOptions, T> getter, ConfigApi.Mutable api, MinecraftClient mc, GameOptions d) {
        CyclingOptionExt<T> optionExt = CyclingOptionExt.from(option);

        IntConVar.Options options = IntConVar.Options.create()
            .min(0)
            .max(values.length - 1);

        IntSupplier getter1 = () -> Arrays.asList(values).indexOf(getter.apply(mc.options));

        api.addConVar(
            name,
            IntConVar.wrap(
                IntRef.from(
                    getter1,
                    value -> optionExt.getSetter().accept(mc.options, option, values[(getter1.getAsInt() + 1) % values.length])
                ),
                Arrays.asList(values).indexOf(getter.apply(d)),
                options
            )
        );
    }

}
