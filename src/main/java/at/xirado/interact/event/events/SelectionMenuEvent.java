package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.SelectionMenuInteractionImpl;

public class SelectionMenuEvent extends InteractionEvent
{
    public SelectionMenuEvent(Interact interact, SelectionMenuInteractionImpl interaction)
    {
        super(interact, interaction);
    }
}
