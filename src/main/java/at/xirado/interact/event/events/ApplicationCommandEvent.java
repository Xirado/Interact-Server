package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;

public class ApplicationCommandEvent extends InteractionEvent
{
    public ApplicationCommandEvent(Interact interact, DataObject interaction)
    {
        super(interact, interaction);
    }

    public void reply(String message)
    {
        DataObject object = DataObject.empty()
                .put("type", 4)
                .put("data", DataObject.empty().put("content", message));
        System.out.println("Calling reply");
        respond(object);
    }
}
