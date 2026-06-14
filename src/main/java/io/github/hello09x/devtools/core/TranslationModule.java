package io.github.hello09x.devtools.core;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.hello09x.devtools.core.translation.PluginTranslator;
import io.github.hello09x.devtools.core.translation.TranslationConfig;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
public class TranslationModule extends AbstractModule {

    private final TranslationConfig config;

    public TranslationModule(TranslationConfig config) {
        this.config = config;
    }

    @Provides
    @Singleton
    public PluginTranslator pluginTranslator(@NotNull Plugin plugin) {
        var translator = PluginTranslator.of(
                plugin,
                config
        );
        GlobalTranslator.translator().addSource(translator);
        return translator;
    }

}
