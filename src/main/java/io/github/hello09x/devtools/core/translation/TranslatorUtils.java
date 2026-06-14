package io.github.hello09x.devtools.core.translation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class TranslatorUtils {

    public static @NotNull ClassLoader getDataFolderClassLoader(@NotNull Plugin plugin) {
        URL url;
        try {
            url = plugin.getDataFolder().toURI().toURL();
        } catch (MalformedURLException e) {
            throw new UndeclaredThrowableException(e);
        }

        return new URLClassLoader(new URL[]{url});
    }

    public static @NotNull ClassLoader getJarClassLoader(@NotNull Plugin plugin) {
        return plugin.getClass().getClassLoader();
    }

    public static @Nullable Locale getLocale(@Nullable CommandSender sender) {
        if (sender instanceof Player player) {
            return player.locale();
        }
        return null;
    }

    public static @NotNull Locale getDefaultLocale(@NotNull Plugin plugin) {
        var config = plugin.getConfig();
        var locale = config.getString("locale");
        if (locale == null) {
            locale = config.getString("i18n.locale");
        }
        if (locale == null) {
            return Locale.getDefault();
        }

        var components = locale.split("_");
        if (components.length == 1) {
            return Locale.forLanguageTag(components[0]);
        } else if (components.length == 2) {
            return new Locale(components[0], components[1]);
        } else if (components.length == 3) {
            return new Locale(components[0], components[1], components[2]);
        }

        throw new IllegalArgumentException("Invalid locale: " + locale);
    }

}
