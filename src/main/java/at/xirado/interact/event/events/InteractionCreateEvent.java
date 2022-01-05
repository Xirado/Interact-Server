package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import at.xirado.interact.entities.Interaction;
import at.xirado.interact.entities.InteractionImpl;
import at.xirado.interact.entities.InteractionType;
import io.javalin.http.Context;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class InteractionCreateEvent extends Event
{
    private static final Logger log = LoggerFactory.getLogger(Interact.class);
    private static final DataObject TIMED_OUT = DataObject.empty()
            .put("code", 408)
            .put("message", "Request timed out");

    private final Interact interact;
    private final InteractionImpl interaction;
    private final Context context;
    private final CompletableFuture<String> response = new CompletableFuture<>();

    public InteractionCreateEvent(Interact interact, DataObject payload, Context context)
    {
        super(interact);
        this.interact = interact;
        this.interaction = new InteractionImpl(interact, payload);
        this.context = context;
        response.orTimeout(3, TimeUnit.SECONDS);
        response.whenComplete((s, err) -> {
            if (err instanceof TimeoutException)
            {
                log.warn("Unhandled interaction! Did you forget to reply() ?");
            }
        });
    }

    public void reply(DataObject object)
    {
        if (response.isDone())
            throw new IllegalStateException("You already replied to this interaction!");
        response.complete(object.toString());
    }

    public CompletableFuture<String> getResponse()
    {
        return response;
    }

    @NotNull
    public Interaction getInteraction()
    {
        return interaction;
    }

    @NotNull
    public InteractionType getInteractionType()
    {
        return interaction.getType();
    }

    @NotNull
    @Override
    public Interact getInteract()
    {
        return interact;
    }
}
