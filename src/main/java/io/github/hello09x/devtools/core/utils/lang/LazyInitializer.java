package io.github.hello09x.devtools.core.utils.lang;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class LazyInitializer<T> implements Supplier<T> {

    private final static Object NIL = new Object();

    private volatile T instance;

    private Supplier<T> supplier;

    protected LazyInitializer(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LazyInitializer<T> of(@NotNull Supplier<T> supplier) {
        return new LazyInitializer<>(supplier);
    }

    @Override
    public T get() {
        if (this.instance == null) {
            synchronized (this) {
                if (this.instance == null) {
                    var instance = this.supplier.get();
                    if (instance == null) {
                        instance = (T) NIL;
                    }
                    this.instance = instance;
                    this.supplier = null;
                }
            }
        }

        return instance == NIL ? null : instance;
    }
}
