package io.github.hello09x.devtools.database.jdbc;

import io.github.hello09x.devtools.core.function.Exchanger;

import java.sql.SQLException;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
@FunctionalInterface
public interface SQLExchanger<T, R> extends Exchanger<T, R, SQLException> {


}
