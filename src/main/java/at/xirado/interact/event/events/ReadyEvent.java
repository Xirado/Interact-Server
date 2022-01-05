package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import org.jetbrains.annotations.NotNull;

public class ReadyEvent extends Event
{
    private final Interact interact;

    public ReadyEvent(Interact interact)
    {
        super(interact);
        this.interact = interact;
    }

    @NotNull
    @Override
    public Interact getInteract()
    {
        return interact;
    }
}
