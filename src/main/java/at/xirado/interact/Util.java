package at.xirado.interact;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

public class Util
{
    private static final Logger log = LoggerFactory.getLogger(Interact.class);

    public static byte[] hexToBytes(String hexString)
    {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public static ThreadFactory newThreadFactory()
    {
        return new ThreadFactoryBuilder().setNameFormat("Interact-Thread %d")
                .setUncaughtExceptionHandler((t, e) -> log.error("An uncaught exception was encountered", e))
                .build();
    }

    public static Guild getGuild(JDA jda, long id)
    {
        if (jda.getShardManager() != null)
            return jda.getShardManager().getGuildById(id);
        return jda.getGuildById(id);
    }
}
