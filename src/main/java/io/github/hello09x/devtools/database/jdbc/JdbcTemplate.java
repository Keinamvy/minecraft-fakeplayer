package io.github.hello09x.devtools.database.jdbc;

import io.github.hello09x.devtools.database.exception.DataAccessException;
import io.github.hello09x.devtools.database.jdbc.extractor.RowMapperResultSetExtractor;
import io.github.hello09x.devtools.database.jdbc.rowmapper.MapRowMapper;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class JdbcTemplate {

    private final Plugin plugin;

    private final DataSource dataSource;

    public JdbcTemplate(@NotNull Plugin plugin, @NotNull DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
    }

    public void execute(@NotNull String sql) {
        this.execute0(stm -> {
            stm.execute(sql);
            return null;
        });
    }

    public <T> @UnknownNullability T query(@NotNull String sql, @NotNull ResultSetExtractor<T> extractor) {
        return this.execute0(stm -> {
            var rs = stm.executeQuery(sql);
            return extractor.extractData(rs);
        });
    }

    public <T> @UnknownNullability T query(@NotNull String sql, @NotNull ResultSetExtractor<T> extractor, Object @NotNull ... args) {
        return this.execute0(stm -> {
            var rs = stm.executeQuery();
            return extractor.extractData(rs);
        }, sql, args);
    }

    public <T> @NotNull List<T> query(@NotNull String sql, @NotNull RowMapper<T> rowMapper) {
        return this.query(sql, new RowMapperResultSetExtractor<>(rowMapper));
    }

    public <T> @NotNull List<T> query(@NotNull String sql, @NotNull RowMapper<T> rowMapper, Object... args) {
        return this.query(sql, new RowMapperResultSetExtractor<>(rowMapper), args);
    }

    public <T> @Nullable T queryForObject(@NotNull String sql, @NotNull RowMapper<T> rowMapper) {
        var results = this.query(sql, new RowMapperResultSetExtractor<>(rowMapper));
        return JdbcUtils.nullableSingleResult(results);
    }

    public <T> @Nullable T queryForObject(@NotNull String sql, @NotNull RowMapper<T> rowMapper, Object @NotNull ... args) {
        var results = this.query(sql, new RowMapperResultSetExtractor<>(rowMapper), args);
        return JdbcUtils.nullableSingleResult(results);
    }

    public @Nullable Map<String, Object> queryForMap(@NotNull String sql) throws SQLException {
        var results = query(sql, new RowMapperResultSetExtractor<>(new MapRowMapper()));
        return JdbcUtils.nullableSingleResult(results);
    }

    public @Nullable Map<String, Object> queryForMap(@NotNull String sql, Object @NotNull ... args) throws SQLException {
        var results = query(sql, new RowMapperResultSetExtractor<>(new MapRowMapper()), args);
        return JdbcUtils.nullableSingleResult(results);
    }

    public @NotNull List<Map<String, Object>> queryForList(@NotNull String sql) {
        return query(sql, new RowMapperResultSetExtractor<>(new MapRowMapper()));
    }

    public @NotNull List<Map<String, Object>> queryForList(@NotNull String sql, Object... args) {
        return query(sql, new RowMapperResultSetExtractor<>(new MapRowMapper()), args);
    }

    public int update(@NotNull String sql) {
        return execute0(stm -> stm.executeUpdate(sql));
    }

    public int update(@NotNull String sql, Object... args) {
        return execute0(PreparedStatement::executeUpdate, sql, args);
    }

    public int update(@NotNull String sql, @NotNull KeyHolder keyHolder, Object... args) {
        return execute0(con -> con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS),
                stm -> {
                    var rows = stm.executeUpdate();
                    var generatedKeys = new RowMapperResultSetExtractor<>(new MapRowMapper()).extractData(stm.getGeneratedKeys());
                    if (generatedKeys != null) {
                        keyHolder.getKeyList().addAll(generatedKeys);
                    }
                    return rows;
                },
                args
        );
    }

    private <T> @UnknownNullability T execute0(@NotNull SQLExchanger<Statement, T> execution) {
        var con = DataSourceUtils.getConnection(this.plugin, dataSource);
        Statement stm = null;
        try {
            stm = con.createStatement();
            return execution.exchange(stm);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            JdbcUtils.closeStatement(this.plugin, stm);
            DataSourceUtils.releaseConnection(this.plugin, con);
        }
    }

    private <T> @UnknownNullability T execute0(@NotNull PreparedStatementCreator preparedStatementCreator, @NotNull SQLExchanger<PreparedStatement, T> execution, Object @NotNull ... args) {
        var con = DataSourceUtils.getConnection(this.plugin, dataSource);
        PreparedStatement stm = null;
        try {
            stm = preparedStatementCreator.createPreparedStatement(con);
            for (int i = 1; i <= args.length; i++) {
                stm.setObject(i, args[i - 1]);
            }
            return execution.exchange(stm);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            JdbcUtils.closeStatement(this.plugin, stm);
            DataSourceUtils.releaseConnection(this.plugin, con);
        }
    }

    private <T> @UnknownNullability T execute0(@NotNull SQLExchanger<PreparedStatement, T> execution, @NotNull String sql, Object @NotNull ... args) {
        return execute0(con -> con.prepareStatement(sql), execution, args);
    }

}
