package io.github.hello09x.devtools.core.utils;

import org.bukkit.Bukkit;

/**
 * @author tanyaofei
 * @since 2024/8/5
 **/
public class ServerUtils {

    private final static boolean IS_FOLIA = Bukkit.getServer().getName().equals("Folia");

    public static boolean isFolia() {
        return IS_FOLIA;
    }

}
