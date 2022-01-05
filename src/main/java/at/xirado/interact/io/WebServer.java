package at.xirado.interact.io;

import at.xirado.interact.Interact;
import at.xirado.interact.event.events.ReadyEvent;
import at.xirado.interact.io.routes.InteractionRoute;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.json.JsonMapper;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

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
        app = Javalin.create(this::getConfig)
                .events(event -> {
                    event.serverStarted(() -> interact.handleEvent(new ReadyEvent(interact)));
                })
                .start(host, port);
        app.post("/interaction", new InteractionRoute(interact));
    }


    private JavalinConfig getConfig(JavalinConfig config)
    {
        config.enforceSsl = true;
        config.defaultContentType = "application/json";
        config.jsonMapper(new JsonMapper()
        {
            @NotNull
            @Override
            public String toJsonString(@NotNull Object obj)
            {
                return obj.toString();
            }
        });
        return config;
    }

}
