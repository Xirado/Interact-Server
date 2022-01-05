package at.xirado.interact.event.events;

import at.xirado.interact.Interact;

public abstract class Event implements GenericEvent
{
    private final Interact interact;

    public Event(Interact interact)
    {
        this.interact = interact;
    }
}
