package io.github.hello09x.devtools.database.transaction;

import io.github.hello09x.devtools.database.exception.DataAccessException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class TransactionStatus {

    private final Plugin plugin;
    private final DataSource dataSource;
    private Connection connection;

    public TransactionStatus(@NotNull Plugin plugin, @NotNull DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
    }

    public @NotNull Connection getConnection() {
        if (this.connection == null) {
            try {
                this.connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }
        return this.connection;
    }

    public void rollback() throws SQLException {
        if (this.connection == null) {
            return;
        }
        this.connection.rollback();
        this.doOnCompleted();
    }

    public void commit() throws SQLException {
        if (this.connection == null) {
            return;
        }
        this.connection.commit();
        this.doOnCompleted();
    }

    private void doOnCompleted() throws SQLException {
        var conn = this.connection;
        this.connection = null;
        conn.close();
    }

}
