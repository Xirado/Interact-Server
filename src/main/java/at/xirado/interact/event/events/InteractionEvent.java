package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.InteractionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InteractionEvent extends Event
{

    private final Interact interact;
    private final Interaction interaction;

    private DataObject response = null;

    public InteractionEvent(Interact interact, Interaction interaction)
    {
        super(interact);
        this.interact = interact;
        this.interaction = interaction;
    }

    @Nullable
    public Guild getGuild()
    {
        return interaction.getGuild();
    }

    protected synchronized void respond(DataObject response)
    {
        if (this.response != null)
            throw new IllegalStateException("You already replied to this interaction!");
        this.response = response;
    }

    public synchronized boolean hasResult()
    {
        return response != null;
    }

    public synchronized DataObject getResponse()
    {
        return response;
    }

    public Interaction getInteraction()
    {
        return interaction;
    }

    @Override
    public @NotNull Interact getInteract()
    {
        return interact;
    }
}
