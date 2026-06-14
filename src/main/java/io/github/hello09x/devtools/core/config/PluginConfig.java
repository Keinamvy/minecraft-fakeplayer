package io.github.hello09x.devtools.core.config;

import com.google.inject.Inject;
import io.github.hello09x.devtools.core.event.ConfigReloadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.codehaus.plexus.util.IOUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
public abstract class PluginConfig {

    public static final String CONFIG_FILE_NAME = "config.yml";
    public static final String CONFIG_TMPL_FILE_NAME = "config.tmpl.yml";

    private final Plugin plugin;

    protected PluginConfig(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        this.doReload();
        Bukkit.getServer().getPluginManager().callEvent(new ConfigReloadedEvent(this.plugin));
    }

    @Inject
    protected void doReload() {
        var folder = this.plugin.getDataFolder();
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IllegalStateException("Failed to create data folder for plugin: " + plugin.getName());
        }

        // config.tmpl.config always update
        var tmplFile = new File(folder, CONFIG_TMPL_FILE_NAME);
        this.copyJarConfigFile(tmplFile);

        // config.yml create if not exists
        var configFile = new File(folder, CONFIG_FILE_NAME);
        if (!configFile.exists()) {
            this.copyJarConfigFile(configFile);
        }

        plugin.reloadConfig();
        this.reload(plugin.getConfig());
    }

    public boolean isConfigFileExists() {
        return new File(this.plugin.getDataFolder(), CONFIG_FILE_NAME).exists();
    }

    public boolean copyJarConfigFile(@NotNull File file) {
        var jarFile = plugin.getResource(CONFIG_FILE_NAME);
        if (jarFile == null) {
            return false;
        }

        try (var out = new FileOutputStream(file)) {
            IOUtil.copy(jarFile, out);
            return true;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate " + file.getAbsolutePath(), e);
        }
    }

    public boolean isConfigFileOutOfDate() {
        var version = Optional.ofNullable(plugin.getConfig().getDefaults()).map(def -> def.getString("version")).orElse(null);
        if (version == null) {
            return false;
        }

        var current = plugin.getConfig().getString("version");
        return !Objects.equals(current, version);
    }

    protected abstract void reload(@NotNull FileConfiguration file);

}
