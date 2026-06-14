package io.github.hello09x.devtools.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariDataSource;
import io.github.hello09x.devtools.database.bukkit.DatasourceListener;
import io.github.hello09x.devtools.database.config.DatasourceConfig;
import io.github.hello09x.devtools.database.jdbc.JdbcTemplate;
import io.github.hello09x.devtools.database.transaction.TransactionManager;
import io.github.hello09x.devtools.database.transaction.TransactionTemplate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
public class DatabaseModule extends AbstractModule {


    public DatabaseModule() {

    }

    @Singleton
    @Provides
    private DataSource dataSource(@NotNull Plugin plugin, @NotNull DatasourceConfig config) {
        var datasource = new HikariDataSource(config.toHikariConfig());
        Bukkit.getPluginManager().registerEvents(new DatasourceListener(plugin, datasource), plugin);
        return datasource;
    }

    @Provides
    @Singleton
    private JdbcTemplate jdbcTemplate(@NotNull Plugin plugin, @NotNull DataSource dataSource) {
        return new JdbcTemplate(plugin, dataSource);
    }

    @Provides
    @Singleton
    private TransactionManager transactionManager(@NotNull Plugin plugin, @NotNull DataSource dataSource) {
        return new TransactionManager(plugin, dataSource);
    }

    @Provides
    @Singleton
    private TransactionTemplate transactionTemplate(@NotNull Plugin plugin, @NotNull TransactionManager transactionManager) {
        return new TransactionTemplate(plugin, transactionManager);
    }

}
