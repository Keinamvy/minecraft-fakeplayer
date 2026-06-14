package io.github.hello09x.devtools.database.transaction;

import com.google.common.base.Throwables;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class TransactionTemplate {

    private final Plugin plugin;

    private final TransactionManager transactionManager;

    public TransactionTemplate(@NotNull Plugin plugin, @NotNull TransactionManager transactionManager) {
        this.plugin = plugin;
        this.transactionManager = transactionManager;
    }

    public <T> @UnknownNullability T execute(@NotNull Supplier<T> execution) {
        TransactionStatus tx;
        try {
            tx = transactionManager.getTransactionStatus();
        } catch (SQLException e) {
            throw new UndeclaredThrowableException(e, "Failed to get transaction");
        }

        T result;
        try {
            result = execution.get();
        } catch (Throwable e) {
            this.rollbackOnException(tx);
            throw e;
        }

        try {
            tx.commit();
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e, "Failed to commit transaction: \n" + Throwables.getStackTraceAsString(e));
        }

        return result;
    }

    private void rollbackOnException(@NotNull TransactionStatus tx) {
        try {
            tx.rollback();
        } catch (Throwable ex) {
            throw new UndeclaredThrowableException(ex, "Failed to rollback");
        }
    }


}
