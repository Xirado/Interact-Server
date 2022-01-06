package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.ComponentInteractionImpl;
import net.dv8tion.jda.internal.interactions.InteractionImpl;

public class GenericComponentInteractionEvent extends InteractionEvent
{
    public GenericComponentInteractionEvent(Interact interact, InteractionImpl interaction)
    {
        super(interact, interaction);
    }
}
