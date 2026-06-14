package io.github.hello09x.devtools.database.jdbc.rowmapper;

import io.github.hello09x.devtools.database.jdbc.GeneratedKeyHolder;
import io.github.hello09x.devtools.database.jdbc.RowMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/8/5
 **/
public class GeneratedKeyHolderRowMapper implements RowMapper<GeneratedKeyHolder> {


    @Override
    public @Nullable GeneratedKeyHolder mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
