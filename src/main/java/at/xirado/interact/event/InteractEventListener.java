package at.xirado.interact.event;

import at.xirado.interact.event.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

public interface InteractEventListener
{

    void onEvent(@NotNull GenericEvent event);

}
