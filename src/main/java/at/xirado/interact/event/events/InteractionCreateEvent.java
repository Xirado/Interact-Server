package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import at.xirado.interact.entities.Interaction;
import at.xirado.interact.entities.InteractionImpl;
import at.xirado.interact.entities.InteractionType;
import io.javalin.http.Context;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class InteractionCreateEvent extends Event
{
    private final Interact interact;
    private final InteractionImpl interaction;
    private CompletableFuture<String> initialResponse = new CompletableFuture<>();

    public InteractionCreateEvent(Interact interact, DataObject payload)
    {
        super(interact);
        this.interact = interact;
        this.interaction = new InteractionImpl(interact, payload);
    }

    public void reply(DataObject object)
    {
        if (initialResponse.isDone())
            throw new IllegalStateException("You already replied to this interaction!");
        initialResponse.complete(object.toString());
    }

    public CompletableFuture<String> getInitialResponse()
    {
        return initialResponse;
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
