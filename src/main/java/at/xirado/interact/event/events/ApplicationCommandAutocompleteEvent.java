package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;

public class ApplicationCommandAutocompleteEvent extends InteractionEvent
{
    public ApplicationCommandAutocompleteEvent(Interact interact, DataObject interaction)
    {
        super(interact, interaction);
    }
}