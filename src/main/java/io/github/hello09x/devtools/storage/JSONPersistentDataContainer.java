package io.github.hello09x.devtools.storage;

import com.google.common.base.Throwables;
import io.github.hello09x.devtools.storage.value.AbstractValueWrapper;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JSONPersistentDataContainer implements PersistentDataContainer {

    private final static JSONHandler HANDLER = new JSONHandler();

    private final transient Plugin plugin;

    private final transient String filename;

    @Getter(AccessLevel.PRIVATE)
    private final Map<String, AbstractValueWrapper<?>> data = new HashMap<>();

    public JSONPersistentDataContainer(@NotNull JavaPlugin plugin, @NotNull String filename) {
        this.plugin = plugin;
        this.filename = filename;
        {
            var file = new File(plugin.getDataFolder(), filename);
            if (file.exists() && file.isFile()) {
                try {
                    this.readFromFile(file, false);
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to read persistent data file: " + file.getPath() + "\n" + Throwables.getStackTraceAsString(e));
                }
            }
        }
    }


    public static @NotNull JSONPersistentDataContainer deserializeFromJSON(@NotNull String json) {
        return HANDLER.getGson().fromJson(json, JSONPersistentDataContainer.class);
    }

    @Override
    public <T, Z> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
        data.put(key.toString(), AbstractValueWrapper.of(type, value));
    }

    @Override
    public <T, Z> boolean has(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type) {
        return this.get(key, type) != null;
    }

    @Override
    public <T, Z> @Nullable Z get(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type) {
        return Optional.ofNullable(data.get(key.toString()))
                .map(AbstractValueWrapper::getValue)
                .filter(value -> type.getComplexType().isAssignableFrom(value.getClass()))
                .map(value -> type.getComplexType().cast(value))
                .orElse(null);

    }

    @Override
    public <T, Z> @NotNull Z getOrDefault(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue) {
        return Optional.ofNullable(this.get(key, type)).orElse(defaultValue);
    }

    @Override
    public @NotNull Set<NamespacedKey> getKeys() {
        return this.data.keySet().stream().map(key -> {
            var parts = key.split(":");
            if (parts.length != 2) {
                return null;
            }
            return new NamespacedKey(parts[0], parts[1]);
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public void remove(@NotNull NamespacedKey key) {
        this.data.remove(key.toString());
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public void copyTo(@NotNull PersistentDataContainer other, boolean replace) {
        for (var key : this.getKeys()) {
            if (!replace && other.has(key)) {
                continue;
            }
            var wrapper = this.data.get(key.toString());
            if (wrapper != null) {
                PersistentDataType<?, ?> type = getDataTypeOfWrapper(wrapper);
                if (type != null) {
                    @SuppressWarnings("unchecked")
                    Object value = wrapper.getValue();
                    // We use raw types safely here because the mapped type matches the value type
                    @SuppressWarnings({"rawtypes", "unchecked"})
                    PersistentDataType rawType = type;
                    other.set(key, rawType, value);
                }
            }
        }
    }

    private @Nullable PersistentDataType<?, ?> getDataTypeOfWrapper(@NotNull AbstractValueWrapper<?> wrapper) {
        String className = wrapper.getClass().getSimpleName();
        return switch (className) {
            case "ByteValueWrapper" -> PersistentDataType.BYTE;
            case "ShortValueWrapper" -> PersistentDataType.SHORT;
            case "IntValueWrapper" -> PersistentDataType.INTEGER;
            case "LongValueWrapper" -> PersistentDataType.LONG;
            case "FloatValueWrapper" -> PersistentDataType.FLOAT;
            case "DoubleValueWrapper" -> PersistentDataType.DOUBLE;
            case "StringValueWrapper" -> PersistentDataType.STRING;
            case "ByteArrayValueWrapper" -> PersistentDataType.BYTE_ARRAY;
            case "IntArrayValueWrapper" -> PersistentDataType.INTEGER_ARRAY;
            case "LongArrayValueWrapper" -> PersistentDataType.LONG_ARRAY;
            case "TagContainerValueWrapper" -> PersistentDataType.TAG_CONTAINER;
            case "TagContainerArrayValueWrapper" -> PersistentDataType.TAG_CONTAINER_ARRAY;
            // Fallback if it's an unmapped or complex custom type
            default -> null;
        };
    }

    @Override
    public @NotNull PersistentDataAdapterContext getAdapterContext() {
        throw new UnsupportedOperationException("getAdapterContext");
    }

    @Override
    public boolean has(@NotNull NamespacedKey key) {
        return this.data.containsKey(key.toString());
    }

    public @NotNull String serializeToJSON() {
        return HANDLER.toJSON(this);
    }

    public void readFromBytes(byte @NotNull [] bytes, boolean clear) {
        if (clear) {
            this.data.clear();
        }

        Optional.ofNullable(HANDLER.fromJSON(new String(bytes, StandardCharsets.UTF_8), JSONPersistentDataContainer.class))
                .map(JSONPersistentDataContainer::getData)
                .ifPresent(this.data::putAll);
    }

    public boolean readFromFile(@NotNull File file, boolean clear) throws IOException {
        if (clear) {
            this.data.clear();
        }
        if (!file.exists() || !file.isFile()) {
            return false;
        }

        Optional.ofNullable(HANDLER.fromFile(file, JSONPersistentDataContainer.class))
                .map(JSONPersistentDataContainer::getData)
                .ifPresent(this.data::putAll);
        return true;
    }

    public byte[] serializeToBytes() throws IOException {
        return this.serializeToJSON().getBytes(StandardCharsets.UTF_8);
    }

    public void clear() {
        this.data.clear();
    }

    public void saveFile() {
        var file = new File(plugin.getDataFolder(), filename);
        try (var out = new FileOutputStream(file)) {
            IOUtil.copy(this.serializeToBytes(), out);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save persistent data file: " + file.getPath() + "\n" + Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public int getSize() {
        return this.data.size();
    }

}
