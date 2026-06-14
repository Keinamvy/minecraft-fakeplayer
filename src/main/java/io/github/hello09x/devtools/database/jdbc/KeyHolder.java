package io.github.hello09x.devtools.database.jdbc;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author tanyaofei
 * @since 2024/8/5
 **/
public interface KeyHolder {

    @Nullable
    Number getKey();

    @Nullable
    <T> T getKeyAs(Class<T> keyType);

    @Nullable
    Map<String, Object> getKeys();

    List<Map<String, Object>> getKeyList();

}
