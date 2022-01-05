package at.xirado.interact.event;

import at.xirado.interact.event.events.Event;
import at.xirado.interact.event.events.GenericEvent;
import at.xirado.interact.event.events.InteractionCreateEvent;
import at.xirado.interact.event.events.ReadyEvent;
import net.dv8tion.jda.internal.utils.ClassWalker;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class ListenerAdapter implements EventListener
{
    public void onGenericEvent(@NotNull GenericEvent event) {}
    public void onReady(@NotNull ReadyEvent event) {}

    public void onInteractionCreate(@NotNull InteractionCreateEvent event) {}

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final ConcurrentMap<Class<?>, MethodHandle> methods = new ConcurrentHashMap<>();
    private static final Set<Class<?>> unresolved;
    static
    {
        unresolved = ConcurrentHashMap.newKeySet();
        Collections.addAll(unresolved,
                Object.class, // Objects aren't events
                Event.class, // onEvent is final and would never be found
                GenericEvent.class // onGenericEvent has already been called
        );
    }

    @Override
    public void onEvent(@NotNull GenericEvent event)
    {
        onGenericEvent(event);

        for (Class<?> clazz : ClassWalker.range(event.getClass(), GenericEvent.class))
        {
            MethodHandle mh = methods.computeIfAbsent(clazz, ListenerAdapter::findMethod);
            if (mh == null)
            {
                unresolved.add(clazz);
                continue;
            }

            try
            {
                mh.invoke(this, event);
            } catch (Throwable throwable)
            {
                if (throwable instanceof RuntimeException)
                    throw (RuntimeException) throwable;
                if (throwable instanceof Error)
                    throw (Error) throwable;
                throw new IllegalStateException(throwable);
            }
        }
    }

    private static MethodHandle findMethod(Class<?> clazz)
    {
        String name = clazz.getSimpleName();
        MethodType type = MethodType.methodType(Void.TYPE, clazz);
        try
        {
            name = "on" + name.substring(0, name.length() - "Event".length());
            return lookup.findVirtual(ListenerAdapter.class, name, type);
        } catch (NoSuchMethodException | IllegalAccessException ignored) {}
        return null;
    }
}
