package at.xirado.interact;

import at.xirado.interact.event.events.Event;
import at.xirado.interact.event.EventListener;
import at.xirado.interact.io.WebServer;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Interact
{
    private final String publicKey;
    private final String host;
    private final int port;
    private final WebServer webServer;
    private final List<EventListener> registeredListeners;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args)
    {
        System.out.println("You cannot run this directly!");
    }

    private Interact(String publicKey, String host, int port, List<EventListener> listeners)
    {
        this.publicKey = publicKey;
        this.host = host;
        this.port = port;
        this.registeredListeners = listeners;
        webServer = new WebServer(this, host, port);
    }

    public void handleEvent(Event event)
    {
        registeredListeners.forEach(x -> x.onEvent(event));
    }

    public ExecutorService getExecutor()
    {
        return executor;
    }

    public String getPublicKey()
    {
        return publicKey;
    }

    public static class Builder
    {
        private final String publicKey;

        private String host = "0.0.0.0";
        private int port = 8080;
        private final List<EventListener> registeredListeners = new ArrayList<>();

        private Builder(String publicKey)
        {
            this.publicKey = publicKey;
        }

        public Builder setPort(int port)
        {
            this.port = port;
            return this;
        }

        public Builder addEventListeners(EventListener event, EventListener... events)
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
}
