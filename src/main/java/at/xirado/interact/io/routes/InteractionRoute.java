package at.xirado.interact.io.routes;

import at.xirado.interact.Interact;
import at.xirado.interact.Util;
import at.xirado.interact.event.events.InteractionCreateEvent;
import com.iwebpp.crypto.TweetNaclFast;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class InteractionRoute implements Handler
{
    private static final Logger log = LoggerFactory.getLogger(Interact.class);

    private final Interact interact;
    private final byte[] publicKey;

    public InteractionRoute(Interact interact)
    {
        this.interact = interact;
        publicKey = Util.hexToBytes(interact.getPublicKey());
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception
    {
        String signature = ctx.header("X-Signature-Ed25519");
        String timestamp = ctx.header("X-Signature-Timestamp");
        if (signature == null || timestamp == null)
        {
            DataObject result = DataObject.empty()
                    .put("code", 401)
                    .put("message", "Unauthorized");
            ctx.status(401).json(result);
            return;
        }
        String bodyString = ctx.body();
        if (!verify(bodyString, signature, timestamp))
        {
            DataObject result = DataObject.empty()
                    .put("code", 401)
                    .put("message", "Unauthorized");
            ctx.status(401).json(result);
            return;
        }
        DataObject body = DataObject.fromJson(bodyString);
        if (body.isNull("type"))
        {
            DataObject result = DataObject.empty()
                    .put("code", 400)
                    .put("message", "Bad Request");
            ctx.status(400).json(result);
            return;
        }
        //interact.handleEvent(new InteractionCreateEvent(interact, body, ctx));
        int code = body.getInt("type");
        switch(code)
        {
            case 1 -> handlePing(ctx);
            case 2 -> ctx.future(handleApplicationCommand(body));
            case 3 -> handleComponentInteraction(ctx);
            case 4 -> handleAutocompleteInteraction(ctx);
            default -> log.debug("Received unhandled interaction type {}", code);
        }
    }

    public void handlePing(Context ctx)
    {
        log.debug("Received PING interaction");
        ctx.json(DataObject.empty().put("type", 1));
    }

    public CompletableFuture<String> handleApplicationCommand(DataObject payload)
    {
        InteractionCreateEvent event = new InteractionCreateEvent(interact, payload);
        interact.handleEvent(event);
        return event.getResponse();
    }

    public void handleComponentInteraction(Context ctx)
    {

    }

    public void handleAutocompleteInteraction(Context ctx)
    {

    }

    private boolean verify(String body, String signature, String timestamp)
    {
        TweetNaclFast.Signature verifier = new TweetNaclFast.Signature(publicKey, null);
        return verifier.detached_verify((timestamp + body).getBytes(StandardCharsets.UTF_8), Util.hexToBytes(signature));
    }
}
