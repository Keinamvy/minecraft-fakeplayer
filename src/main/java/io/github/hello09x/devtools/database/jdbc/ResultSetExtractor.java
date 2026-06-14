package io.github.hello09x.devtools.database.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
@FunctionalInterface
public interface ResultSetExtractor<T> {

    @Nullable
    T extractData(@NotNull ResultSet rs) throws SQLException;

}
