package io.github.hello09x.devtools.database.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/8/14
 **/
public interface PreparedStatementCreator {

    @NotNull
    PreparedStatement createPreparedStatement(Connection con) throws SQLException;

}
