package io.github.hello09x.devtools.core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
public abstract class ConfigUtils {

    public static int getMaxIfZero(int value) {
        if (value == 0) {
            return Integer.MAX_VALUE;
        }
        return value;
    }

    public static <E extends Enum<E>> E getEnum(@Nullable String name, @NotNull Class<E> clz, E defaultValue) {
        if (name == null || name.isEmpty()) {
            return defaultValue;
        }
        return Enum.valueOf(clz, name);
    }

    public static <E extends Enum<E>> E getEnum(@NotNull FileConfiguration config, @NotNull String key, @NotNull Class<E> type, @Nullable E defaultValue) {
        var value = config.getString(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }

        try {
            return Enum.valueOf(type, value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The value of %s in config.yml is invalid, allowed values are %s".formatted(
                    key,
                    Arrays.stream(type.getEnumConstants()).map(E::name).collect(Collectors.joining(", "))
            ), e);
        }
    }

}
