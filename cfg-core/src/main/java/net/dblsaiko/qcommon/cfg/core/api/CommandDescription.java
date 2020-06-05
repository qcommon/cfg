package net.dblsaiko.qcommon.cfg.core.api;

import net.minecraft.util.Language;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

public interface CommandDescription {

    /**
     * Get the description this {@link CommandDescription} represents.
     *
     * @return the description text
     */
    String getValue(String commandName);

    /**
     * Get a literal command description which will not be translated.
     *
     * @param text the description text
     * @return the command description
     */
    static CommandDescription literal(@NotNull String text) {
        return _name -> text;
    }

    /**
     * Get a translated command description.
     *
     * @param translationKey the translation key
     * @return the command description
     */
    static CommandDescription translated(@NotNull String translationKey) {
        return _name -> {
            String s = Language.getInstance().get(translationKey);
            // this is on purpose! translate() will return the same object if
            // it could not find a translation.
            // noinspection StringEquality
            if (s == translationKey) s = "";
            return s;
        };
    }

    /**
     * Get a translated command description based on the command using it.
     *
     * @param keyGetter the function transforming the command name into a
     *                  translation key
     * @return the command description
     */
    static CommandDescription commandBased(@NotNull Function<String, String> keyGetter) {
        return name -> {
            String text = keyGetter.apply(name);
            String s = Language.getInstance().get(text);
            // this is on purpose! translate() will return the same object if
            // it could not find a translation.
            // noinspection StringEquality
            if (s == text) s = "";
            return s;
        };
    }

}
