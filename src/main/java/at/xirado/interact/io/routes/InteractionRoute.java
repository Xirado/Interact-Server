package at.xirado.interact.io.routes;

import at.xirado.interact.Interact;
import at.xirado.interact.Util;
import at.xirado.interact.entities.InteractionType;
import at.xirado.interact.event.events.*;
import com.iwebpp.crypto.TweetNaclFast;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.nio.charset.StandardCharsets;

public class InteractionRoute implements Route
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
    public Object handle(Request request, Response response) throws Exception
    {
        long startTime = System.currentTimeMillis();
        String signature = request.headers("X-Signature-Ed25519");
        String timestamp = request.headers("X-Signature-Timestamp");
        String bodyString = request.body();
        response.header("content-type", "application/json");

        if (signature == null || timestamp == null || !verify(bodyString, signature, timestamp))
        {
            DataObject result = DataObject.empty()
                    .put("code", 401)
                    .put("message", "Unauthorized");
            response.status(401);
            return result.toString();
        }

        DataObject body = DataObject.fromJson(bodyString);

        if (body.isNull("type"))
        {
            DataObject result = DataObject.empty()
                    .put("code", 400)
                    .put("message", "Bad Request");
            response.status(400);
            return result.toString();
        }

        int type = body.getInt("type");

        if (type == 1)
            return DataObject.empty().put("type", 1).toString(); // Pong
        InteractionEvent event;
        System.out.println("Received interaction of type "+InteractionType.fromKey(type));
        switch (InteractionType.fromKey(type)) {
            case APPLICATION_COMMAND -> event = new ApplicationCommandEvent(interact, body);
            case MESSAGE_COMPONENT -> {
                int componentType = body.getObject("data").getInt("component_type");
                if (componentType == 2)
                    event = new ButtonClickEvent(interact, body);
                else if (componentType == 3)
                    event = new SelectionMenuEvent(interact, body);
                else
                    event = new GenericComponentInteractionEvent(interact, body);
            }
            case AUTOCOMPLETE -> event = new ApplicationCommandAutocompleteEvent(interact, body);
            default -> event = new GenericInteractionEvent(interact, body);
        }
        interact.handleEvent(event);
        while (!event.hasResult())
        {
            if (System.currentTimeMillis() > startTime + 3000)
                break;
            Thread.sleep(20);
        }

        if (!event.hasResult())
        {
            response.status(408);
            return DataObject.empty().put("code", 408).put("message", "Request timed out").toString();
        }
        System.out.println("Sending response: "+event.getResponse());
        return event.getResponse().toString();
    }

    private boolean verify(String body, String signature, String timestamp)
    {
        TweetNaclFast.Signature verifier = new TweetNaclFast.Signature(publicKey, null);
        return verifier.detached_verify((timestamp + body).getBytes(StandardCharsets.UTF_8), Util.hexToBytes(signature));
    }
}
