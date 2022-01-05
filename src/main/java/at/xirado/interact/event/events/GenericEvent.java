package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import org.jetbrains.annotations.NotNull;

public interface GenericEvent
{
    @NotNull
    Interact getInteract();
}
