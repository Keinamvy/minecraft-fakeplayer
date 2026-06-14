package io.github.hello09x.devtools.database.jdbc.rowmapper;

import io.github.hello09x.devtools.database.jdbc.RowMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/8/5
 **/
public class LongRowMapper implements RowMapper<Long> {

    public final static LongRowMapper instance = new LongRowMapper();

    @Override
    public @Nullable Long mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        return rs.getObject(0, Long.class);
    }

}
