package io.github.hello09x.devtools.database.transaction;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class TransactionManager {

    private final static Map<Plugin, ThreadLocal<TransactionStatus>> TXS = new ConcurrentHashMap<>();
    private final Plugin plugin;
    private final DataSource dataSource;

    public TransactionManager(@NotNull Plugin plugin, @NotNull DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
    }

    public static @Nullable TransactionStatus getCurrentTransaction(@NotNull Plugin plugin) {
        return Optional.ofNullable(TXS.get(plugin)).map(ThreadLocal::get).orElse(null);
    }

    public @NotNull TransactionStatus getTransactionStatus() throws SQLException {
        var tx = getCurrentTransaction(this.plugin);
        if (tx == null) {
            tx = new TransactionStatus(this.plugin, this.dataSource);
            TXS.computeIfAbsent(this.plugin, k -> new ThreadLocal<>()).set(tx);
        }
        return tx;
    }

    public void commit(@NotNull TransactionStatus status) throws SQLException {
        status.commit();
    }

    public void rollback(@NotNull TransactionStatus status) throws SQLException {
        status.rollback();
    }

}
