package at.xirado.interact.io;

import at.xirado.interact.Interact;
import at.xirado.interact.event.events.ReadyEvent;
import at.xirado.interact.io.routes.InteractionRoute;
import io.javalin.Javalin;

public class WebServer
{
    private final Interact interact;
    private final String host;
    private final int port;

    private final Javalin app;


    public WebServer(Interact interact, String host, int port)
    {
        this.interact = interact;
        this.host = host;
        this.port = port;
        app = Javalin.create()
                .events(event -> {
                    event.serverStarted(() -> interact.handleEvent(new ReadyEvent(interact)));
                })
                .start(host, port);
        app.post("/interaction", new InteractionRoute(interact));
    }
}
