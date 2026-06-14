package io.github.hello09x.devtools.database.jdbc;

import io.github.hello09x.devtools.database.exception.TooManyResultException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class JdbcUtils {

    public static void closeStatement(@NotNull Plugin plugin, @Nullable Statement stm) {
        if (stm == null) {
            return;
        }
        try {
            stm.close();
        } catch (Throwable e) {
            plugin.getLogger().warning("Failed to close statement: " + e.getMessage());
        }
    }

    @Nullable
    public static <T> T nullableSingleResult(@Nullable Collection<T> results) {
        if (results == null || results.isEmpty()) {
            return null;
        }

        if (results.size() > 1) {
            throw new TooManyResultException("Too many results: " + results.size());
        }

        return results.iterator().next();
    }

    @Nullable
    public static <T> T nullableSingleResult(@Nullable Stream<T> results) throws SQLException {
        if (results == null) {
            return null;
        }
        return nullableSingleResult(results.limit(2).toList());
    }


    public static @NotNull String lookupColumnName(@NotNull ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (!name.isEmpty()) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }
        return name;
    }

}
