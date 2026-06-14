package io.github.hello09x.devtools.database.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author tanyaofei
 * @since 2024/8/3
 **/
@Data
@Singleton
public class DatasourceConfig {

    public final static String CONFIG_SECTION = "datasource";
    /**
     * 插件
     */
    private final Plugin plugin;
    /**
     * SQL 驱动类名
     */
    private String driverClassName;
    /**
     * 数据库链接
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 最大连接数
     */
    private int maxPoolSize = 1;
    /**
     * 连接超时时间
     */
    private long connectionTimeout = 1000L;

    @Inject
    public DatasourceConfig(@NotNull Plugin plugin) {
        this.plugin = plugin;
        var dataFolder = this.plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IllegalStateException("Failed to create data folder: " + dataFolder.getAbsolutePath());
        }
        this.reload(plugin.getConfig());
    }

    public void reload(@NotNull FileConfiguration configuration) {
        this.driverClassName = configuration.getString(CONFIG_SECTION + ".driver-class", "org.sqlite.JDBC");
        this.url = configuration.getString(CONFIG_SECTION + ".url", "jdbc:sqlite:" + new File(plugin.getDataFolder(), "data.db").getAbsolutePath());
        this.username = configuration.getString(CONFIG_SECTION + ".username");
        this.password = configuration.getString(CONFIG_SECTION + ".password");
        this.maxPoolSize = configuration.getInt(CONFIG_SECTION + ".max-pool-size", 1);
        this.connectionTimeout = configuration.getInt(CONFIG_SECTION + ".connection-timeout", 1000);
    }

    public @NotNull HikariConfig toHikariConfig() {
        var config = new HikariConfig();
        config.setJdbcUrl(this.url);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setPoolName(this.plugin.getName() + "-datasource");
        config.setMaximumPoolSize(this.maxPoolSize);
        config.setConnectionTimeout(this.connectionTimeout);
        return config;
    }

}
