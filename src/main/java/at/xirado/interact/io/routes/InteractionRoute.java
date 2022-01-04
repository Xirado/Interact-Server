package at.xirado.interact.io.routes;

import at.xirado.interact.Interact;
import at.xirado.interact.Util;
import com.iwebpp.crypto.TweetNaclFast;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

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
        System.out.println("Received request");
        String signature = ctx.header("X-Signature-Ed25519");
        String timestamp = ctx.header("X-Signature-Timestamp");
        String bodyString = ctx.body();
        if (!verify(bodyString, signature, timestamp))
        {
            System.out.println("Could not verify");
            DataObject result = DataObject.empty()
                    .put("code", 401)
                    .put("message", "Unauthorized");
            ctx.status(401).result(result.toString());
            return;
        }
        System.out.println("Verification success!");
        DataObject body = DataObject.fromJson(bodyString);
        if (body.isNull("type"))
        {
            DataObject result = DataObject.empty()
                    .put("code", 400)
                    .put("message", "Bad Request");
            ctx.status(400).result(result.toString());
            return;
        }

        int code = body.getInt("type");
        System.out.println("Got type "+code);
        switch(code)
        {
            case 1 -> handlePing(ctx);
            default -> log.debug("Received unhandled interaction type {}", code);
        }
    }

    public void handlePing(Context ctx)
    {
        System.out.println("Handling ping!");
        ctx.result(DataObject.empty().put("type", 1).toString());
    }

    private boolean verify(String body, String signature, String timestamp)
    {
        TweetNaclFast.Signature verifier = new TweetNaclFast.Signature(publicKey, null);
        return verifier.detached_verify((timestamp + body).getBytes(StandardCharsets.UTF_8), Util.hexToBytes(signature));
    }
}
