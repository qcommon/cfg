package net.dblsaiko.qcommon.cfg.base.client.rcon;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.StringConVar;
import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;
import net.dblsaiko.qcommon.cfg.core.api.ref.Ref;

public class RconClient {

    // rcon c->s packet format
    // i32 -> following data length
    // i32 -> message id
    // i32 -> message type (2: exec command, 3: auth req)
    // str -> message content
    // 0x00

    // rcon s->c packet format
    // i32 -> following data length
    // i32 -> message id / -1 for auth fail
    // i32 -> message type (0: command/error response, 2: auth)
    // str -> message content
    // 0x00
    // 0x00

    private String rconAddr = "";
    private int rconPort = 25575;
    private String rconPassword = "";

    public void initialize(ConfigApi.Mutable api) {
        api.addConVar("rcon_address", StringConVar.wrap(Ref.from(() -> rconAddr, this::setRconAddr), ""));
        api.addConVar("rcon_port", IntConVar.wrap(IntRef.from(() -> rconPort, this::setRconPort), 25575, IntConVar.Options.create().min(0).max(65535)));
        api.addConVar("rcon_password", StringConVar.wrap(Ref.from(() -> rconPassword, this::setRconPassword), ""));
        api.addCommand("rcon", (args, src, output, cf) -> sendMessage(args, output));
        api.addCommand("rcon_reset", (args, src, output, cf) -> invalidate());
    }

    private void sendMessage(String[] args, LinePrinter output) {
        output.print("todo");
    }

    private void invalidate() {

    }

    private void setRconAddr(String rconAddr) {
        this.rconAddr = rconAddr;
        invalidate();
    }

    private void setRconPort(int rconPort) {
        this.rconPort = rconPort;
        invalidate();
    }

    private void setRconPassword(String rconPassword) {
        this.rconPassword = rconPassword;
        invalidate();
    }

}
