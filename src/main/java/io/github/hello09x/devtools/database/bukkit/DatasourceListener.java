package io.github.hello09x.devtools.database.bukkit;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author tanyaofei
 * @since 2024/8/13
 **/
public class DatasourceListener implements Listener {

    private final Plugin plugin;
    private final HikariDataSource dataSource;

    public DatasourceListener(Plugin plugin, HikariDataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPluginDisable(@NotNull PluginDisableEvent event) {
        if (event.getPlugin() != plugin) {
            return;
        }

        dataSource.close();
    }


}
