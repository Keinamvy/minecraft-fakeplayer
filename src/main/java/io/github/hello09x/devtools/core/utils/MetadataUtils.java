package io.github.hello09x.devtools.core.utils;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author tanyaofei
 * @since 2024/8/2
 **/
public class MetadataUtils {

    public static @NotNull Optional<MetadataValue> find(@NotNull Plugin plugin, @Nullable Metadatable metadatable, @Nullable String metadataKey, @NotNull Class<?> type) {
        if (metadatable == null) {
            return Optional.empty();
        }

        if (metadataKey == null) {
            return Optional.empty();
        }

        var values = metadatable.getMetadata(metadataKey);
        if (values.isEmpty()) {
            return Optional.empty();
        }

        return values.stream().filter(v -> v.getOwningPlugin() == plugin && type.isInstance(v.value())).findAny();
    }


}
