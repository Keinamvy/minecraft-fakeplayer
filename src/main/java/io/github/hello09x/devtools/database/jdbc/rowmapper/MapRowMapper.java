package io.github.hello09x.devtools.database.jdbc.rowmapper;

import io.github.hello09x.devtools.database.jdbc.JdbcUtils;
import io.github.hello09x.devtools.database.jdbc.RowMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class MapRowMapper implements RowMapper<Map<String, Object>> {

    @Override
    public @Nullable Map<String, Object> mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        var md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        var result = new LinkedHashMap<String, Object>();
        for (int i = 0; i < columnCount; i++) {
            var column = md.getColumnName(i + 1);
            result.put(JdbcUtils.lookupColumnName(md, i + 1), rs.getObject(column));
        }
        return result;
    }

}
