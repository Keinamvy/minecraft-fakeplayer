package io.github.hello09x.devtools.core.utils;

import java.util.function.Supplier;

/**
 * @author tanyaofei
 * @since 2024/8/3
 **/
public class SingletonSupplier<T> implements Supplier<T> {

    private final Supplier<T> instanceSupplier;
    private volatile T instance;

    public SingletonSupplier(Supplier<T> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    @Override
    public T get() {
        T instance = this.instance;
        if (instance == null) {
            synchronized (this) {
                instance = this.instance;
                if (instance == null) {
                    instance = this.instanceSupplier.get();
                }
            }
        }

        return instance;
    }

    public void remove() {
        this.instance = null;
    }

}
