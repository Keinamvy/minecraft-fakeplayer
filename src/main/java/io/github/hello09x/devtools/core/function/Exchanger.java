package io.github.hello09x.devtools.core.function;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
@FunctionalInterface
public interface Exchanger<T, R, X extends Exception> {

    R exchange(T t) throws X;

}
