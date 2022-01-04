package at.xirado.interact;

import at.xirado.interact.io.WebServer;
import org.jetbrains.annotations.NotNull;

public class Interact
{
    private final String publicKey;
    private final String host;
    private final int port;
    private final WebServer webServer;

    public Interact(String publicKey, String host, int port)
    {
        this.publicKey = publicKey;
        this.host = host;
        this.port = port;
        webServer = new WebServer(this, host, port);
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

        private Builder(String publicKey)
        {
            this.publicKey = publicKey;
        }

        public Builder setPort(int port)
        {
            this.port = port;
            return this;
        }

        public Builder setHost(String host)
        {
            this.host = host;
            return this;
        }

        public Interact build()
        {
            return new Interact(publicKey, host, port);
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
