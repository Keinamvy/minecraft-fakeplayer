package io.github.hello09x.devtools.core.function;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
@FunctionalInterface
public interface Executable<T, X extends Throwable> {

    X execute() throws X;

}
