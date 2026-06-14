package io.github.hello09x.devtools.database.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public interface RowMapper<T> {

    @Nullable
    T mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException;
}
