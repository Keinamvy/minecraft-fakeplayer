package io.github.hello09x.devtools.core.utils;

import org.bukkit.Bukkit;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public abstract class Assertion {

    public void assertPrimaryThread() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Not in primary thread");
        }
    }

}
