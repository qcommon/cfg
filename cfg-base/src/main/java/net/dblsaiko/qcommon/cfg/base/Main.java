package net.dblsaiko.qcommon.cfg.base;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.fabricmc.api.ModInitializer;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigApi.Mutable api = ConfigApi.getInstanceMut();

        api.addCommand("list", (args, src, output, cf) -> {
            Predicate<String> matcher;
            if (args.length == 0) {
                matcher = _str -> true;
            } else {
                matcher = str -> Arrays.asList(args).contains(str);
            }
            ConfigApi api1 = ConfigApi.getInstance();
            List<Triple<String, String, String>> list = Stream.concat(api1.getCommands().keySet().stream(), api1.getConVars().keySet().stream())
                .filter(matcher)
                .sorted()
                .distinct()
                .map(cmd -> Triple.of(cmd, api1.getDescription(cmd), api1.getLongDescription(cmd)))
                .collect(Collectors.toList());
            if (list.size() == 0) {
                output.print("No results found");
            } else if (list.size() == 1) {
                Triple<String, String, String> element = list.get(0);
                output.printf("%s: %s\n%s", element.getLeft(), element.getMiddle(), element.getRight());
            } else {
                int limit = list.stream().mapToInt($ -> $.getLeft().length()).max().getAsInt();
                list.stream()
                    .map(entry -> String.format("%s | %s", padRight(entry.getLeft(), limit), entry.getMiddle()))
                    .forEach(output::print);
            }
        });
    }

    private static String padRight(String s, int length) {
        if (s.length() >= length) return s;
        return String.format("%s%s", s, String.join("", Collections.nCopies(length - s.length(), " ")));
    }

}
