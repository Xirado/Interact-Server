package at.xirado.interact.entities;

import org.jetbrains.annotations.NotNull;

public enum InteractionType
{
    UNKNOWN(-1),
    PING(1),
    APPLICATION_COMMAND(2),
    MESSAGE_COMPONENT(3),
    AUTOCOMPLETE(4);

    private final int key;

    InteractionType(int key)
    {
        this.key = key;
    }

    public int getKey()
    {
        return key;
    }

    @NotNull
    public static InteractionType fromKey(int key)
    {
        return switch (key)
                {
                    case 1 -> PING;
                    case 2 -> APPLICATION_COMMAND;
                    case 3 -> MESSAGE_COMPONENT;
                    case 4 -> AUTOCOMPLETE;
                    default -> UNKNOWN;
                };
    }
}
