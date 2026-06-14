package io.github.hello09x.devtools.core.utils;

import com.google.common.base.Throwables;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author tanyaofei
 * @since 2024/7/29
 **/
public class Exceptions {

    public static void suppress(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        suppress(plugin, runnable, true);
    }

    public static void suppress(@NotNull Plugin plugin, @NotNull Runnable runnable, boolean doLog) {
        try {
            runnable.run();
        } catch (Throwable e) {
            if (doLog) {
                plugin.getLogger().warning(Throwables.getStackTraceAsString(e));
            }
        }
    }

}
