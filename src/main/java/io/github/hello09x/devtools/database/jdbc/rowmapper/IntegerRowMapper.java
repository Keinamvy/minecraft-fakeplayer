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
public class IntegerRowMapper implements RowMapper<Integer> {

    public final static IntegerRowMapper instance = new IntegerRowMapper();

    @Override
    public @Nullable Integer mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        return rs.getObject(1, Integer.class);
    }

}
