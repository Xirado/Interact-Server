package at.xirado.interact.io.routes;

import at.xirado.interact.Interact;
import at.xirado.interact.Util;
import at.xirado.interact.event.events.*;
import com.iwebpp.crypto.TweetNaclFast;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.InteractionType;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.interactions.ButtonInteractionImpl;
import net.dv8tion.jda.internal.interactions.CommandInteractionImpl;
import net.dv8tion.jda.internal.interactions.InteractionImpl;
import net.dv8tion.jda.internal.interactions.SelectionMenuInteractionImpl;
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
        log.trace("[INTERACTION CREATE] Received interaction: "+ body);

        if (body.isNull("type"))
        {
            DataObject result = DataObject.empty()
                    .put("code", 400)
                    .put("message", "Bad Request");
            response.status(400);
            log.trace("[INTERACTION CREATE] Payload missing \"type\" field! Returning 400.");
            return result.toString();
        }

        int type = body.getInt("type");

        if (type == 1)
        {
            log.debug("[INTERACTION CREATE] Received ping.");
            return DataObject.empty().put("type", 1).toString(); // Pong
        }

        boolean isFromGuild = !body.isNull("guild_id");
        JDAImpl jda = (JDAImpl) interact.getJDA();
        if (isFromGuild)
        {
            Guild guild = Util.getGuild(jda, body.getLong("guild_id"));
            if (guild == null)
            {
                log.warn("[INTERACTION CREATE] Received interaction from a guild that JDA has not yet loaded!");
                response.status(404);
                return DataObject.empty().put("code", 404).put("message", "This guild is not loaded yet").toString();
            }
        }

        InteractionEvent event;
        switch (InteractionType.fromKey(type)) {
            case SLASH_COMMAND -> event = new ApplicationCommandEvent(interact, new CommandInteractionImpl(jda, body));
            case COMPONENT -> {
                int componentType = body.getObject("data").getInt("component_type");
                if (componentType == 2)
                    event = new ButtonClickEvent(interact, new ButtonInteractionImpl(jda, body));
                else if (componentType == 3)
                    event = new SelectionMenuEvent(interact, new SelectionMenuInteractionImpl(jda, body));
                else
                    event = new GenericComponentInteractionEvent(interact, new InteractionImpl(jda, body));
            }
            case AUTOCOMPLETE -> event = new ApplicationCommandAutocompleteEvent(interact, new InteractionImpl(jda, body));
            default -> event = new GenericInteractionEvent(interact, new InteractionImpl(jda, body));
        }
        interact.handleEvent(event);

        log.trace("[INTERACTION CREATE] Waiting on acknowledgement...");
        while (!event.hasResult())
        {
            if (System.currentTimeMillis() > startTime + 3000)
                break;
            Thread.sleep(20);
        }

        if (!event.hasResult())
        {
            log.warn("[INTERACTION CREATE] Did not acknowledge in time! Returning 408.");
            response.status(408);
            return DataObject.empty().put("code", 408).put("message", "Request timed out").toString();
        }
        log.trace("[INTERACTION CREATE] Response payload: " + event.getResponse());
        return event.getResponse().toString();
    }

    private boolean verify(String body, String signature, String timestamp)
    {
        TweetNaclFast.Signature verifier = new TweetNaclFast.Signature(publicKey, null);
        return verifier.detached_verify((timestamp + body).getBytes(StandardCharsets.UTF_8), Util.hexToBytes(signature));
    }
}
