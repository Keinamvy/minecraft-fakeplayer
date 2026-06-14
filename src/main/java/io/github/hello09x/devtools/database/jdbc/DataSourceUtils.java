package io.github.hello09x.devtools.database.jdbc;

import io.github.hello09x.devtools.database.exception.DataAccessException;
import io.github.hello09x.devtools.database.transaction.TransactionManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class DataSourceUtils {

    public static @NotNull Connection getConnection(@NotNull Plugin plugin, @NotNull DataSource dataSource) {
        var tx = TransactionManager.getCurrentTransaction(plugin);
        if (tx != null) {
            return tx.getConnection();
        }
        return fetchConnection(dataSource);
    }

    private static @NotNull Connection fetchConnection(@NotNull DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

        if (connection == null) {
            throw new DataAccessException("Can not fetch a connection from datasource: " + dataSource);
        }

        return connection;
    }

    public static void releaseConnection(@NotNull Plugin plugin, @Nullable Connection connection) {
        if (connection == null) {
            return;
        }

        var tx = TransactionManager.getCurrentTransaction(plugin);
        if (tx != null && tx.getConnection() == connection) {
            // should close by transaction
            return;
        }

        try {
            connection.close();
        } catch (Throwable e) {
            plugin.getLogger().warning("Failed to close connection: " + connection);
        }
    }

}
