package at.xirado.interact.io;

import at.xirado.interact.Interact;
import at.xirado.interact.event.events.ReadyEvent;
import at.xirado.interact.io.routes.InteractionRoute;

import static spark.Spark.*;

public class WebServer
{
    private final Interact interact;
    private final String host;
    private final int port;

    public WebServer(Interact interact, String host, int port)
    {
        this.interact = interact;
        this.host = host;
        this.port = port;
        ipAddress(host);
        port(port);
        post("/interaction", new InteractionRoute(interact));
        interact.handleEvent(new ReadyEvent(interact));
    }
}
