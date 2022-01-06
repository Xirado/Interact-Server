package at.xirado.interact;

import at.xirado.interact.event.events.Event;
import at.xirado.interact.event.InteractEventListener;
import at.xirado.interact.event.events.ReadyEvent;
import at.xirado.interact.io.WebServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Interact implements EventListener
{
    private static final Logger log = LoggerFactory.getLogger(Interact.class);

    private final String publicKey;
    private final String host;
    private final int port;
    private final WebServer webServer;
    private final List<InteractEventListener> registeredListeners;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), Util.newThreadFactory());
    private boolean started = false;
    private JDA jda = null;

    private Interact(String publicKey, String host, int port, List<InteractEventListener> listeners)
    {
        this.publicKey = publicKey;
        this.host = host;
        this.port = port;
        this.registeredListeners = listeners;
        webServer = new WebServer(this, host, port);
    }

    @Override
    public void onEvent(@NotNull GenericEvent event)
    {
        if (event instanceof GuildReadyEvent guildReadyEvent)
        {
            if (started)
                return;
            this.jda = guildReadyEvent.getJDA();
            webServer.start();
            handleEvent(new ReadyEvent(this));
            log.info("Interact is ready!");
            return;
        }

        if (event instanceof ShutdownEvent)
        {
            executor.shutdown();
            log.info("Interact shutting down...");
        }
    }

    public void handleEvent(Event event)
    {
        executor.execute(() -> registeredListeners.forEach(x -> x.onEvent(event)));
    }

    public ExecutorService getExecutor()
    {
        return executor;
    }

    public String getPublicKey()
    {
        return publicKey;
    }

    public JDA getJDA()
    {
        return jda;
    }

    public static class Builder
    {
        private final String publicKey;

        private String host = "0.0.0.0";
        private int port = 8080;
        private final List<InteractEventListener> registeredListeners = new ArrayList<>();

        private Builder(String publicKey)
        {
            this.publicKey = publicKey;
        }

        public Builder setPort(int port)
        {
            this.port = port;
            return this;
        }

        public Builder addEventListeners(InteractEventListener event, InteractEventListener... events)
        {
            Checks.notNull(event, "Event");
            Checks.noneNull(events, "Events");
            registeredListeners.add(event);
            Collections.addAll(registeredListeners, events);
            return this;
        }

        public Builder setHost(String host)
        {
            this.host = host;
            return this;
        }

        public Interact build()
        {
            return new Interact(publicKey, host, port, registeredListeners);
        }

        public static Builder create(@NotNull String publicKey)
        {
            return new Builder(publicKey);
        }

        public static Builder create(@NotNull String publicKey, int port)
        {
            return new Builder(publicKey).setPort(port);
        }
    }

    public static Logger getLog()
    {
        return log;
    }
}
