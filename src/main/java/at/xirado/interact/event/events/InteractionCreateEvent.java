package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import at.xirado.interact.entities.Interaction;
import at.xirado.interact.entities.InteractionImpl;
import at.xirado.interact.entities.InteractionType;
import io.javalin.http.Context;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

public class InteractionCreateEvent extends Event
{
    private final Interact interact;
    private final InteractionImpl interaction;
    private final Context context;

    public InteractionCreateEvent(Interact interact, DataObject payload, Context context)
    {
        super(interact);
        this.interact = interact;
        this.interaction = new InteractionImpl(interact, payload);
        this.context = context;
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
    public Context getContext()
    {
        return context;
    }

    @NotNull
    @Override
    public Interact getInteract()
    {
        return interact;
    }
}
