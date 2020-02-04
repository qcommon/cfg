package net.dblsaiko.qcommon.cfg.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cmd.ControlFlow;
import org.jetbrains.annotations.NotNull;

public class ExecCommand implements Command {

    private final Path sourceDir;

    public ExecCommand(Path sourceDir) {
        this.sourceDir = sourceDir;
    }

    @Override
    public void exec(@NotNull String[] args, @NotNull ExecSource src, @NotNull LinePrinter output, @NotNull ControlFlow cf) {
        if (args.length > 0) {
            Path filePath = sourceDir.resolve(args[0] + ".cfg");
            if (filePath.startsWith(sourceDir)) {
                try {
                    String source = String.join("\n", Files.readAllLines(filePath));
                    cf.enterSubroutine(source);
                } catch (NoSuchFileException e) {
                    output.printf("File not found: %s.cfg", args[0]);
                } catch (IOException e) {
                    output.printf("Failed to open %s.cfg", args[0]);
                    e.printStackTrace();
                }
            }
        }
    }

}

