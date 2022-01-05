package at.xirado.interact.entities;


import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

public interface Interaction
{
    int getTypeRaw();

    default InteractionType getType()
    {
        return InteractionType.fromKey(getTypeRaw());
    }

    @NotNull
    DataObject getPayload();

    @NotNull
    Interact getInteract();

}
