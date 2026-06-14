package io.github.hello09x.devtools.database.exception;

import org.jetbrains.annotations.Nullable;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class DataAccessException extends RuntimeException {

    public DataAccessException(@Nullable String message) {
        super(message);
    }

    public DataAccessException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(@Nullable Throwable cause) {
        super(cause);
    }
}
