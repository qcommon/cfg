package net.dblsaiko.qcommon.cfg.ui.gui.widget;

import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Supplier;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;

public class ExecButtonWidget extends AbstractPressableButtonWidget {

    private final Supplier<String> scriptSupplier;

    public ExecButtonWidget(int x, int y, int width, int height, Text text, Supplier<String> scriptSupplier) {
        super(x, y, width, height, text);
        this.scriptSupplier = scriptSupplier;
    }

    public ExecButtonWidget(int x, int y, int width, int height, Text text, String script) {
        this(x, y, width, height, text, () -> script);
    }

    @Override
    public void onPress() {
        ConfigApi.getInstance().exec(scriptSupplier.get(), ExecSource.UI);
    }

}
