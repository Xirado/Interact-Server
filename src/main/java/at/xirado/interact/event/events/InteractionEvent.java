package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

public class InteractionEvent extends Event
{

    private final Interact interact;
    private final DataObject interaction;

    private DataObject response = null;

    public InteractionEvent(Interact interact, DataObject interaction)
    {
        super(interact);
        this.interact = interact;
        this.interaction = interaction;
    }

    protected synchronized void respond(DataObject response)
    {
        System.out.println("Responding!");
        if (response != null)
            throw new IllegalStateException("You already replied to this interaction!");
        this.response = response;
    }

    public synchronized DataObject getResponse()
    {
        return response;
    }

    @Override
    public @NotNull Interact getInteract()
    {
        return interact;
    }
}
