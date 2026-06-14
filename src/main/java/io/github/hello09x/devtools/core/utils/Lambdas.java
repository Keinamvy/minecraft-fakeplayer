package io.github.hello09x.devtools.core.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
public abstract class Lambdas {

    public static <T> T configure(@NotNull T obj, @NotNull Consumer<T> consumer) {
        consumer.accept(obj);
        return obj;
    }

    public static @NotNull Supplier<Void> asSupplier(@NotNull Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

}
