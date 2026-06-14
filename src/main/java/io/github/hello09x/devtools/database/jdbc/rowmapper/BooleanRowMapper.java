package io.github.hello09x.devtools.database.jdbc.rowmapper;

import io.github.hello09x.devtools.database.jdbc.RowMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/8/3
 **/
public class BooleanRowMapper implements RowMapper<Boolean> {

    @Override
    public @Nullable Boolean mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        return rs.getBoolean(1);
    }

}
