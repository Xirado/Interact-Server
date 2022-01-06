package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.InteractionImpl;

public class GenericInteractionEvent extends InteractionEvent
{
    public GenericInteractionEvent(Interact interact, InteractionImpl interaction)
    {
        super(interact, interaction);
    }
}
