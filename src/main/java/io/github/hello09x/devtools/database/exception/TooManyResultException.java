package io.github.hello09x.devtools.database.exception;

import org.jetbrains.annotations.Nullable;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class TooManyResultException extends DataAccessException {

    public TooManyResultException(@Nullable String message) {
        super(message);
    }

    public TooManyResultException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public TooManyResultException(@Nullable Throwable cause) {
        super(cause);
    }
}
