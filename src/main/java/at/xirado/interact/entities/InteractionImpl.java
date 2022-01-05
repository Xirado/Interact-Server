package at.xirado.interact.entities;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

public class InteractionImpl implements Interaction
{
    private final Interact interact;
    private final DataObject payload;
    private final int type;

    public InteractionImpl(Interact interact, DataObject payload)
    {
        this.interact = interact;
        this.payload = payload;
        this.type = payload.getInt("type");
    }

    @Override
    public int getTypeRaw()
    {
        return type;
    }

    @NotNull
    @Override
    public DataObject getPayload()
    {
        return payload;
    }

    @NotNull
    @Override
    public Interact getInteract()
    {
        return interact;
    }
}
