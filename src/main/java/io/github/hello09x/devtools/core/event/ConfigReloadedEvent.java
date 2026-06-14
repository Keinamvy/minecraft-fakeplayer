package io.github.hello09x.devtools.core.event;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
public class ConfigReloadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @NotNull
    private final Plugin plugin;

    private final FileConfiguration config;

    public ConfigReloadedEvent(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    public @NotNull FileConfiguration getConfig() {
        return config;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
