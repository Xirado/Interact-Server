package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.ButtonInteractionImpl;

public class ButtonClickEvent extends InteractionEvent
{
    public ButtonClickEvent(Interact interact, ButtonInteractionImpl interaction)
    {
        super(interact, interaction);
    }
}
