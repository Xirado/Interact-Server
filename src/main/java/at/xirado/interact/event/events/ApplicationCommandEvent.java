package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.dv8tion.jda.internal.interactions.CommandInteractionImpl;
import net.dv8tion.jda.internal.interactions.InteractionHookImpl;
import net.dv8tion.jda.internal.requests.restaction.interactions.ReplyActionImpl;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ApplicationCommandEvent extends InteractionEvent
{
    private InteractionHookImpl hook;

    public ApplicationCommandEvent(Interact interact, CommandInteractionImpl interaction)
    {
        super(interact, interaction);
        hook = (InteractionHookImpl) interaction.getHook();
    }

    public ReplyAction deferReply(boolean ephemeral)
    {
        return new ReplyActionImpl(hook) {

            public void send()
            {
                respond(toData());
                hook.ack();
                hook.ready();
            }

            @Override
            public void queue()
            {
                respond(toData());
                hook.ack();
                hook.ready();
            }

            @Override
            public void queue(@Nullable Consumer<? super InteractionHook> success)
            {
                respond(toData());
                hook.ack();
                hook.ready();
            }

            @Override
            public void queue(Consumer<? super InteractionHook> success, Consumer<? super Throwable> failure)
            {
                respond(toData());
                hook.ack();
                hook.ready();
            }

            @Override
            public InteractionHook complete()
            {
                respond(toData());
                hook.ack();
                hook.ready();
                return hook;
            }
        }.setEphemeral(ephemeral);
    }

    public ReplyAction deferReply()
    {
        return deferReply(false);
    }

    public ReplyAction reply(String message)
    {
        return new ReplyActionImpl(hook) {

            public void send()
            {
                respond(toData());
                hook.ack();
                hook.ready();
            }

            @Override
            public void queue()
            {
                respond(toData());
                hook.ack();
                hook.ready();
            }

            @Override
            public void queue(@Nullable Consumer<? super InteractionHook> success)
            {
                respond(toData());
                hook.ack();
                hook.ready();
            }

            @Override
            public void queue(Consumer<? super InteractionHook> success, Consumer<? super Throwable> failure)
            {
                respond(toData());
                hook.ack();
                hook.ready();
            }

            @Override
            public InteractionHook complete()
            {
                respond(toData());
                hook.ack();
                hook.ready();
                return hook;
            }
        }.setContent(message);
    }
}
