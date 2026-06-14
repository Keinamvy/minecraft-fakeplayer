package io.github.hello09x.devtools.storage.value;

import io.github.hello09x.devtools.storage.JSONPersistentDataContainer;
import org.jetbrains.annotations.Nullable;

public class TagContainerArrayValueWrapper extends AbstractValueWrapper<JSONPersistentDataContainer[]> {

    public TagContainerArrayValueWrapper(@Nullable JSONPersistentDataContainer[] value) {
        super(value);
    }

}
